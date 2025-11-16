package com.junyou.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @JsonProperty("order_no")
    private String orderNo;
    
    @JsonProperty("amount")
    private String amount;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("create_at")
    private LocalDateTime createAt;
}

