package com.xu.graphqldemo.graphql;

import com.xu.graphqldemo.model.Author;
import com.xu.graphqldemo.model.AuthorDataSource;
import com.xu.graphqldemo.model.Book;
import com.xu.graphqldemo.model.BookDataSource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
//todo 在数据源上直接使用一个注解
public class AuthorDataLoaderDefinition extends DataLoaderDefinition {
    public AuthorDataLoaderDefinition() throws NoSuchMethodException {
        setLoaderName("authorDataLoader");
        setDataSourceClazz(AuthorDataSource.class);
        setMethodName("queryByIds");
        setParamsType(Collections.singletonList(Set.class));
        setResultClass(Author.class);
    }

}
