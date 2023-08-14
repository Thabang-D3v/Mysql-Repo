package com.formidex.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Forex {
    private LocalDate date;
    @JsonProperty("USD")
    private String uSD;
    @JsonProperty("JPY")
    private String jPY;
    @JsonProperty("BGN")
    private String bGN;
    @JsonProperty("CYP")
    private String cYP;
    @JsonProperty("CZK")
    private String cZK;
    @JsonProperty("DKK")
    private String dKK;
    @JsonProperty("EEK")
    private String eEK;
    @JsonProperty("GBP")
    private String gBP;
    @JsonProperty("HUF")
    private String hUF;
    @JsonProperty("LTL")
    private String lTL;
    @JsonProperty("LVL")
    private String lVL;
    @JsonProperty("MTL")
    private String mTL;
    @JsonProperty("PLN")
    private String pLN;
    @JsonProperty("ROL")
    private String rOL;
    @JsonProperty("RON")
    private String rON;
    @JsonProperty("SEK")
    private String sEK;
    @JsonProperty("SIT")
    private String sIT;
    @JsonProperty("SKK")
    private String sKK;
    @JsonProperty("CHF")
    private String cHF;
    @JsonProperty("ISK")
    private String iSK;
    @JsonProperty("NOK")
    private String nOK;
    @JsonProperty("HRK")
    private String hRK;
    @JsonProperty("RUB")
    private String rUB;
    @JsonProperty("TRL")
    private String tRL;
    @JsonProperty("TRY")
    private String tRY;
    @JsonProperty("AUD")
    private String aUD;
    @JsonProperty("BRL")
    private String bRL;
    @JsonProperty("CAD")
    private String cAD;
    @JsonProperty("CNY")
    private String cNY;
    @JsonProperty("HKD")
    private String hKD;
    @JsonProperty("IDR")
    private String iDR;
    @JsonProperty("ILS")
    private String iLS;
    @JsonProperty("INR")
    private String iNR;
    @JsonProperty("KRW")
    private String kRW;
    @JsonProperty("MXN")
    private String mXN;
    @JsonProperty("MYR")
    private String mYR;
    @JsonProperty("NZD")
    private String nZD;
    @JsonProperty("PHP")
    private String pHP;
    @JsonProperty("SGD")
    private String sGD;
    @JsonProperty("THB")
    private String tHB;
    @JsonProperty("ZAR")
    private String zAR;

    /**
     *
     * @param fieldName the name of the field to get the value of
     * @return the value of the field with the specified name
     * @throws Exception the NoSuchElement exception in case wrong field name is parsed
     */
    public String getValue(String fieldName) throws Exception {

            fieldName = fieldName.substring(0, 1).toLowerCase() +
                    fieldName.substring(1).toUpperCase();
             Field field= this.getClass().getDeclaredField(fieldName);
             Object o=field.get(this);//get the value of the field
            if (o instanceof String)
                return (String)o;//cast to string if it is an instance of
            else return "N/A";

    }

    /**
     *
     * @param s The field name of the currency
     * @return the currency exchange rate converted to double
     * @throws Exception exception for wrong exchange rates
     */
    public double getExchangeRate(String s)throws Exception{
        String value=getValue(s);//get the value of the exchange rate
        double d=0.0;
        try{
            d=Double.parseDouble(value);
        }catch (Exception e){
            e.printStackTrace();//stack trace for bug fixing
        }
        return d;
    }

    /**
     *
     * @param startDate the start date of the currencies
     * @param endDate the end date of the currencies
     * @return true if the date of the object is between the specified dates else false
     */
    public boolean dateBetween(LocalDate startDate,LocalDate endDate){//method used to filter the list for a given date range
            return date.equals(startDate)||
                    (date.isAfter(startDate)&&
                            date.isBefore(endDate))||
                    date.equals(endDate);
    }

}
