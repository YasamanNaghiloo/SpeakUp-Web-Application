USE SpeakUp;

-- USERS TABLE
CREATE TABLE users (
    u_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    u_Name VARCHAR(255) NOT NULL UNIQUE,
    f_Name VARCHAR(255) NOT NULL,
    l_Name VARCHAR(255) NOT NULL,
    pwd CHAR(64) NOT NULL,
    phone_Num VARCHAR(255) NOT NULL UNIQUE
);

-- PETITIONS TABLE
CREATE TABLE petitions (
    p_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(300) NOT NULL,
    goal INT NOT NULL,
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_date DATE NOT NULL,
    category        ENUM('environmental','political','social','local','other') NOT NULL, 
	location_scope  ENUM('city','country','global') NOT NULL,
    location_country VARCHAR(80),
    location_city    VARCHAR(80),
	template_email   TEXT,
    FOREIGN KEY (user_id) REFERENCES users(u_id) ON DELETE CASCADE
);

-- SIGNEES TABLE
CREATE TABLE signees (
    petition_id INT NOT NULL,
    user_id INT NOT NULL,
    signed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (petition_id, user_id),
    FOREIGN KEY (petition_id) REFERENCES petitions(p_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(u_id) ON DELETE CASCADE
);

-- COMMENTS TABLE
CREATE TABLE comments (
    c_id INT AUTO_INCREMENT PRIMARY KEY,
    petition_id INT NOT NULL,
    user_id INT NOT NULL,
    content VARCHAR(2000) NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (petition_id) REFERENCES petitions(p_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(u_id) ON DELETE CASCADE
);

-- RESPONSIBLE PERSONS TABLE
CREATE TABLE responsible_persons (
    resp_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    title VARCHAR(120),
    email VARCHAR(120),
    phone VARCHAR(30),
    UNIQUE (email, phone)
);

-- PETITION RESPONSIBLES TABLE
CREATE TABLE petition_responsibles (
    petition_id INT NOT NULL,
    rp_id INT NOT NULL,
    PRIMARY KEY (petition_id, rp_id),
    FOREIGN KEY (petition_id) REFERENCES petitions(p_id) ON DELETE CASCADE,
    FOREIGN KEY (rp_id) REFERENCES responsible_persons(resp_id)
);

-- SAVED PETITIONS TABLE
CREATE TABLE saved_petitions (
    petition_id INT NOT NULL,
    user_id INT NOT NULL,
    saved_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (petition_id, user_id),
    FOREIGN KEY (petition_id) REFERENCES petitions(p_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(u_id)
);
