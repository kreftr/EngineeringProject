ALTER TABLE task ADD COLUMN participant_id BIGINT NULL;
ALTER TABLE task ADD CONSTRAINT task_participant_id FOREIGN KEY (participant_id) REFERENCES participant (id);

ALTER TABLE task ADD COLUMN team_id BIGINT NULL;
ALTER TABLE task ADD CONSTRAINT task_team_id FOREIGN KEY (team_id) REFERENCES team (id);

ALTER TABLE file ADD COLUMN task_id BIGINT NULL;
ALTER TABLE file ADD CONSTRAINT file_task_id FOREIGN KEY (task_id) REFERENCES task (id);