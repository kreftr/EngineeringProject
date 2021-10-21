CREATE TABLE "user"(
                       id               BIGINT NOT NULL,
                       username         VARCHAR UNIQUE NOT NULL,
                       email            VARCHAR UNIQUE NOT NULL,
                       password         VARCHAR NOT NULL,
                       creation_date    TIMESTAMP NOT NULL,
                       user_role        VARCHAR NOT NULL,
                       name             VARCHAR,
                       surname          VARCHAR,
                       locked           BOOLEAN NOT NULL,
                       enabled          BOOLEAN NOT NULL,

                       PRIMARY KEY (id)
);