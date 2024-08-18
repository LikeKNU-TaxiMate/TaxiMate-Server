USE taxi_mate;

INSERT INTO party (title, explanation, departure_time, origin, destination,
                   origin_address, destination_address,
                   origin_location, destination_location,
                   max_participants, views, created_at, updated_at)
VALUES ('두정역 주변 ㄱㄱ', '갑시다~', '2024-08-31T23:15:20', '성정동 24-1', '성정동 1486',
        '충청남도 천안시 서북구 1공단3길 1', '충남 천안시 서북구 성정동 1486', ST_POINTFROMTEXT('POINT(36.828665 127.146126)', 4326),
        ST_POINTFROMTEXT('POINT(36.825869 127.140199)', 4326), 4, 0, '2024-08-05T00:04:21', '2024-08-05T00:04:21');

INSERT INTO party (title, explanation, departure_time, origin, destination,
                   origin_address, destination_address,
                   origin_location, destination_location,
                   max_participants, views, created_at, updated_at)
VALUES ('나는야 준석이 >.<', '갑시다~', '2024-08-31T23:15:20', '두정동 1801', '성정동 1429',
        '충남 천안시 서북구 두정동 1801', '충남 천안시 서북구 성정동 1429', ST_POINTFROMTEXT('POINT(36.834282 127.138272)', 4326),
        ST_POINTFROMTEXT('POINT(36.825598 127.143798)', 4326), 4, 0, '2024-08-05T00:06:28', '2024-08-05T00:06:28');

INSERT INTO party (title, explanation, departure_time, origin, destination,
                   origin_address, destination_address,
                   origin_location, destination_location,
                   max_participants, views, created_at, updated_at)
VALUES ('성정동에서 한성3차필하우스 ㄱ', '갑시다~', '2024-08-31T23:15:20', '성정동 1171-1', '한성3차필하우스아파트 105동',
        '충남 천안시 서북구 성정동 1171-1', '충청남도 천안시 서북구 봉정로 366', ST_POINTFROMTEXT('POINT(36.828949 127.137276)', 4326),
        ST_POINTFROMTEXT('POINT(36.834756 127.146254)', 4326), 4, 0, '2024-08-05T00:12:14', '2024-08-05T00:12:14');

INSERT INTO party (title, explanation, departure_time, origin, destination,
                   origin_address, destination_address,
                   origin_location, destination_location,
                   max_participants, views, created_at, updated_at)
VALUES ('진영렌트빌에서 성정동', '갑시다~', '2024-08-31T23:15:20', '진영렌트빌', '성정동 747', '충청남도 천안시 서북구 서부19길 41-4',
        '충남 천안시 서북구 성정동 747', ST_POINTFROMTEXT('POINT(36.824767 127.141664)', 4326),
        ST_POINTFROMTEXT('POINT(36.825133 127.145700)', 4326), 4, 0, '2024-08-05T00:12:31', '2024-08-05T00:12:31');

INSERT INTO user (email, nickname, profile_image, provider, status, created_at, updated_at)
VALUES ('test1@gmail.com', '테스트1',
        'https://nimble-diagnostic-396.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fa9ae5549-d3e8-421c-92cd-18b28a929614%2FUntitled.jpeg?table=block&id=08821681-7c20-4adc-a973-438d0efac80e&spaceId=7b71d5da-04f4-4ed6-811a-0d74031064db&width=240&userId=&cache=v2',
        'KAKAO', 'ACTIVE', NOW(), NOW());

INSERT INTO user (email, nickname, profile_image, provider, status, created_at, updated_at)
VALUES ('test2@gmail.com', '테스트2',
        'https://nimble-diagnostic-396.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fa9ae5549-d3e8-421c-92cd-18b28a929614%2FUntitled.jpeg?table=block&id=08821681-7c20-4adc-a973-438d0efac80e&spaceId=7b71d5da-04f4-4ed6-811a-0d74031064db&width=240&userId=&cache=v2',
        'KAKAO', 'ACTIVE', NOW(), NOW());

INSERT INTO participation (role, status, user_id, party_id, created_at, updated_at)
VALUES ('HOST', 'PARTICIPATING', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO participation (role, status, user_id, party_id, created_at, updated_at)
VALUES ('HOST', 'PARTICIPATING', 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO participation (role, status, user_id, party_id, created_at, updated_at)
VALUES ('HOST', 'PARTICIPATING', 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO participation (role, status, user_id, party_id, created_at, updated_at)
VALUES ('HOST', 'PARTICIPATING', 2, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
