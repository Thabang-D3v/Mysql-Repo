package com.formidex.controller;

import com.formidex.dto.ConvertDTO;
import com.formidex.dto.DateDTO;
import com.formidex.dto.DateRangeDTO;
import com.formidex.dto.ResponseDTO;
import com.formidex.service.FormidexService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@Validated
@RequestMapping
public class Controller {
    @Autowired
    private FormidexService formidexService;

    @PostMapping("/retrieve/date")
    @ApiOperation(value = "Get Forex references based on a given date",
            notes = "This api request required the date to filter the values to retrieve the data based on the date specified",
            response = ResponseDTO.class,
            produces = "application/json")
    public  ResponseEntity<ResponseDTO>retrieveByDate(@Valid @RequestBody DateDTO dateDTO){
        return formidexService.retrieveByDate(dateDTO);//retrieve the reference rate data for a given Date for all available Currencies.
    }
    @PostMapping("/convert")
    @ApiOperation(value = "Get the value of money in another currency based on the references on a given date",
            notes = "All the fields are required in order to calculate the amount",
            response = ResponseDTO.class,
            produces = "application/json")
    public ResponseEntity<ResponseDTO>convert(@RequestBody @Valid ConvertDTO convertDTO){
        return formidexService.convertMoney(convertDTO);//returns the Amount given converted from the first to the second Currency as
                                                        // it would have been on that Date (assuming zero fees).
    }
    @PostMapping("/reference")
    @ApiOperation(value = "Get The references of currencies on a given date range",
            notes = "All values are required",
            response = ResponseDTO.class,
            produces = "application/json")
    public ResponseEntity<ResponseDTO>getByDateRange(@RequestBody @Valid DateRangeDTO dateRangeDTO){
        return formidexService.getHighestExchangeRate(dateRangeDTO);//return the highest reference
                                                                    //exchange rate that the Currency achieved for the period.
    }
    @PostMapping("/average")
    @ApiOperation(value = "Compute the average exchange rate of a given currency",
            notes = "All fields are required",
            response = ResponseDTO.class,
            produces = "application/json")
    public ResponseEntity<ResponseDTO>getAverage(@RequestBody @Valid DateRangeDTO dateRangeDTO){
        return formidexService.getAverageExchangeRate(dateRangeDTO);//determine and return the average
                                                                    //reference exchange rate of that Currency for the period.
    }
}
