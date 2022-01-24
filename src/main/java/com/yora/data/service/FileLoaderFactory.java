package com.yora.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class FileLoaderFactory {

    @Autowired
    List<AbstractDataLoader> loaderList = new ArrayList<>();

    public Optional<AbstractDataLoader> getLoaderByName(String name) {
        return loaderList.stream().filter(i -> i.getName().equalsIgnoreCase(name)).findFirst();
    }

}
