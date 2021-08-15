package com.xu.graphqldemo.graphql;


import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Data
public abstract class DataLoaderDefinition {

    private String loaderName;
    private Class<?> dataSourceClazz;
    private String methodName;
    private List<Object> paramsType;

    private Class resultClass;
}
