package com.sb.sbweek3.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessResponseDTO {
    private int statusCode;
    private String message;
    private String redirectUrl; // 이동할 URL
}
