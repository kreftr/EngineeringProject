ALTER TABLE project DROP CONSTRAINT project_project_participant_fkey;
ALTER TABLE project DROP COLUMN project_participant;

CREATE TABLE participant(

     id                 BIGINT NOT NULL,
     user_id            BIGINT NOT NULL,
     project_id         BIGINT NOT NULL,
     participant_role   VARCHAR NOT NULL,

     FOREIGN KEY (user_id) REFERENCES "user" (id),
     FOREIGN KEY (project_id) REFERENCES project (id),
     PRIMARY KEY (id)
);