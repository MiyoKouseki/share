CREATE TABLE IF NOT EXISTS tasklist (
    id   VARCHAR(8)  PRIMARY KEY,
    place VARCHAR(256),
    groupname VARCHAR(256),
    deadline VARCHAR(16),
    done BOOLEAN
);