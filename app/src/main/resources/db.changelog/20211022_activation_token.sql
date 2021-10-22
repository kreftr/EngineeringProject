CREATE TABLE activation_token(

    id          BIGINT NOT NULL,
    token       VARCHAR UNIQUE NOT NULL,
    created     TIMESTAMP,
    expired     TIMESTAMP,
    confirmed   TIMESTAMP,
    user_id     BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES "user"
);