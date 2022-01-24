package com.yora.data.dao;

import com.yora.data.entity.CountryDatedValue;
import com.yora.data.entity.CountryValueTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface CountryDatedValueDAO extends JpaRepository<CountryDatedValue, Long> {

    @Query("select dv from CountryDatedValue dv join fetch dv.country co where co.id = :id and dv.type = :type and dv.timeStamp = :ts ")
    Optional<CountryDatedValue> findByCountryAndTypeAndYear(@Param("id") Long id, @Param("type") CountryValueTypes type, @Param("ts") Date timeStamp);

    @Query("select dv from CountryDatedValue dv join fetch dv.country co join fetch co.codesList codes where codes.code = :code and dv.type = :type and dv.timeStamp = :ts ")
    Optional<CountryDatedValue> findByCode(@Param("code") String code, @Param("type") CountryValueTypes type, @Param("ts") Date timeStamp);
}
