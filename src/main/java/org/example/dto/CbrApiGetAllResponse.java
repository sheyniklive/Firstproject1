package org.example.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class CbrApiGetAllResponse {
    private LocalDate date;
    private List<Rate> rates = new ArrayList<>();

    @NoArgsConstructor
    @Data
    public static class Rate {
        private String currencyCode;
        private String currencyName;
        private Integer nominal;
        private BigDecimal value;
        private BigDecimal previous;
    }

}
