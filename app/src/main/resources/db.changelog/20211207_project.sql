CREATE TABLE file(
                     id                      BIGINT         NOT NULL,
                     file_name               VARCHAR        NOT NULL,
                     file_size               BIGINT         NOT NULL,
                     file_url                VARCHAR        NOT NULL,

                     PRIMARY KEY (id)
);

CREATE TABLE task
(
    id                      BIGINT         NOT NULL,
    task_name               VARCHAR        NOT NULL,
    task_description        VARCHAR,
    task_status             VARCHAR        NOT NULL,
    creation_date           TIMESTAMP      NOT NULL,
    expiration_date         TIMESTAMP      NOT NULL,

    PRIMARY KEY (id)

);

CREATE TABLE project(

    id                      BIGINT         NOT NULL,
    project_name            VARCHAR        NOT NULL,
    project_description     VARCHAR,
    creation_date           TIMESTAMP      NOT NULL,
    project_category        VARCHAR        NOT NULL,
    project_status          VARCHAR        NOT NULL,
    project_participant     BIGINT,
    project_creator         BIGINT         NOT NULL,
    project_file            BIGINT,
    project_task            BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (project_participant) REFERENCES "user",
    FOREIGN KEY (project_creator) REFERENCES "user",
    FOREIGN KEY (project_file) REFERENCES "file",
    FOREIGN KEY (project_task) REFERENCES "task"
);