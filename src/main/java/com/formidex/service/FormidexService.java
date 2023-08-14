package com.formidex.service;

import com.formidex.dto.ConvertDTO;
import com.formidex.dto.DateDTO;
import com.formidex.dto.DateRangeDTO;
import com.formidex.dto.ResponseDTO;
import com.formidex.utils.ForexList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class FormidexService {
    /**
     * Business Logic class for the convert, get the highest rate and calculation the average rate of a given currency
     * All methods return the response on the Object ResponseDTO which is made of the HTTP status code, a message and the response of the request
     */
    @Autowired
    private ForexList forexList;
    private final Logger LOG= LoggerFactory.getLogger(FormidexService.class);

    /**
     * Method to retrieve currency exchange values for a given date
     * @param dateDTO the date of the currencies
     * @return the Object containing the currencies of the given date
     */

    public ResponseEntity<ResponseDTO>retrieveByDate(DateDTO dateDTO){
        ResponseDTO responseDTO;//the response body of the api call
        try{
            if (dateDTO.getDate()==null){
                responseDTO=ResponseDTO.builder()
                        .code(400)
                        .response(dateDTO)
                        .message("Date cannot be null")
                        .build();
                LOG.info("The request to retrieve data was unsuccessful, request: {}  response:{}",dateDTO,responseDTO);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
            }
            Optional<Forex> byDate=forexList.getData().stream().
                    filter(forex ->forex.getDate().equals(dateDTO.getDate())).findFirst();//find the forex data that matches the date
            String message=byDate.isEmpty()?"No data matching the date criteria":"Data retrieved successfully";
            responseDTO=ResponseDTO.builder()
                    .code(200)
                    .response(byDate.isEmpty()?null:byDate.get())
                    .message(message)
                    .build();
            LOG.info("The request to retrieve data was a success, request: {}  response:{}",dateDTO,responseDTO);
            return ResponseEntity.ok(responseDTO);
        }catch (Exception e){
            LOG.error("Something went wrong with the request",e);
            responseDTO=ResponseDTO.builder()
                    .code(500)
                    .message("Something went wrong with the request")
                    .response(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    /**
     * Method for converting from one currency to another
     * @param convertDTO the object for the conversion
     * @return the converted amount from one currency to another
     */
    public ResponseEntity<ResponseDTO>convertMoney(ConvertDTO convertDTO) {
        ResponseDTO responseDTO;//the response body of the api call
        String message;
        try {
            List<Forex> byDate = forexList.getData().stream().
                    filter(forex -> forex.getDate().equals(convertDTO.getDate())).collect(Collectors.toList());
            if (!byDate.isEmpty()) {
                Forex forex = byDate.get(0);
                double source=forex.getExchangeRate(convertDTO.getSource());//convert the string to a double
                double destination=forex.getExchangeRate(convertDTO.getDestination());
                if (source==0||destination==0){//one of the currencies was unavailable for the given date
                    message="Forex rates not available for the day";
                    responseDTO= ResponseDTO.builder()
                            .message(message)
                            .code(404)
                            .build();
                    LOG.error("The request to convert money was unsuccessful, request: {}  response:{} message {}",convertDTO,responseDTO,message);
                    return ResponseEntity.ok(responseDTO);
                }
                message="Exchange calculated successfully";
                responseDTO= ResponseDTO.builder()
                        .code(200)
                        .response(exchange(source,destination,convertDTO.getAmount()))
                        .message(message)
                        .build();
                LOG.info("The request to convert money was a success, request: {}  response:{}",convertDTO,responseDTO);
                return ResponseEntity.ok(responseDTO);


            }
            message="Exchange rates not found for the specified date";
            responseDTO= ResponseDTO.builder()
                    .message(message)
                    .response(byDate)
                    .code(200)
                    .build();
            LOG.info("Exchange rates not found for the specified date, request: {}  response:{}",convertDTO,responseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

        } catch (Exception e) {
            String m="Something went wrong with the request";
            message=e instanceof NoSuchFieldException?"Invalid currency provided":m;//checking which exception was thrown to customise the response
            responseDTO= ResponseDTO.builder()
                    .message(message)
                    .code(500)
                    .response(e.getMessage())
                    .build();
            LOG.error(m,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    /**
     *
     * @param dateRangeDTO the data of the request
     * @return the highest exchange rate of the given currency
     */
    public ResponseEntity<ResponseDTO>getHighestExchangeRate(DateRangeDTO dateRangeDTO){
        ResponseDTO responseDTO;//the response body of the api call
        String message;//the message of the response
        try{
            List<Forex> byDate = forexList.getData().stream().
                    filter(forex -> forex.dateBetween(dateRangeDTO.getStart(),
                            dateRangeDTO.getEnd())).collect(Collectors.toList());
           List<Double> references= byDate.stream().map(forex -> getExchangeRate(dateRangeDTO.getCurrency(),forex))
                   .filter(aDouble -> aDouble>0)//filter by the date
                   .sorted().collect(Collectors.toList());//sort the data
           message="No references found on the specified date range";
           if (references.isEmpty()){
               responseDTO= ResponseDTO.builder()
                       .message(message)
                       .response(references)
                       .code(404)
                       .build();
               return ResponseEntity.ok(responseDTO);//the currency had no data in the given date range
           }
            message="Reference retrieved successfully";
            responseDTO= ResponseDTO.builder()
                   .code(200)
                   .response(references.get(0))
                   .message(message)
                   .build();
           return ResponseEntity.ok(responseDTO);//success

        }

        catch (Exception e){
            message="Something went wrong with the request";
            responseDTO= ResponseDTO.builder()
                    .message(message)
                    .code(500)
                    .response(e.getMessage())
                    .build();
            LOG.error(message,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    /**
     *
     * @param dateRangeDTO the object containing the request data
     * @return the average rating enclosed in the ResponseDto class if found
     */
    public ResponseEntity<ResponseDTO>getAverageExchangeRate(DateRangeDTO dateRangeDTO){
        ResponseDTO responseDTO;
        String message;
        try{
            List<Forex> byDate = forexList.getData().stream().
                    filter(forex -> forex.dateBetween(dateRangeDTO.getStart(),
                            dateRangeDTO.getEnd())).collect(Collectors.toList());
            List<Double> references= byDate.stream().map(forex -> getExchangeRate(dateRangeDTO.getCurrency(),forex))
                    .filter(aDouble -> aDouble>0)
                    .sorted().collect(Collectors.toList());
            message="No references found on the specified date range";
            if (references.isEmpty()){
                responseDTO= ResponseDTO.builder()
                        .message(message)
                        .response(references)
                        .code(404)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
            }
            message="Reference retrieved successfully";
            responseDTO= ResponseDTO.builder()
                    .code(200)
                    .response(calculateAverage(references))
                    .message(message)
                    .build();
            return ResponseEntity.ok(responseDTO);

        }

        catch (Exception e){
            message="Something went wrong with the request";
            responseDTO= ResponseDTO.builder()
                    .message(message)
                    .code(500)
                    .response(e.getMessage())
                    .build();
            LOG.error(message,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    public double getExchangeRate(String s,Forex forex){
        //get the value of the exchange rate

        double d=0.0;
        try{
            d=forex.getExchangeRate(s);
        }catch (Exception e){
            e.printStackTrace();//stack trace for bug fixing
        }
        return d;
    }

    /**
     *
     * @param references the references/exchange rates of the given currency
     * @return the average if the currency has been found in the data else 0
     */
    public double calculateAverage(List<Double>references){
        if (references.isEmpty())
            return 0;//return 0 if list is empty to avoid ArithmeticException
        double total=0;
        for (Double reference : references) {
            total+=reference;
        }
        return total/references.size();//calculate and return the average rate
    }



    /**
     * The method to handle the calculation of the amount given the source and dest exchange rate
     * @param source the source exchange rate
     * @param destination the destination exchange rate
     * @param amount amount to be exchanged
     * @return returns the amount multiplied or devided by the exchange rate
     */
    private double exchange(double source,double destination,double amount){
        if (source==destination)
            return amount;
        return source>destination?amount*destination:amount/destination;
    }


}
