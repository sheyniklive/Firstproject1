package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class CbrDailyResponse {

    @JsonProperty("Date")
    private OffsetDateTime date;

    @JsonProperty("PreviousDate")
    private OffsetDateTime previousDate;

    @JsonProperty("PreviousURL")
    private String previousURL;

    @JsonProperty("Timestamp")
    private OffsetDateTime timestamp;

    @JsonProperty("Valute")
    private Map<String, Valute> valute;

    @Data
    public static class Valute {

        @JsonProperty("ID")
        private String iD;

        @JsonProperty("NumCode")
        private String numCode;

        @JsonProperty("CharCode")
        private String charCode;

        @JsonProperty("Nominal")
        private Integer nominal;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Value")
        private BigDecimal value;

        @JsonProperty("Previous")
        private BigDecimal previous;
    }
}
