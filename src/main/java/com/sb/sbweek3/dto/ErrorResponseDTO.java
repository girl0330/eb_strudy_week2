package com.sb.sbweek3.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDTO {
    private int statusCode;
    private String message;
    private String errorDetails;
}
