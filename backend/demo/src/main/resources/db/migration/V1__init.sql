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

CREATE TABLE project (
        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        name VARCHAR(50) NOT NULL,
        description VARCHAR(255) NOT NULL,
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
       project_id INT UNIQUE ,
       created_at TIMESTAMP NOT NULL,
       updated_at TIMESTAMP NOT NULL,
       deleted BOOLEAN not null default false,

       CONSTRAINT fk_user_employee FOREIGN KEY (employee_id) REFERENCES employee(id),
       CONSTRAINT fk_user_project FOREIGN KEY (project_id) REFERENCES project(id)
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

CREATE TABLE refresh_token(
    id SERIAL PRIMARY KEY ,
    token VARCHAR(128) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL ,
    CONSTRAINT FK_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT refresh_token_UNIQUE UNIQUE (user_id, id)
);