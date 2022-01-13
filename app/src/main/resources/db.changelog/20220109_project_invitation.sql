CREATE TABLE project_invitation(

    id                 BIGINT NOT NULL,
    project_id         BIGINT NOT NULL,
    receiver_id          BIGINT NOT NULL,

    FOREIGN KEY (project_id) REFERENCES project (id),
    FOREIGN KEY (receiver_id) REFERENCES "user" (id),
    PRIMARY KEY (id)
);