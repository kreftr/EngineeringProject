CREATE TABLE comment
(
    id      BIGINT      NOT NULL,
    text    VARCHAR     NOT NULL,
    datee   TIMESTAMP   NOT NULL,
    userr   BIGINT      NOT NULL,
    post_id BIGINT,
    project_id BIGINT,
    
    PRIMARY KEY (id),
    FOREIGN KEY (userr) REFERENCES "user",
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (project_id) REFERENCES project(id)
);