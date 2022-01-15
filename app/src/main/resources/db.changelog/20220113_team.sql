CREATE TABLE team(

    id               BIGINT NOT NULL,
    name             VARCHAR NOT NULL,
    description      VARCHAR,
    project_id       BIGINT NOT NULL,

    FOREIGN KEY (project_id) REFERENCES project(id),
    PRIMARY KEY (id)
)