CREATE TABLE recovery_token(

     id          BIGINT NOT NULL,
     token       VARCHAR UNIQUE NOT NULL,
     created     TIMESTAMP,
     expired     TIMESTAMP,
     user_id     BIGINT NOT NULL,

     PRIMARY KEY (id),
     FOREIGN KEY (user_id) REFERENCES "user"
);