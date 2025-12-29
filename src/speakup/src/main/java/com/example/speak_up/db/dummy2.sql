USE SpeakUp;

-- Insert Users
INSERT INTO users (email, u_Name, f_Name, l_Name, pwd, phone_Num) VALUES
('alice@example.com', 'alice123', 'Alice', 'Smith', SHA2('password1', 256), '123-456-7890'),
('bob@example.com', 'bobster', 'Bob', 'Johnson', SHA2('password2', 256), '234-567-8901'),
('charlie@example.com', 'charlieC', 'Charlie', 'Williams', SHA2('password3', 256), '345-678-9012'),
('diana@example.com', 'dianaD', 'Diana', 'Brown', SHA2('password4', 256), '456-789-0123'),
('eve@example.com', 'eve_ev', 'Eve', 'Jones', SHA2('password5', 256), '567-890-1234'),
('frank@example.com', 'frankie', 'Frank', 'Miller', SHA2('password6', 256), '678-901-2345'),
('grace@example.com', 'graceG', 'Grace', 'Davis', SHA2('password7', 256), '789-012-3456'),
('heidi@example.com', 'heidi_h', 'Heidi', 'Garcia', SHA2('password8', 256), '890-123-4567'),
('ivan@example.com', 'ivanI', 'Ivan', 'Rodriguez', SHA2('password9', 256), '901-234-5678'),
('judy@example.com', 'judyJ', 'Judy', 'Martinez', SHA2('password10', 256), '012-345-6789');

-- Insert Petitions
INSERT INTO petitions (user_id, title, description, goal, end_date, category, location_scope, location_country, location_city, template_email) VALUES
(1, 'Clean Up the River', 'We need to clean up the river from plastic and waste.', 500, '2025-12-31', 'environmental', 'city', 'USA', 'Denver', '[PetitionTitle] is supported by me: [UserName] and I am very happy to support: [PetitionTitle].'),
(2, 'Ban Single-Use Plastics', 'Urging local government to ban single-use plastic products.', 1000, '2025-11-30', 'environmental', 'country', 'USA', NULL, 'Ban plastics now!'),
(3, 'Lower Tuition Fees', 'Call to reduce university tuition fees for students.', 1500, '2025-10-20', 'social', 'global', NULL, NULL, 'Education should be affordable.'),
(4, 'Save the Rainforest', 'Protect the Amazon rainforest from illegal logging.', 2000, '2026-01-15', 'environmental', 'global', NULL, NULL, 'Rainforests are our future.'),
(5, 'Increase Public Transit Funding', 'Better funding for public transport in the city.', 800, '2025-09-30', 'local', 'city', 'Canada', 'Toronto', 'Let’s improve public transit.'),
(6, 'Stop Online Surveillance', 'Prevent government overreach and online privacy violations.', 2500, '2025-08-25', 'political', 'country', 'UK', NULL, 'Protect our digital rights.'),
(7, 'Animal Shelter Support', 'Ask the city to increase funds for animal shelters.', 600, '2025-10-10', 'social', 'city', 'USA', 'Seattle', 'Animals deserve better care.'),
(8, 'Bike Lanes in Downtown', 'Create safe bike lanes in downtown areas.', 700, '2025-07-01', 'local', 'city', 'Germany', 'Berlin', 'Promote eco-friendly transport.'),
(9, 'Mental Health Services in Schools', 'Demand better mental health support for students.', 1200, '2025-12-10', 'social', 'country', 'USA', NULL, 'Students need support.'),
(10, 'Net Neutrality Protection', 'Ensure all web traffic is treated equally.', 3000, '2026-03-01', 'political', 'global', NULL, NULL, 'Defend net neutrality.'),
(1, 'Protect Marine Life', 'Ban deep-sea trawling to preserve ocean biodiversity.', 1800, '2025-11-11', 'environmental', 'global', NULL, NULL, 'Save our oceans.'),
(2, 'Equal Pay for Equal Work', 'Push for legislation ensuring equal pay.', 2000, '2025-10-05', 'social', 'country', 'USA', NULL, 'Fair pay for all.'),
(3, 'No to Facial Recognition', 'Ban facial recognition in public spaces.', 2200, '2025-12-15', 'political', 'country', 'UK', NULL, 'Say no to surveillance.'),
(4, 'Library Expansion', 'Expand the city library and increase hours.', 400, '2025-09-20', 'local', 'city', 'USA', 'Austin', 'Support your local library.'),
(5, 'School Lunch Reform', 'Provide healthy meals for all school children.', 1600, '2026-02-01', 'social', 'country', 'USA', NULL, 'Feed our children right.');

-- Insert Responsible Persons
INSERT INTO responsible_persons (name, title, email, phone) VALUES
('John Mayor', 'City Mayor', 'mayor@city.gov', '111-222-3333'),
('Dr. Emily Clarke', 'Health Director', 'eclarke@health.org', '222-333-4444'),
('Michael Green', 'Environment Minister', 'green@enviro.gov', '333-444-5555'),
('Linda Torres', 'Education Commissioner', 'linda.torres@edu.org', '444-555-6666'),
('Thomas Lane', 'Transportation Head', 'tlane@trans.gov', '555-666-7777');

-- Link Responsible Persons to Petitions
INSERT INTO petition_responsibles (petition_id, rp_id) VALUES
(1, 3),
(3, 4),
(5, 5),
(7, 1),
(9, 2),
(15, 2);

-- Insert Signees
INSERT INTO signees (petition_id, user_id) VALUES
(1, 2),
(1, 3),
(2, 4),
(3, 5),
(4, 6),
(5, 7),
(6, 8),
(7, 9),
(8, 10),
(9, 1),
(10, 2);

-- Insert Comments
INSERT INTO comments (petition_id, user_id, content) VALUES
(1, 2, 'This is a great cause, count me in!'),
(3, 5, 'Education should be free!'),
(5, 7, 'We need better transit now.'),
(9, 1, 'Mental health is so important!'),
(10, 2, 'We must keep the internet open.');

-- Insert Saved Petitions
INSERT INTO saved_petitions (petition_id, user_id) VALUES
(1, 4),
(2, 6),
(3, 7),
(5, 8),
(6, 9),
(10, 10);
