package com.xu.graphqldemo.graphql;

import com.xu.graphqldemo.model.Author;
import com.xu.graphqldemo.model.Book;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 还需要解决：
 *  1.Schema 中的query 对应哪个DataLoader
 *  2. Schema query 中某个字段对应哪个DataLoader
 */
@Component
public class BookQueryResolver implements GraphQLQueryResolver {
    public CompletableFuture<List<Book>> queryBooks(List<String> bookIds, DataFetchingEnvironment dfe) {
        DataLoaderRegistry registry = ((GraphQLContext) dfe.getContext()).getDataLoaderRegistry();
        DataLoader<String, Book> bookLoader = registry.getDataLoader("bookDataLoader");
        if (bookLoader != null) {
            return bookLoader.loadMany(bookIds);
        }
        throw new IllegalStateException("No customer data loader found");
    }

    public CompletableFuture<Book> queryBook(String bookId, String bookName, DataFetchingEnvironment dfe) {
        DataLoaderRegistry registry = ((GraphQLContext) dfe.getContext()).getDataLoaderRegistry();
        DataLoader<String, Book> bookLoader = registry.getDataLoader("bookDataLoader");
        if (bookLoader != null) {
            return bookLoader.load(bookId, new BookQueryContext(bookId, bookName));
        }
        throw new IllegalStateException("No customer data loader found");
    }

    @Data
    @AllArgsConstructor
    static class BookQueryContext {
        String bookId;
        String bookName;
    }

}
