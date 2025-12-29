-- Create the user
CREATE USER 'yourUsername'@'localhost' IDENTIFIED BY 'yourPassword';

-- Grant all privileges on the 'speakup' database
GRANT ALL PRIVILEGES ON speakup.* TO 'yourUsername'@'localhost';

-- Apply the changes
FLUSH PRIVILEGES;