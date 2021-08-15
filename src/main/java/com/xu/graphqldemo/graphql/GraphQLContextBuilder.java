package com.xu.graphqldemo.graphql;

import com.xu.graphqldemo.model.Author;
import com.xu.graphqldemo.model.AuthorDataSource;
import com.xu.graphqldemo.model.Book;
import com.xu.graphqldemo.model.BookDataSource;
import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Component
@Slf4j
public class GraphQLContextBuilder implements GraphQLServletContextBuilder, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, DataLoader> dataLoaders = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, DataLoaderDefinition> beans = applicationContext.getBeansOfType(DataLoaderDefinition.class);
        if (!beans.isEmpty()) {
            for (String beanName : beans.keySet()) {
                DataLoaderDefinition definition = beans.get(beanName);
                DataLoaderOptions options = new DataLoaderOptions();

                DataLoader<Object, Object> dataLoader = DataLoader.newMappedDataLoader(new MappedBatchLoaderWithContext<>() {
                    @Override
                    public CompletionStage load(Set<Object> keys, BatchLoaderEnvironment environment) {
//                        Map<Object, Object> context = environment.getKeyContexts();
                        return CompletableFuture.supplyAsync(() -> {
                            try {
                                Object dataSource = definition.getDataSourceClazz().newInstance();
                                Method method = definition.getDataSourceClazz().getMethod(definition.getMethodName(), definition.getParamsType().toArray(new Class[0]));
                                return method.invoke(dataSource, keys);
                            } catch (Exception e) {
                                log.error("error", e);
                                return null;
                            }
                        });
                    }
                }, options);
                dataLoaders.put(definition.getLoaderName(), dataLoader);
            }
        }
    }


    private DataLoaderRegistry buildDataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        for (String loaderName : dataLoaders.keySet()) {
            dataLoaderRegistry.register(loaderName, dataLoaders.get(loaderName));

        }
        return dataLoaderRegistry;
    }

    @Override
    public GraphQLContext build(HttpServletRequest req, HttpServletResponse response) {
        return DefaultGraphQLServletContext.createServletContext(buildDataLoaderRegistry(), null)
                .with(req)
                .with(response)
                .build();
    }

    @Override
    public GraphQLContext build() {
        return new DefaultGraphQLContext(buildDataLoaderRegistry(), null);
    }

    @Override
    public GraphQLContext build(Session session, HandshakeRequest request) {
        return DefaultGraphQLWebSocketContext.createWebSocketContext(buildDataLoaderRegistry(), null)
                .with(session)
                .with(request)
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
