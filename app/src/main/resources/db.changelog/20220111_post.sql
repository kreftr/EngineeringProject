CREATE TABLE post
(
    id    BIGINT    NOT NULL,
    title VARCHAR   NOT NULL,
    userr BIGINT    NOT NULL,
    datee TIMESTAMP NOT NULL,
    text  VARCHAR   NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (userr) REFERENCES "user"
);