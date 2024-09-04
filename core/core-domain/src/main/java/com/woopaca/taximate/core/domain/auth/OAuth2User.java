package com.woopaca.taximate.core.domain.auth;

public interface OAuth2User {

    String email();

    String profileImageUrl();

    String nickname();

    String provider();
}
