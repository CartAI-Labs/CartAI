package com.bikemmerce.commerce.domain.ports;

public interface IncrementIdGeneratorPort {

    Long increment(Class<?> clazz);

}
