package com.woopaca.taximate.core.api.party.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.controller.dto.request.MapBound;
import com.woopaca.taximate.core.api.party.controller.dto.response.PartiesResponse;
import com.woopaca.taximate.core.api.party.domain.Parties;
import com.woopaca.taximate.core.api.party.service.PartyService;
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

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping
    public ApiResponse<List<PartiesResponse>> searchByMapRange(@Validated MapBound mapBound) {
        Parties partiesInRange = partyService.getPartiesInRange(mapBound);
        List<PartiesResponse> response = partiesInRange.stream()
                .map(PartiesResponse::from)
                .toList();
        return ApiResults.success(response);
    }

}
