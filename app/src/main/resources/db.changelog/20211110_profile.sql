CREATE TABLE profile(

    id          BIGINT NOT NULL,
    name        VARCHAR NULL,
    surname     VARCHAR NULL,
    bio         TEXT NULL,
    photo_id    BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (photo_id) REFERENCES photo
);