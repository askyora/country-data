package com.rc.country.dao;

import com.rc.country.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryDAO extends JpaRepository<Country,Long> {
    Optional<Country> findByName(String country);
}
