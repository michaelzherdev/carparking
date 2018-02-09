# --- First database schema

# --- !Ups
SET IGNORECASE TRUE;

CREATE TABLE car (
  id        BIGINT                  NOT NULL,
  number    VARCHAR(20)             NOT NULL,
  arrival   TIMESTAMP DEFAULT now() NOT NULL,
  departure TIMESTAMP,
  CONSTRAINT pk_car PRIMARY KEY (id)
);

CREATE SEQUENCE car_seq START WITH 1000;
CREATE INDEX ix_car_1
  ON car (number);

# --- !Downs
SET REFERENTIAL_INTEGRITY FALSE;
DROP TABLE IF EXISTS car;
SET REFERENTIAL_INTEGRITY TRUE;
DROP SEQUENCE IF EXISTS car_seq;

