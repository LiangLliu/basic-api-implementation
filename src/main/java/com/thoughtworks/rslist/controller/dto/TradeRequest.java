package com.thoughtworks.rslist.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRequest {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer ranking;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Integer rsEventId;
}
