package com.yora.graalvm.service;

import com.yora.graalvm.dto.BaseDto;
import com.yora.graalvm.entity.BaseObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public abstract class AbstractDataLoader<T extends BaseObject, R extends BaseDto> {

    protected Logger log = LogManager.getLogger(this.getClass());

    public List<T> load(InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream);
        Set<R> existingSet = new HashSet<>();
        List<T> listOfCountries = parseList(reader)
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

    abstract List<R> parseList(Reader reader);

    abstract T map(@Valid R dto);

    abstract List<T> save(@Valid List<T> list);

    abstract String getName();

}
