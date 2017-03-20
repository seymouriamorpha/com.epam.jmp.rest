package com.epam.jmp.rest.domain.builder;

public interface DomainBuilder<T> {

  T build();

  T build(T t);

}
