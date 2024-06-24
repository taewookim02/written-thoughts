-- Create a new user with a password
CREATE USER spring_user WITH PASSWORD '1234';

-- Create a new database owned by the new user
CREATE DATABASE spring_db OWNER spring_user;

-- Grant all privileges on the database to the user
GRANT ALL PRIVILEGES ON DATABASE spring_db TO spring_user;
