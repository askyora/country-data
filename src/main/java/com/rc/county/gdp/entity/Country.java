package com.rc.county.gdp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Country {
    @Id
    private Long id;

    private String name;
}
