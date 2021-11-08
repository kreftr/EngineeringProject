CREATE TABLE photo(

    id      BIGINT NOT NULL,
    url     VARCHAR UNIQUE NOT NULL,

    PRIMARY KEY (id)
);