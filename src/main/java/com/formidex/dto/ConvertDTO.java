package com.formidex.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConvertDTO {
    @ApiModelProperty(required = true)
    private LocalDate date;
    @ApiModelProperty(required = true)
    private String source;
    @ApiModelProperty(required = true)
    private String destination;
    private double amount;
}
