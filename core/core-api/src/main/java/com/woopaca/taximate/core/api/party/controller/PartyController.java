package com.woopaca.taximate.core.api.party.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.controller.dto.request.MapRange;
import com.woopaca.taximate.core.api.party.controller.dto.response.PartiesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/v1/parties")
@RestController
public class PartyController {

    @GetMapping
    public ApiResponse<List<PartiesResponse>> searchByMapRange(@Validated MapRange mapRange) {
        return ApiResults.success(List.of());
    }

}
