ALTER TABLE users
	DROP COLUMN u_name,
	DROP COLUMN f_name,
    DROP COLUMN l_name,
    DROP COLUMN phone_num,
    CHANGE fName f_name VARCHAR(255) NOT NULL,
    CHANGE lName l_name VARCHAR(255) NOT NULL,
    CHANGE uName u_name VARCHAR(255) NOT NULL,
    CHANGE phoneNum phone_num VARCHAR(255) NOT NULL;