package com.rc.country.rest;

import com.rc.country.dao.CountryDatedValueDAO;
import com.rc.country.entity.CountryDatedValue;
import com.rc.country.entity.CountryValueTypes;
import com.rc.country.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private CountryDatedValueDAO datedValueDAO;

    @Autowired
    private DateUtil dateUtil;

    @Operation(summary = "Data Retriever")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Country Values", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "No Data found.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content)
    })
    @GetMapping(value = "/{country-code}/{value-type}/{year}")
    public ResponseEntity<String> getValueData(
            @PathVariable(value = "country-code", required = true) String countryCode,
            @PathVariable(value = "value-type", required = true) String type,
            @PathVariable(value = "year", required = true) int year
    ) {

        Optional<CountryDatedValue> optional = datedValueDAO.findByCode
                (countryCode, CountryValueTypes.valueOf(type.toUpperCase()), dateUtil.getDateByYear(year));

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get().getValue().toString());
        }
        return ResponseEntity.notFound().build();
    }


}
