CREATE TABLE customer (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          phone_number VARCHAR(15)
);

-- Insert sample customers
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES
                                                                      ('John', 'Doe', 'john.doe@example.com', '123-456-7890'),
                                                                      ('Jane', 'Smith', 'jane.smith@example.com', '098-765-4321'),
                                                                      ('Robert', 'Brown', 'robert.brown@example.com', '567-890-1234'),
                                                                      ('Emily', 'Johnson', 'emily.johnson@example.com', '432-109-8765'),
                                                                      ('Michael', 'Williams', 'michael.williams@example.com', '321-654-9870');