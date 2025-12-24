package org.example.cbrApi;

import org.example.dto.CbrApiGetAllResponse;
import org.example.dto.CbrApiGetByCodeResponse;
import org.example.dto.CbrDailyResponse;

import java.time.LocalDate;

public class CbrApiMapper {

    public static CbrApiGetAllResponse toGetAllResponse(CbrDailyResponse cbrDailyResponse) {
        CbrApiGetAllResponse response = new CbrApiGetAllResponse();
        response.setDate(cbrDailyResponse.getDate().toLocalDate());
        for (CbrDailyResponse.Valute valute : cbrDailyResponse.getValutes().values()) {
            CbrApiGetAllResponse.Rate rate = new CbrApiGetAllResponse.Rate();
            rate.setCurrencyCode(valute.getCharCode());
            rate.setCurrencyName(valute.getName());
            rate.setNominal(valute.getNominal());
            rate.setValue(valute.getValue());
            rate.setPrevious(valute.getPrevious());
            response.getRates().add(rate);
        }
        return response;
    }

    public static CbrApiGetByCodeResponse toGetByCodeResponse(CbrDailyResponse.Valute valute, LocalDate date) {
        CbrApiGetByCodeResponse response = new CbrApiGetByCodeResponse();
        response.setCurrencyCode(valute.getCharCode());
        response.setCurrencyName(valute.getName());
        response.setNominal(valute.getNominal());
        response.setValue(valute.getValue());
        response.setPrevious(valute.getPrevious());
        response.setDate(date);
        return response;
    }

}
