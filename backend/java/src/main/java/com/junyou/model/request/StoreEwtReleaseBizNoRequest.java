package com.junyou.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StoreEwtReleaseBizNoRequest {
    @NotBlank(message = "业务编号不能为空")
    private String ewtBizNo;
    
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
}

