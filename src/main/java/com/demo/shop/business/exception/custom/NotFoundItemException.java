package com.demo.shop.business.exception.custom;

import com.demo.shop.business.exception.GenericCustomException;
import org.springframework.http.HttpStatus;

public class NotFoundItemException extends GenericCustomException {
    public NotFoundItemException() {
        super(HttpStatus.NOT_FOUND, "Item not found");
    }
}
