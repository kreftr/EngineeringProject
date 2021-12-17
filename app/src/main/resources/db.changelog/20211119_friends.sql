CREATE TABLE IF NOT EXISTS "friend"
(
    id               BIGINT NOT NULL,
    pending          BOOL NOT NULL,

    first_user_id    BIGINT NOT NULL,
    second_user_id   BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (first_user_id) REFERENCES "user" (id),
    FOREIGN KEY (second_user_id) REFERENCES "user" (id)
);