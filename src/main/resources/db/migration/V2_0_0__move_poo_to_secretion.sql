CREATE SEQUENCE IF NOT EXISTS secretion_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE secretion (
    id BIGINT NOT NULL DEFAULT nextval('secretion_sequence'),
    username VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    chat_id BIGINT NOT NULL,
    secretion_type INTEGER DEFAULT 0,
    CONSTRAINT pk_secretion PRIMARY KEY (id)
);

INSERT INTO secretion (username, timestamp, chat_id, secretion_type)
SELECT username, timestamp, chat_id, 0
FROM poo;