package com.woopaca.taximate.core.api.party.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.dto.request.CreatePartyRequest;
import com.woopaca.taximate.core.api.party.dto.response.CreatePartyResponse;
import com.woopaca.taximate.core.api.party.dto.response.PartiesResponse;
import com.woopaca.taximate.core.api.party.dto.response.PartyDetailsResponse;
import com.woopaca.taximate.core.domain.party.Parties;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyDetails;
import com.woopaca.taximate.core.domain.party.model.MapBound;
import com.woopaca.taximate.core.domain.party.service.PartyService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/{partyId}")
    public ApiResponse<PartyDetailsResponse> getDetailsInformation(@PathVariable Long partyId) {
        PartyDetails partyDetails = partyService.getPartyDetails(partyId);
        PartyDetailsResponse response = PartyDetailsResponse.from(partyDetails);
        return ApiResults.success(response);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<CreatePartyResponse> createParty(@Validated @RequestBody CreatePartyRequest request) {
        Party newParty = request.toDomain();
        Long partyId = partyService.createParty(newParty);
        CreatePartyResponse response = new CreatePartyResponse(partyId);
        return ApiResults.success(response);
    }
}
