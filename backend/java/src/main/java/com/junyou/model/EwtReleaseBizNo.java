package com.junyou.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwtReleaseBizNo {
    @JsonProperty("ewt_biz_no")
    private String ewtBizNo;
    
    @JsonProperty("order_no")
    private String orderNo;
    
    @JsonProperty("create_at")
    private LocalDateTime createAt;
}

