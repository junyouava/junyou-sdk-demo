package com.junyou.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotBlank(message = "金额不能为空")
    @Size(max = 20, message = "金额长度不能超过20位")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "金额格式不正确，应为数字（最多两位小数）")
    private String amount;
}

