-- Create Seat table with foreign key references to Level, PriceTier, and Customer
CREATE TABLE seat (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      seat_number VARCHAR(255) NOT NULL,
                      status VARCHAR(255) NOT NULL,
                      level_id BIGINT NOT NULL,
                      price_tier_id BIGINT NOT NULL,
                      customer_id BIGINT,  -- Nullable field for the customer holding the seat
                      hold_time TIMESTAMP,  -- Time when the seat was held
                      FOREIGN KEY (level_id) REFERENCES level(id),
                      FOREIGN KEY (price_tier_id) REFERENCES price_tier(id),
                      FOREIGN KEY (customer_id) REFERENCES customer(id)  -- Add foreign key to customer table
);