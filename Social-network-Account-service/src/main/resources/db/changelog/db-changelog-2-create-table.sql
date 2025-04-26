CREATE TABLE IF NOT EXISTS schema_account.account (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    photo TEXT,
    about TEXT,
    city VARCHAR(255),
    country VARCHAR(255),
    token VARCHAR(255),
    status_code VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    reg_date TIMESTAMP WITH TIME ZONE NOT NULL,
    birth_date TIMESTAMP WITH TIME ZONE,
    age INTEGER NULL,
    message_permission VARCHAR(255),
    last_online_time TIMESTAMP WITH TIME ZONE,
    is_online BOOLEAN,
    is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    photo_id VARCHAR(255),
    photo_name VARCHAR(255),
    created_on TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_on TIMESTAMP WITH TIME ZONE

);

CREATE INDEX idx_account_id ON account (id);
CREATE INDEX idx_account_email ON account (email);
CREATE INDEX idx_account_phone ON account (phone);
CREATE INDEX idx_account_first_name_last_name ON account (first_name, last_name);