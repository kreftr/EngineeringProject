CREATE TABLE IF NOT EXISTS "conversation"(
    id               BIGINT NOT NULL,

    first_user_id    BIGINT NOT NULL,
    second_user_id   BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (first_user_id) REFERENCES "user" (id),
    FOREIGN KEY (second_user_id) REFERENCES "user" (id)
);

CREATE TABLE IF NOT EXISTS "message"(
    id               BIGINT NOT NULL,
    content          VARCHAR,
    message_date     DATE,

    conversation_id  BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (conversation_id) REFERENCES conversation (id)
);