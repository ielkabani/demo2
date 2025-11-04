-- Insert sample users
INSERT INTO users (name, email, password) VALUES
('John Doe', 'john.doe@example.com', 'password123'),
('Jane Smith', 'jane.smith@example.com', 'password456'),
('Alice Johnson', 'alice.johnson@example.com', 'password789'),
('Bob Williams', 'bob.williams@example.com', 'password101'),
('Charlie Brown', 'charlie.brown@example.com', 'password202');

-- Insert sample addresses for users (note: using explicit IDs from auto_increment)
INSERT INTO addresses (street, city, zip, state, user_id) VALUES
('123 Main St', 'New York', '10001', 'NY', (SELECT id FROM users WHERE email = 'john.doe@example.com')),
('456 Oak Ave', 'Los Angeles', '90001', 'CA', (SELECT id FROM users WHERE email = 'jane.smith@example.com')),
('789 Pine Rd', 'Chicago', '60601', 'IL', (SELECT id FROM users WHERE email = 'alice.johnson@example.com')),
('321 Elm St', 'Houston', '77001', 'TX', (SELECT id FROM users WHERE email = 'bob.williams@example.com')),
('654 Maple Dr', 'Phoenix', '85001', 'AZ', (SELECT id FROM users WHERE email = 'charlie.brown@example.com'));

