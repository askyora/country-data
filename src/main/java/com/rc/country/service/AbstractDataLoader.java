package com.rc.country.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.rc.country.entity.BaseObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Slf4j
public abstract class AbstractDataLoader<T extends BaseObject, R> {


    public List<T> load(InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream);
        Set<R> existingSet = new HashSet<>();
        List<T> listOfCountries = new CsvToBeanBuilder<R>
                (reader)
                .withType(getGenericTypeClass(1))
                .build()
                .stream().filter(predicates())
                .map(current -> {
                    if (existingSet.contains(current)) {
                        throw new HttpClientErrorException(HttpStatus.CONFLICT,
                                String.format("[Record : %s] Conflicting with another row.", current.toString()));
                    }
                    existingSet.add(current);
                    return current;
                }).map(this::map).collect(Collectors.toList());

        return save(listOfCountries);
    }

    protected abstract Predicate<R> predicates();


    @SneakyThrows
    private Class<R> getGenericTypeClass(int index) {
        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[index].getTypeName();
        Class<?> clazz = Class.forName(className);
        return (Class<R>) clazz;
    }


    abstract T map(@Valid R dto);

    abstract List<T> save(@Valid List<T> list);

    abstract String getName();

}
