USE SpeakUp;

-- Insert dummy users
INSERT INTO users (email, u_Name, f_Name, l_Name, pwd, phoneNum) VALUES
('alice@example.com', 'alice123', 'Alice', 'Johnson', SHA2('password1', 256), '1234567890'),
('bob@example.com',   'bobster',  'Bob',   'Smith',   SHA2('password2', 256), '0987654321'),
('carol@example.com', 'carol98',  'Carol', 'White',   SHA2('password3', 256), '5678901234');

-- Insert dummy petitions with new columns
INSERT INTO petitions (
    user_id, title, description, goal, end_date, category, location_scope,
    location_country, location_city, template_email
) VALUES
(1, 'Save the Local Library',
    'We want to keep the local library open despite budget cuts.',
    500, DATE_ADD(CURDATE(), INTERVAL 30 DAY),
    'social', 'city', 'Sweden', 'Gothenburg',
    'Dear Council,\nPlease reconsider the decision to shut down our local library.'),
    
(2, 'Reduce Campus Plastic Use',
    'Let’s replace plastic with sustainable options in the cafeteria.',
    300, DATE_ADD(CURDATE(), INTERVAL 15 DAY),
    'environmental', 'country', 'Sweden', 'Lund',
    'To whom it may concern,\nWe urge you to implement greener policies on our campus.'),

(3, 'Fight for Net Neutrality',
    'Protect the open internet and prevent throttling by ISPs.',
    1000, DATE_ADD(CURDATE(), INTERVAL 45 DAY),
    'political', 'global', NULL, NULL,
    'Dear Representative,\nPlease support legislation that protects net neutrality.');



-- Insert dummy signees
INSERT INTO signees (petition_id, user_id) VALUES
(1, 2),
(1, 3),
(2, 1),
(2, 3);

-- Insert dummy comments
INSERT INTO comments (petition_id, user_id, content) VALUES
(1, 2, 'I love this idea, libraries are important for the community!'),
(1, 3, 'Please save our library!'),
(2, 1, 'Great initiative. Our campus needs this.'),
(2, 3, 'Support 100%!');

-- Insert dummy responsible persons
INSERT INTO responsible_persons (name, title, email, phone) VALUES
('Dr. Elaine Rivers', 'City Council Member', 'elaine.rivers@council.gov', '555-123-4567'),
('Mark Tran', 'Sustainability Officer', 'mark.tran@university.edu', '555-987-6543');

-- Link responsible persons to petitions
INSERT INTO petition_responsibles (petition_id, rp_id) VALUES
(1, 1),
(2, 2);

-- Insert dummy saved petitions
INSERT INTO saved_petitions (petition_id, user_id) VALUES
(1, 1),
(2, 2),
(1, 3);
