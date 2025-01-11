-- V202401111042__create_car_brand_table.sql

-- Create the CarBrand table
CREATE TABLE car_brand
(
    id                BIGSERIAL PRIMARY KEY,
    uuid                UUID UNIQUE DEFAULT gen_random_uuid(),
    name              VARCHAR(100) NOT NULL UNIQUE,
    country_of_origin VARCHAR(100) NOT NULL,
    founded_year      INT,
    created_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- Index for optimized querying by name
CREATE INDEX idx_car_brand_name ON car_brand (name);

-- Inserting a large dataset of car brands in order to have pagination
INSERT INTO car_brand (name, country_of_origin, founded_year)
VALUES ('Toyota', 'Japan', 1937),
       ('Ford', 'United States', 1903),
       ('Volkswagen', 'Germany', 1937),
       ('Honda', 'Japan', 1948),
       ('Chevrolet', 'United States', 1911),
       ('Mercedes-Benz', 'Germany', 1926),
       ('BMW', 'Germany', 1916),
       ('Nissan', 'Japan', 1933),
       ('Hyundai', 'South Korea', 1967),
       ('Renault', 'France', 1899),
       ('Peugeot', 'France', 1810),
       ('Fiat', 'Italy', 1899),
       ('Kia', 'South Korea', 1944),
       ('Land Rover', 'United Kingdom', 1948),
       ('Jaguar', 'United Kingdom', 1922),
       ('Porsche', 'Germany', 1931),
       ('Ferrari', 'Italy', 1939),
       ('Maserati', 'Italy', 1914),
       ('Lamborghini', 'Italy', 1963),
       ('Bugatti', 'France', 1909),
       ('Aston Martin', 'United Kingdom', 1913),
       ('Bentley', 'United Kingdom', 1919),
       ('Rolls-Royce', 'United Kingdom', 1906),
       ('Subaru', 'Japan', 1953),
       ('Mazda', 'Japan', 1920),
       ('Suzuki', 'Japan', 1909),
       ('Mitsubishi', 'Japan', 1870),
       ('Jeep', 'United States', 1941),
       ('Chrysler', 'United States', 1925),
       ('Dodge', 'United States', 1900),
       ('Ram', 'United States', 2010),
       ('Tesla', 'United States', 2003),
       ('Lucid', 'United States', 2007),
       ('Rivian', 'United States', 2009),
       ('Polestar', 'Sweden', 1996),
       ('Volvo', 'Sweden', 1927),
       ('Skoda', 'Czech Republic', 1895),
       ('SEAT', 'Spain', 1950),
       ('Saab', 'Sweden', 1937),
       ('Opel', 'Germany', 1862),
       ('Citroen', 'France', 1919),
       ('Dacia', 'Romania', 1966),
       ('Lancia', 'Italy', 1906),
       ('Alfa Romeo', 'Italy', 1910),
       ('Genesis', 'South Korea', 2015),
       ('Great Wall', 'China', 1984),
       ('Chery', 'China', 1997),
       ('Geely', 'China', 1986),
       ('BYD', 'China', 1995),
       ('Haval', 'China', 2013),
       ('Perodua', 'Malaysia', 1992),
       ('Proton', 'Malaysia', 1983);
