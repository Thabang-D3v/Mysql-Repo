package com.formidex.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateRangeDTO {
    @ApiModelProperty(required = true)
    private LocalDate start;
    @ApiModelProperty(required = true)
    private LocalDate end;
    @ApiModelProperty(required = true)
    private String currency;
}
