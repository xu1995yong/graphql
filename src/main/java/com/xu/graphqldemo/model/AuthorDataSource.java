package com.xu.graphqldemo.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthorDataSource {
    private static List<Author> authors = Arrays.asList(
            new Author("author-1", "firstName1", "lastName1"),
            new Author("author-2", "firstName2", "lastName2"),
            new Author("author-3", "firstName3", "lastName3")
    );

    public Author queryById(String authorId) {
        return authors.stream().filter(author -> author.getId().equals(authorId)).findFirst().orElse(null);
    }


    public Map<String, Author> queryByIds(Set<String> authorIds) {
        log.info("AuthorDataSource queryByIds invoke. params:{}", authorIds);
        return authors.stream().filter(author -> authorIds.contains(author.getId())).collect(Collectors.toMap(Author::getId, author -> author));
    }
}
