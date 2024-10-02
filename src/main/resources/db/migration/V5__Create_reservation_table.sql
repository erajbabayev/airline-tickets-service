-- Create Reservation table
CREATE TABLE reservation (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             reservation_code VARCHAR(255) NOT NULL,
                             reservation_time TIMESTAMP NOT NULL,
                             customer_id BIGINT,
                             CONSTRAINT fk_customer
                                 FOREIGN KEY (customer_id)
                                     REFERENCES customer(id)
                                     ON DELETE CASCADE
);

-- Create table for the relationship between reservation and seat
CREATE TABLE reservation_seats (
                                   reservation_id BIGINT,
                                   seat_id BIGINT,
                                   PRIMARY KEY (reservation_id, seat_id),
                                   CONSTRAINT fk_reservation
                                       FOREIGN KEY (reservation_id)
                                           REFERENCES reservation(id)
                                           ON DELETE CASCADE,
                                   CONSTRAINT fk_seat
                                       FOREIGN KEY (seat_id)
                                           REFERENCES seat(id)
                                           ON DELETE CASCADE
);