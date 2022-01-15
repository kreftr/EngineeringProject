CREATE TABLE team_participant(

     team_id            BIGINT NOT NULL,
     participant_id     BIGINT NOT NULL,

     FOREIGN KEY (team_id) REFERENCES team(id),
     FOREIGN KEY (participant_id) REFERENCES participant(id),
     PRIMARY KEY (team_id, participant_id)
);