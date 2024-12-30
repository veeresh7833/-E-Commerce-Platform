package com.example.productservice.exceptions;

public class ProductLimitReachedExpection extends Exception {
    public ProductLimitReachedExpection(String s) {
        super(s);
    }
}
