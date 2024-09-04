package com.woopaca.taximate.core.api.party.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.dto.response.ParticipatePartyResponse;
import com.woopaca.taximate.core.domain.party.service.ParticipationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/participation")
@RestController
public class ParticipationController {

    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping
    public ApiResponse<ParticipatePartyResponse> participateParty(@RequestParam("partyId") Long partyId) {
        Long participatedPartyId = participationService.participateParty(partyId);
        return ApiResults.success(new ParticipatePartyResponse(participatedPartyId));
    }
}
