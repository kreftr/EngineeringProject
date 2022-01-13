CREATE TABLE profile_category(

     profile_id        BIGINT NOT NULL,
     category_id       BIGINT NOT NULL,

     FOREIGN KEY (profile_id) REFERENCES profile (id),
     FOREIGN KEY (category_id) REFERENCES category (id),
     PRIMARY KEY (profile_id, category_id)
);