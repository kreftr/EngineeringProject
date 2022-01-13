ALTER TABLE project DROP CONSTRAINT project_project_task_fkey;
ALTER TABLE project DROP COLUMN project_task;

ALTER TABLE project DROP CONSTRAINT project_project_file_fkey;
ALTER TABLE project DROP COLUMN project_file;

ALTER TABLE file ADD COLUMN project_id BIGINT NOT NULL;
ALTER TABLE file ADD CONSTRAINT file_project_id FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE task ADD COLUMN project_id BIGINT NOT NULL;
ALTER TABLE task ADD CONSTRAINT task_project_id FOREIGN KEY (project_id) REFERENCES project (id);