package com.woopaca.taximate.core.api.party.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.dto.request.ParticipatePartyRequest;
import com.woopaca.taximate.core.api.party.dto.response.ParticipatePartyResponse;
import com.woopaca.taximate.core.domain.party.service.ParticipationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/participation")
@RestController
public class ParticipationController {

    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping
    public ApiResponse<ParticipatePartyResponse> participateParty(@RequestBody ParticipatePartyRequest request) {
        Long participatedPartyId = participationService.participateParty(request.partyId());
        return ApiResults.success(new ParticipatePartyResponse(participatedPartyId));
    }
}
