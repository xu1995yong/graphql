package com.xu.graphqldemo.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BookDataSource {
    private static List<Book> books = Arrays.asList(
            new Book("1", "name1", 1, "author-1"),
            new Book("2", "name2", 2, "author-2"),
            new Book("3", "name3", 3, "author-3")
    );

//    public Book queryById(String bookId) {
//        return books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElse(null);
//    }

    public Map<String, Book> queryByIds(Set<String> bookIds) {
        log.info("BookDataSource queryByIds invoke.parmas:{}", bookIds);
        Map<String, Book> map = new HashMap<>();
        for (Book book : books) {
            if (bookIds.contains(book.getId())) {
                map.put(book.getId(), book);
            }
        }
        return map;
    }

}
