package com.formidex.service;

import com.formidex.dto.ConvertDTO;
import com.formidex.dto.DateDTO;
import com.formidex.dto.DateRangeDTO;
import com.formidex.dto.ResponseDTO;
import com.formidex.utils.ForexList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
@WebMvcTest(FormidexService.class)
class FormidexServiceTest {
    @MockBean
    private ForexList forexList;
    @Autowired
    private FormidexService formidexService;

    @Test
    void retrieveByDate() {
        int number =new Random().nextInt(10);
        Mockito.when(forexList.getData()).thenReturn(getData());//mock the response from the bean creating which
                                                                //is not available during test phase
        DateDTO dto=DateDTO.builder()
                .date(LocalDate.now().minusDays(number))
                .build();
        ResponseEntity<ResponseDTO>retrieveByDate=formidexService.retrieveByDate(dto);
        Assertions.assertEquals(HttpStatus.OK,retrieveByDate.getStatusCode());//assert the response from the logic
        System.out.println(retrieveByDate);

    }

    @Test
    void convertMoney() {
        int number =new Random().nextInt(10);
        Mockito.when(forexList.getData()).thenReturn(getData());
        ConvertDTO dto=ConvertDTO.builder()
                .amount(new Random().nextDouble())
                .date(LocalDate.now().minusDays(number))
                .destination(getCurrency())
                .source(getCurrency())
                .build();
        ResponseEntity<ResponseDTO>convertMoney=formidexService.convertMoney(dto);
        Assertions.assertEquals(HttpStatus.OK,convertMoney.getStatusCode());

        System.out.println(convertMoney);

    }

    @Test
    void getHighestExchangeRate() {
        int number =new Random().nextInt(10);
        Mockito.when(forexList.getData()).thenReturn(getData());
        DateRangeDTO dateRangeDTO=DateRangeDTO.builder()
                .currency(getCurrency())
                .end(LocalDate.now().minusDays(number))
                .start(LocalDate.now())
                .build();
        ResponseEntity<ResponseDTO>highestExchangeRate=formidexService.getHighestExchangeRate(dateRangeDTO);
        Assertions.assertEquals(HttpStatus.OK,highestExchangeRate.getStatusCode());
        System.out.println(highestExchangeRate);

    }

    @Test
    void getAverageExchangeRate() {
        int number =new Random().nextInt(10);
        Mockito.when(forexList.getData()).thenReturn(getData());
        DateRangeDTO dateRangeDTO=DateRangeDTO.builder()
                .currency(getCurrency())
                .end(LocalDate.now().minusDays(number))
                .start(LocalDate.now())
                .build();
        ResponseEntity<ResponseDTO>getAverageExchangeRate=formidexService.getAverageExchangeRate(dateRangeDTO);
        Assertions.assertEquals(true,true);
        System.out.println(getAverageExchangeRate);



    }

    public List<Forex> getData(){
        URI uri=URI.create("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.zip");
        List<Forex>forexList=new ArrayList<>();
        try (InputStream in = uri.toURL().openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream("currency.csv")) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            BufferedInputStream bis = new BufferedInputStream(uri.toURL().openStream());//stream for deserializing the file from network
            ZipInputStream zip=new ZipInputStream(bis);
            ZipEntry entry=zip.getNextEntry();
            String lines=new String(zip.readAllBytes());
            //System.out.println(lines);
            String[]strings=lines.split("\n");
            for (String string : strings) {
                if (string.toLowerCase().contains("date"))
                    continue;
                String[]values=string.split(",");

                int i=0;
                try {
                    Forex forex = Forex.builder()//creating objects from the file
                            .date(LocalDate.parse(values[i]))
                            .uSD(values[++i]).jPY(values[++i])
                            .bGN(values[++i]).cYP(values[++i])
                            .cZK(values[++i]).dKK(values[++i])
                            .eEK(values[++i]).gBP(values[++i])
                            .hUF(values[++i]).lTL(values[++i])
                            .lVL(values[++i]).mTL(values[++i])
                            .pLN(values[++i]).rOL(values[++i])
                            .rON(values[++i]).sEK(values[++i])
                            .sIT(values[++i]).sKK(values[++i])
                            .cHF(values[++i]).iSK(values[++i])
                            .nOK(values[++i]).hRK(values[++i])
                            .rUB(values[++i]).tRL(values[++i])
                            .tRY(values[++i]).aUD(values[++i])
                            .bRL(values[++i]).cAD(values[++i])
                            .cNY(values[++i]).hKD(values[++i])
                            .iDR(values[++i]).iLS(values[++i])
                            .iNR(values[++i]).kRW(values[++i])
                            .mXN(values[++i]).mYR(values[++i])
                            .nZD(values[++i]).pHP(values[++i])
                            .sGD(values[++i]).tHB(values[++i])
                            .zAR(values[++i])
                            .build();
                    forexList.add(forex);
                }catch (Exception e){
                    System.err.println(string);
                }
            }
            return forexList;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String getCurrency(){//randomly pick a currency for the test case
        List<String>currencies= Arrays.asList("USD","JPY","BGN","CYP","CZK","DKK","EEK","GBP","HUF","LTL","LVL",
                "MTL","PLN","ROL","RON","SEK","SIT","SKK","CHF","ISK","NOK","HRK","RUB","TRL",
                "TRY","AUD","BRL","CAD","CNY","HKD","IDR","ILS","INR","KRW","MXN","MYR","NZD","PHP","SGD","THB","ZAR");
        Collections.shuffle(currencies);
        return currencies.get(0);
    }

}