package com.xu.graphqldemo.graphql;

import com.xu.graphqldemo.model.Book;
import com.xu.graphqldemo.model.BookDataSource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
//dataLoader 与 数据源关系
public class BookDataLoaderDefinition extends DataLoaderDefinition {
    public BookDataLoaderDefinition() {
        setLoaderName("bookDataLoader");
        setDataSourceClazz(BookDataSource.class);
        setMethodName("queryByIds");
        setParamsType(Collections.singletonList(Set.class));
        setResultClass(Book.class);
    }

}
