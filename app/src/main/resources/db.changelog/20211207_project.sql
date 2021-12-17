CREATE TABLE project (
    id                      BIGINT         NOT NULL,
    project_name            VARCHAR        NOT NULL,
    project_description     VARCHAR,
    creation_date           TIMESTAMP      NOT NULL,
    project_category        VARCHAR        NOT NULL,
    project_status          VARCHAR        NOT NULL,

    project_creator         BIGINT         NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (project_creator) REFERENCES "user"
);

CREATE TABLE participant (
    id                      BIGINT        NOT NULL,
    role                    VARCHAR       NOT NULL,

    project_id              BIGINT        NOT NULL,
    user_id                 BIGINT        NOT NULL,

    PRIMARY KEY (id),       FOREIGN KEY (project_id) REFERENCES "project" (id),
    FOREIGN KEY (user_id)   REFERENCES "user" (id)
);

CREATE TABLE file (
    id                      BIGINT         NOT NULL,
    file_name               VARCHAR        NOT NULL,
    file_size               BIGINT         NOT NULL,
    file_url                VARCHAR        NOT NULL,

    project_id              BIGINT         NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES "project" (id)
);

CREATE TABLE task (
    id                      BIGINT         NOT NULL,
    task_name               VARCHAR        NOT NULL,
    task_description        VARCHAR,
    task_status             VARCHAR        NOT NULL,
    creation_date           TIMESTAMP      NOT NULL,
    expiration_date         TIMESTAMP      NOT NULL,

    participant_id          BIGINT         NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (participant_id) REFERENCES "participant" (id)
);
