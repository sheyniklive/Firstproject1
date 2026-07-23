package org.example.services;


import org.example.cbr.Proxy;
import org.example.dto.CbrApiGetAllResponse;
import org.example.dto.CbrDailyResponse;
import org.example.service.CurrencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private Proxy proxy;

    @InjectMocks
    private CurrencyService currencyService;

    private static final OffsetDateTime date =
            OffsetDateTime.of(2026, 7, 23, 12, 0, 0, 0, ZoneOffset.UTC);
    private static final LocalDate localDate = LocalDate.of(2026, 7, 23);


    private CbrDailyResponse.Valute valute(String code, String name, int nominal, String value) {
        CbrDailyResponse.Valute v = new CbrDailyResponse.Valute();
        v.setCharCode(code);
        v.setName(name);
        v.setNominal(nominal);
        v.setValue(new BigDecimal(value));
        v.setPrevious(new BigDecimal(value));
        return v;
    }

    private CbrDailyResponse dailyWith(CbrDailyResponse.Valute... valutes) {
        Map<String, CbrDailyResponse.Valute> map = new LinkedHashMap<>();
        for (CbrDailyResponse.Valute v : valutes) {
            map.put(v.getCharCode(), v);
        }
        CbrDailyResponse daily = new CbrDailyResponse();
        daily.setDate(date);
        daily.setValutes(map);
        return daily;
    }

    @Test
    @DisplayName("list: отдаёт все курсы из ответа ЦБ")
    void list_shouldReturnAllRates() {
        when(proxy.getDailyRates()).thenReturn(dailyWith(
                valute("USD", "Доллар США", 1, "75.5"),
                valute("EUR", "Евро", 1, "82.3")
        ));

        CbrApiGetAllResponse result = currencyService.list();

        assertThat(result.getDate()).isEqualTo(localDate);
        assertThat(result.getRates())
                .hasSize(2)
                .extracting(CbrApiGetAllResponse.Rate::getCurrencyCode)
                .containsExactlyInAnyOrder("USD", "EUR");
    }



    }
