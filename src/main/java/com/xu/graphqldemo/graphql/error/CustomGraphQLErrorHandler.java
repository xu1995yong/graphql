package com.xu.graphqldemo.graphql.error;

import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class CustomGraphQLErrorHandler implements GraphQLErrorHandler {

  @Override
  public List<GraphQLError> processErrors(List<GraphQLError> list) {
    log.info("Handle errors: {}", list);
    return list;
  }
}
