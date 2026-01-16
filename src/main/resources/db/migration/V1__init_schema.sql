CREATE TABLE persons
(
    id      UUID PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    age     INTEGER CHECK (age >= 0 AND age < 150)
);

CREATE TABLE pets
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    pet_type VARCHAR(100) NOT NULL,
    person_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE
);