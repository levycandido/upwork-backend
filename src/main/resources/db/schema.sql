-- Drop tables if they exist
DROP TABLE IF EXISTS block CASCADE;
DROP TABLE IF EXISTS SHORT_URL CASCADE;
DROP TABLE IF EXISTS record CASCADE;
DROP TABLE IF EXISTS operation CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "url" CASCADE;
DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS guest CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS place CASCADE;

CREATE TABLE "url"
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_url VARCHAR(255) NOT NULL,
    short_url    VARCHAR(50)  NOT NULL UNIQUE,
    expiry_date  TIMESTAMP NOT NULL,
    last_access_time  TIMESTAMP NOT NULL
);

-- Create table `user`
CREATE TABLE "user"
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status   VARCHAR(255) NOT NULL
);

-- Create table `operation`
CREATE TABLE operation
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    cost NUMERIC      NOT NULL
);

-- Create table `record`
CREATE TABLE record
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation_id       BIGINT  NOT NULL,
    user_id            BIGINT  NOT NULL,
    amount             DECIMAL NOT NULL,
    user_balance       DECIMAL NOT NULL,
    operation_response VARCHAR,
    date               DATE    NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT fk_operation FOREIGN KEY (operation_id) REFERENCES operation (id)
);


-- Create table `customer`
CREATE TABLE customer
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL
);

-- Create table `place`
CREATE TABLE place
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

-- Create table `person`
CREATE TABLE person
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (id) REFERENCES customer (id)
);

-- Create table `guest`
CREATE TABLE guest
(
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_guest FOREIGN KEY (id) REFERENCES person (id)
);

-- Create table `employee`
CREATE TABLE employee
(
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_employee FOREIGN KEY (id) REFERENCES person (id)
);

-- Create table `booking`
CREATE TABLE booking
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date DATE         NOT NULL,
    end_date   DATE         NOT NULL,
    status     VARCHAR(255) NOT NULL,
    guest_id   BIGINT       NOT NULL,
    place_id   BIGINT       NOT NULL,
    CONSTRAINT fk_booking_guest FOREIGN KEY (guest_id) REFERENCES person (id),
    CONSTRAINT fk_booking_place FOREIGN KEY (place_id) REFERENCES place (id)
);


