-- Create Level and PriceTier tables
CREATE TABLE level (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       level_name VARCHAR(255),
                       rows INT,
                       seats_in_row INT
);

CREATE TABLE price_tier (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            min_seats INT,
                            max_seats INT,
                            price DECIMAL(10, 2),
                            level_id BIGINT,
                            CONSTRAINT fk_level FOREIGN KEY (level_id) REFERENCES level(id)
);