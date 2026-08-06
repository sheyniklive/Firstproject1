package org.example.services;


import org.example.cbr.Proxy;
import org.example.dto.*;
import org.example.exception.CurrencyNotFoundException;
import org.example.service.CurrencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private Proxy proxy;

    @InjectMocks
    private CurrencyService service;

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

        CbrApiGetAllResponse result = service.list();

        assertThat(result.getDate()).isEqualTo(localDate);
        assertThat(result.getRates())
                .hasSize(2)
                .extracting(CbrApiGetAllResponse.Rate::getCurrencyCode)
                .containsExactlyInAnyOrder("USD", "EUR");
    }

    @Test
    @DisplayName("getByCode: возвращает найденную валюту")
    void getByCode_shouldReturnValute() {
        when(proxy.getDailyRates()).thenReturn(dailyWith(
                valute("USD", "Доллар США", 1, "75.5")
        ));

        CbrApiGetByCodeResponse result = service.getByCode("USD");

        assertThat(result.getCurrencyCode()).isEqualTo("USD");
        assertThat(result.getCurrencyName()).isEqualTo("Доллар США");
        assertThat(result.getValue()).isEqualByComparingTo(new BigDecimal("75.5"));
        assertThat(result.getDate()).isEqualTo(localDate);
    }

    @Test
    @DisplayName("getByCode: если кода нет — CurrencyNotFoundException")
    void getByCode_shouldThrow_whenCodeMissing() {
        when(proxy.getDailyRates()).thenReturn(dailyWith(
                valute("USD", "Доллар США", 1, "75.5")
        ));

        assertThatThrownBy(() -> service.getByCode("XXX"))
                .isInstanceOf(CurrencyNotFoundException.class);
    }

    @Test
    @DisplayName("convert: обычная конвертация (nominal=1): 1000 / 75.5 = 13.25")
    void convert_shouldConvert_withNominalOne() {
        when(proxy.getDailyRates()).thenReturn(dailyWith(
                valute("USD", "Доллар США", 1, "75.5")
        ));
        CbrApiConvertRequest request = new CbrApiConvertRequest(new BigDecimal("1000"), "USD");

        CbrApiConvertResponse result = service.convert(request);

        assertThat(result.getFromCurrency()).isEqualTo("RUB");
        assertThat(result.getToCurrency()).isEqualTo("USD");
        assertThat(result.getAmountInRubles()).isEqualByComparingTo(new BigDecimal("1000"));
        assertThat(result.getRate()).isEqualByComparingTo(new BigDecimal("75.5"));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(new BigDecimal("13.25"));
        assertThat(result.getDate()).isEqualTo(localDate);
    }

    @Test
    @DisplayName("convert: учитывает номинал (nominal=100): 1000*100 / 55.0 = 1818.18")
    void convert_shouldApplyNominal() {
        when(proxy.getDailyRates()).thenReturn(dailyWith(
                valute("JPY", "Иена", 100, "55.0")
        ));
        CbrApiConvertRequest request = new CbrApiConvertRequest(new BigDecimal("1000"), "JPY");

        CbrApiConvertResponse result = service.convert(request);

        assertThat(result.getConvertedAmount()).isEqualByComparingTo(new BigDecimal("1818.18"));
    }

    @Test
    @DisplayName("convert: округляет HALF_UP: 1 / 8 = 0.125 -> 0.13")
    void convert_shouldRoundHalfUp() {
        when(proxy.getDailyRates()).thenReturn(dailyWith(
                valute("USD", "Доллар США", 1, "8")
        ));
        CbrApiConvertRequest request = new CbrApiConvertRequest(new BigDecimal("1"), "USD");

        CbrApiConvertResponse result = service.convert(request);

        assertThat(result.getConvertedAmount()).isEqualByComparingTo(new BigDecimal("0.13"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-100"})
    @DisplayName("convert: сумма <= 0 -> IllegalArgumentException")
    void convert_shouldThrow_whenAmountNotPositive(String amount) {
        CbrApiConvertRequest request = new CbrApiConvertRequest(new BigDecimal(amount), "USD");

        assertThatThrownBy(() -> service.convert(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Сумма в рублях <= 0 (" + request.getAmountInRubles() + ")");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("convert: пустой код валюты -> IllegalArgumentException")
    void convert_shouldThrow_whenCurrencyBlank(String currency) {
        CbrApiConvertRequest request = new CbrApiConvertRequest(new BigDecimal("1000"), currency);

        assertThatThrownBy(() -> service.convert(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Передан неверный код валюты - " + request.getTargetCurrency());
    }

    @Test
    @DisplayName("convert: валюты нет в ответе ЦБ -> CurrencyNotFoundException")
    void convert_shouldThrow_whenCurrencyNotFound() {
        when(proxy.getDailyRates()).thenReturn(dailyWith(
                valute("USD", "Доллар США", 1, "75.5")
        ));
        CbrApiConvertRequest request = new CbrApiConvertRequest(new BigDecimal("1000"), "XXX");

        assertThatThrownBy(() -> service.convert(request))
                .isInstanceOf(CurrencyNotFoundException.class);
    }

}
