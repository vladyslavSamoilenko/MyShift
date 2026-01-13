CREATE TABLE employee (
        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL UNIQUE ,
        phone VARCHAR(255) NOT NULL,
        deleted BOOLEAN not null default false,
        created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL
);

CREATE TABLE users (
       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       password VARCHAR(255) NOT NULL,
       email VARCHAR(255) NOT NULL UNIQUE ,
       role VARCHAR(50) NOT NULL,
       employee_id INT UNIQUE,
       created_at TIMESTAMP NOT NULL,
       updated_at TIMESTAMP NOT NULL,
       deleted BOOLEAN not null default false,

       CONSTRAINT fk_user_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE project (
       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       name VARCHAR(50) NOT NULL,
       description VARCHAR(255) NOT NULL,
       deleted BOOLEAN not null default false,
       created_at TIMESTAMP NOT NULL,
       updated_at TIMESTAMP NOT NULL

);

CREATE TABLE shifts (
       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       shift_date DATE NOT NULL,
       start_time TIME NOT NULL,
       end_time TIME NOT NULL,
       project_id INT NOT NULL ,
       user_id INT REFERENCES users(id),
       employee_id INT NOT NULL,
       status VARCHAR(25) NOT NULL DEFAULT 'PLANNED',
       actual_start_time TIMESTAMP,
       actual_end_time TIMESTAMP,
       break_duration INT DEFAULT 0,
       break_start TIMESTAMP,
       break_end TIMESTAMP,
       duration INT DEFAULT 0,

       created_at TIMESTAMP NOT NULL,
       CONSTRAINT fk_shift_project FOREIGN KEY (project_id) REFERENCES project(id),
       CONSTRAINT fk_shift_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE project_users(
    project_id INT NOT NULL,
    user_id INT NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (project_id, user_id),
    CONSTRAINT fk_project_project FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES users(id)
)