-- Insert Levels
INSERT INTO level (level_name, rows, seats_in_row) VALUES
                                                       ('First Class', 10, 4),
                                                       ('Business', 15, 6),
                                                       ('Premium Economy', 20, 6),
                                                       ('Economy', 25, 6);

-- Insert Price Tiers for each level
INSERT INTO price_tier (min_seats, max_seats, price, level_id) VALUES
                                                                   -- Price Tiers for First Class (Level ID = 1)
                                                                   (1, 10, 500, 1),
                                                                   (11, 30, 1000, 1),
                                                                   (31, 100, 1600, 1),
                                                                   -- Price Tiers for Business (Level ID = 2)
                                                                   (1, 45, 350, 2),
                                                                   (46, 100, 450, 2),
                                                                   -- Price Tiers for Premium Economy (Level ID = 3)
                                                                   (1, 40, 250, 3),
                                                                   (41, 60, 150, 3),
                                                                   (61, 120, 300, 3),
                                                                   -- Price Tiers for Economy (Level ID = 4)
                                                                   (1, 150, 200, 4);

-- Insert Seats based on Levels
INSERT INTO seat (seat_number, status, level_id, price_tier_id)
SELECT
    CONCAT(l.level_name, '-', r, '-', s) AS seat_number,
    'AVAILABLE' AS status,
    l.id AS level_id,
    pt.id AS price_tier_id
FROM level l
         JOIN price_tier pt ON l.id = pt.level_id
         JOIN (SELECT ROW_NUMBER() OVER () AS r FROM SYSTEM_RANGE(1, 1000)) AS row_range ON row_range.r <= l.rows
         JOIN (SELECT ROW_NUMBER() OVER () AS s FROM SYSTEM_RANGE(1, 1000)) AS seat_range ON seat_range.s <= l.seats_in_row
WHERE (row_range.r - 1) * l.seats_in_row + seat_range.s BETWEEN pt.min_seats AND pt.max_seats;