package org.example.mappers;

import org.example.dto.CbrApiGetAllResponse;
import org.example.dto.CbrApiGetByCodeResponse;
import org.example.dto.CbrDailyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CbrApiMapperTest {

    private CbrDailyResponse.Valute valute(String charCode, String name, int nominal, String value, String previous) {
        CbrDailyResponse.Valute v = new CbrDailyResponse.Valute();
        v.setCharCode(charCode);
        v.setName(name);
        v.setNominal(nominal);
        v.setValue(new BigDecimal(value));
        v.setPrevious(new BigDecimal(previous));
        return v;
    }

    @Test
    @DisplayName("toGetAllResponse: дату приводит к LocalDate, поля валюты переносит верно")
    void toGetAllResponse_shouldMapDateAndFields() {
        CbrDailyResponse daily = new CbrDailyResponse();
        daily.setDate(OffsetDateTime.of(2026, 7, 1, 12, 0, 0, 0, ZoneOffset.UTC));
        Map<String, CbrDailyResponse.Valute> valutes = new HashMap<>();
        valutes.put("USD", valute("USD", "Доллар США", 1, "75.5000", "75.1000"));
        daily.setValutes(valutes);

        CbrApiGetAllResponse response = CbrApiMapper.toGetAllResponse(daily);

        assertThat(response.getDate()).isEqualTo(LocalDate.of(2026, 7, 1));
        assertThat(response.getRates()).hasSize(1);

        CbrApiGetAllResponse.Rate rate = response.getRates().getFirst();

        assertThat(rate.getCurrencyCode()).isEqualTo("USD");
        assertThat(rate.getCurrencyName()).isEqualTo("Доллар США");
        assertThat(rate.getNominal()).isEqualTo(1);
        assertThat(rate.getValue()).isEqualByComparingTo(new BigDecimal("75.5"));
        assertThat(rate.getPrevious()).isEqualByComparingTo(new BigDecimal("75.1"));
    }

    @Test
    @DisplayName("toGetAllResponse: переносит все валюты из мапы в список курсов")
    void toGetAllResponse_shouldMapAllValutes() {
        CbrDailyResponse daily = new CbrDailyResponse();
        daily.setDate(OffsetDateTime.of(2026, 7, 1, 12, 0, 0, 0, ZoneOffset.UTC));
        Map<String, CbrDailyResponse.Valute> valutes = new HashMap<>();
        valutes.put("USD", valute("USD", "Доллар США", 1, "75.5", "75.1"));
        valutes.put("EUR", valute("EUR", "Евро", 1, "82.3", "82.0"));
        daily.setValutes(valutes);

        CbrApiGetAllResponse response = CbrApiMapper.toGetAllResponse(daily);

        assertThat(response.getRates()).hasSize(2)
                .extracting(CbrApiGetAllResponse.Rate::getCurrencyCode)
                .containsExactlyInAnyOrder("USD", "EUR");
    }

    @Test
    @DisplayName("toGetByCodeResponse: переносит поля валюты и проставляет переданную дату")
    void toGetByCodeResponse_shouldMapAllFieldsAndSetDate() {
        CbrDailyResponse.Valute usd = valute("USD", "Доллар США", 1, "75.5000", "75.1000");
        LocalDate date = LocalDate.of(2026, 7, 1);

        CbrApiGetByCodeResponse result = CbrApiMapper.toGetByCodeResponse(usd, date);

        assertThat(result.getCurrencyCode()).isEqualTo("USD");
        assertThat(result.getCurrencyName()).isEqualTo("Доллар США");
        assertThat(result.getNominal()).isEqualTo(1);
        assertThat(result.getValue()).isEqualByComparingTo(new BigDecimal("75.5"));
        assertThat(result.getPrevious()).isEqualByComparingTo(new BigDecimal("75.1"));
        assertThat(result.getDate()).isEqualTo(date);
    }
}