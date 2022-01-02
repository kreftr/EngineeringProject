ALTER TABLE project ADD photo_id BIGINT;
ALTER TABLE project ADD FOREIGN KEY(photo_id) REFERENCES photo(id);

ALTER TABLE project ADD project_access VARCHAR NOT NULL;
ALTER TABLE project ADD project_introduction VARCHAR NOT NULL;
ALTER TABLE project ADD youtube_link VARCHAR;
ALTER TABLE project ADD facebook_link VARCHAR;
ALTER TABLE project ADD github_link VARCHAR;
ALTER TABLE project ADD kickstarter_link VARCHAR;

ALTER TABLE project DROP COLUMN project_category;