CREATE TABLE rating(

     id               BIGINT NOT NULL,
     value            BIGINT NOT NULL,
     profile_id       BIGINT NOT NULL,
     project_id       BIGINT NOT NULL,

     FOREIGN KEY (profile_id) REFERENCES profile (id),
     FOREIGN KEY (project_id) REFERENCES project (id),
     PRIMARY KEY (id)
);