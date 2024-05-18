package com.e_commerce.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInfo {

    @NotNull
    private Long itemId;

    @Min(value = 1)
    @Max(value = 999)
    private int count;
}
