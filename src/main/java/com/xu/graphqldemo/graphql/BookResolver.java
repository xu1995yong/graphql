package com.xu.graphqldemo.graphql;

import com.xu.graphqldemo.model.Author;
import com.xu.graphqldemo.model.Book;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class BookResolver implements GraphQLResolver<Book> {

    public CompletableFuture<List<Book>> getBook(List<String> bookIds, DataFetchingEnvironment dfe) {
        DataLoaderRegistry registry = ((GraphQLContext) dfe.getContext()).getDataLoaderRegistry();
        DataLoader<String, Book> bookLoader = registry.getDataLoader("bookDataLoader");
        if (bookLoader != null) {
            return bookLoader.loadMany(bookIds);
        }
        throw new IllegalStateException("No Book data loader found");
    }

    public CompletableFuture<Author> getAuthor(Book book, DataFetchingEnvironment dfe) {
        DataLoaderRegistry registry = ((GraphQLContext) dfe.getContext()).getDataLoaderRegistry();
        DataLoader<String, Author> bookLoader = registry.getDataLoader("authorDataLoader");
        if (bookLoader != null) {
            return bookLoader.load(book.getAuthorId());
        }
        throw new IllegalStateException("No Author data loader found");
    }
}
