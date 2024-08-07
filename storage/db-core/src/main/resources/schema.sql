CREATE TABLE IF NOT EXISTS `party`
(
    `id`                   BIGINT          NOT NULL AUTO_INCREMENT,
    `title`                VARCHAR(255)    NOT NULL,           # 팟 제목
    `explanation`          VARCHAR(255),                       # 간단 설명
    `departure_time`       DATETIME        NOT NULL,           # 출발 시간
    `origin`               VARCHAR(255)    NOT NULL,           # 출발지 이름
    `destination`          VARCHAR(255)    NOT NULL,           # 도착지 이름
    `origin_address`       VARCHAR(255)    NOT NULL,           # 출발지 주소
    `destination_address`  VARCHAR(255)    NOT NULL,           # 도착지 주소
    `origin_location`      POINT SRID 4326 NOT NULL,           # 출발지 좌표
    `destination_location` POINT SRID 4326 NOT NULL,           # 도착지 좌표
    `max_participants`     INT             NOT NULL,           # 최대 탑승 인원
    `views`                INT             NOT NULL DEFAULT 0, # 조회수
    `created_at`           DATETIME        NOT NULL,           # 생성 시간
    `updated_at`           DATETIME        NOT NULL,           # 수정 시간
    PRIMARY KEY (`id`),
    SPATIAL INDEX `sidx_origin_location` (`origin_location`),
    SPATIAL INDEX `sidx_destination_location` (`destination_location`)
);

CREATE TABLE IF NOT EXISTS `user`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `email`         VARCHAR(255) NOT NULL,
    `nickname`      VARCHAR(255) NOT NULL,
    `profile_image` VARCHAR(1000),
    `provider`      CHAR(6)      NOT NULL DEFAULT 'KAKAO',
    `status`        CHAR(8)      NOT NULL DEFAULT 'ACTIVE',
    `created_at`    DATETIME     NOT NULL,
    `updated_at`    DATETIME     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uidx_email` (`email`)
);

CREATE TABLE IF NOT EXISTS `participation`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT,
    `role`       CHAR(11) NOT NULL,
    `status`     CHAR(13) NOT NULL,
    `user_id`    BIGINT   NOT NULL,
    `party_id`   BIGINT   NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
);