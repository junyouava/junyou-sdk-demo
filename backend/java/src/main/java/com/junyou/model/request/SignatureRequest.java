package com.junyou.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignatureRequest {
    @NotBlank(message = "method 不能为空")
    private String method;
    
    @NotBlank(message = "path 不能为空")
    private String path;
}

