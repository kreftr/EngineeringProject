CREATE TABLE timestamp(

     id               BIGINT NOT NULL,
     description      VARCHAR,
     time_start       DATE NOT NULL,
     time_end         DATE NOT NULL,
     project_name     VARCHAR NOT NULL,

     participant_id   BIGINT NOT NULL,

     FOREIGN KEY (participant_id) REFERENCES participant(id),
     PRIMARY KEY (id)
)