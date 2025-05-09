package com.spec.subscriptions.controller.openapi;

import com.spec.subscriptions.dto.response.TopSubscriptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Analytics", description = "Аналитика подписок")
@RequestMapping("/subscriptions/top")
public interface AnalyticsApi {

    @Operation(summary = "Получить ТОП-3 популярных подписок")
    @GetMapping
    ResponseEntity<List<TopSubscriptionDto>> getTopSubscriptions();

}
