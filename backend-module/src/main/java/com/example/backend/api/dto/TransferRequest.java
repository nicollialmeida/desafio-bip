package com.example.backend.api.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class TransferRequest {

    private Long fromId;
    private Long toId;
    private BigDecimal amount;

}