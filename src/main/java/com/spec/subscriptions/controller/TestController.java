package com.spec.subscriptions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Tag(name = "Test", description = "Тестовый контроллер")
public class TestController {

    @Operation(summary = "Простой тестовый метод")
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Swagger!";
    }
}
