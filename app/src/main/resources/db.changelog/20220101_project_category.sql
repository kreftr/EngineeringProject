CREATE TABLE project_category(

     project_id        BIGINT NOT NULL,
     category_id       BIGINT NOT NULL,

     FOREIGN KEY (project_id) REFERENCES project (id),
     FOREIGN KEY (category_id) REFERENCES category (id),
     PRIMARY KEY (project_id, category_id)
);