CREATE TABLE blockade (
    id               BIGINT NOT NULL,
    user_id          BIGINT NOT NULL,
    entity_type      VARCHAR NOT NULL,
    entity_id        BIGINT NOT NULL,
    creation_time    TIMESTAMP NOT NULL,
    end_time         TIMESTAMP NOT NULL,
    reasoning        VARCHAR NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);