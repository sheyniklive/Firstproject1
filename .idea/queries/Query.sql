CREATE TABLE persons
(
    id      VARCHAR(100) NOT NULL,
    name    VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    age     INT          NOT NULL,
    pets    TEXT         NOT NULL DEFAULT '[]'
);

DROP TABLE persons