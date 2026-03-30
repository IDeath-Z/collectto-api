package com.collectto.api_collectto.presentation.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    @Operation(summary = "Test endpoint", description = "Returns a simple greeting message to verify that the API is working, with you are not authenticated return an error.")
    public String hello() {
        return "Hello, World!";
    }
}
