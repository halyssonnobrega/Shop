package com.demo.shop.resources.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(description = "Item model")
public class ItemRequest {

    @ApiModelProperty(value = "name", required = true)
    @NotNull(message = "Name is required")
    private String name;

    @ApiModelProperty(value = "price", required = true)
    @NotNull(message = "Price is required")
    private Double price;
}
