package com.yora.data.dao;

import com.yora.data.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryDAO extends JpaRepository<Country, Long> {

    Optional<Country> findByName(String country);

    @Query("select c from Country c join fetch c.codesList co where co.code = :code ")
    Optional<Country> findByCode(@Param("code") String code);
}
