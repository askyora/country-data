package com.yora.data.rest;

import com.yora.data.dao.CountryDatedValueDAO;
import com.yora.data.entity.CountryDatedValue;
import com.yora.data.entity.CountryValueTypes;
import com.yora.data.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/data")
public class DataController {

    protected Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    private CountryDatedValueDAO datedValueDAO;

    @GetMapping(value = "/{country-code}/{value-type}/{year}")
    public ResponseEntity<BigDecimal> getValueData(
            @PathVariable(value = "country-code", required = true) String countryCode,
            @PathVariable(value = "value-type", required = true) String type,
            @PathVariable(value = "year", required = true) int year
    ) {
        Optional<CountryDatedValue> optional = datedValueDAO.findByCode
                (countryCode, CountryValueTypes.valueOf(type.toUpperCase()), DateUtil.getDateByYear(year));

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get().getValue());
        }
        return ResponseEntity.notFound().build();
    }


}
