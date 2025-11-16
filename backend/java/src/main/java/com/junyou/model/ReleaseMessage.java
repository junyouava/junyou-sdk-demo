package com.junyou.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseMessage {
    @JsonProperty("amount")
    private String amount;
    
    @JsonProperty("ratio")
    private String ratio;
    
    @JsonProperty("level1_ratio")
    private String level1Ratio;
    
    @JsonProperty("level2_ratio")
    private String level2Ratio;
    
    @JsonProperty("level1_open_id")
    private String level1OpenId;
    
    @JsonProperty("level2_open_id")
    private String level2OpenId;
}

