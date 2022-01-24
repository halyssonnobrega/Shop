package com.demo.shop.business.exception.custom;

import com.demo.shop.business.exception.GenericCustomException;
import org.springframework.http.HttpStatus;

public class InvalidPriceException extends GenericCustomException {
    public InvalidPriceException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Price is not valid");
    }
}
