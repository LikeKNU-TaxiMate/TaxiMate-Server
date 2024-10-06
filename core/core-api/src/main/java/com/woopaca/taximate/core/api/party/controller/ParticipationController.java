package com.woopaca.taximate.core.api.party.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.dto.request.LeavePartyRequest;
import com.woopaca.taximate.core.api.party.dto.request.ParticipatePartyRequest;
import com.woopaca.taximate.core.api.party.dto.response.LeavePartyResponse;
import com.woopaca.taximate.core.api.party.dto.response.ParticipatePartyResponse;
import com.woopaca.taximate.core.api.party.dto.response.ParticipatingPartyResponse;
import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import com.woopaca.taximate.core.domain.party.service.ParticipationService;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RequestMapping("/api/v1/participation")
@RestController
public class ParticipationController {

    private final ParticipationService participationService;
    private final PartyFinder partyFinder;

    public ParticipationController(ParticipationService participationService, PartyFinder partyFinder) {
        this.participationService = participationService;
        this.partyFinder = partyFinder;
    }

    @PostMapping
    public ApiResponse<ParticipatePartyResponse> participateParty(@RequestBody ParticipatePartyRequest request) {
        Long participatedPartyId = participationService.participateParty(request.partyId());
        return ApiResults.success(new ParticipatePartyResponse(participatedPartyId));
    }

    @GetMapping
    public ApiResponse<List<ParticipatingPartyResponse>> participatingPartyList(
            @RequestParam(required = false, name = "isTerminated") boolean isTerminated
    ) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        List<ParticipatingPartyResponse> response = partyFinder.findParticipatingParties(authenticatedUser)
                .stream()
                .filter(isTerminated ? Party::isTerminated : Party::isProgress)
                .sorted(Comparator.comparing(party -> party.getParticipatedAt(authenticatedUser)))
                .map(ParticipatingPartyResponse::from)
                .toList();
        return ApiResults.success(response);
    }

    @DeleteMapping
    public ApiResponse<LeavePartyResponse> leaveParty(@RequestBody LeavePartyRequest request) {
        Long leftPartyId = participationService.leaveParty(request.partyId());
        return ApiResults.success(new LeavePartyResponse(leftPartyId));
    }
}
