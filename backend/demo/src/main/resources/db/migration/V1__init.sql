CREATE TABLE employee (
        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        phone VARCHAR(255) NOT NULL
);

CREATE TABLE users (
       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       password VARCHAR(50) NOT NULL,
       email VARCHAR(255) NOT NULL,
       role VARCHAR(50) NOT NULL,
       employee_id INT UNIQUE,
       created_at TIMESTAMP NOT NULL,

       CONSTRAINT fk_user_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE project (
       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       name VARCHAR(50) NOT NULL,
       description VARCHAR(255) NOT NULL
);

INSERT INTO project (name, description) values ('Test name', 'test description');

CREATE TABLE shifts (
       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       shift_date DATE NOT NULL,
       start_time TIME NOT NULL,
       end_time TIME NOT NULL,
       project_id INT,

       CONSTRAINT fk_shift_project FOREIGN KEY (project_id) REFERENCES project(id)
);

CREATE TABLE shift_employee (
      shift_id INT NOT NULL,
      employee_id INT NOT NULL,
      PRIMARY KEY (shift_id, employee_id),
      CONSTRAINT fk_shift_employee_shift FOREIGN KEY (shift_id) REFERENCES shifts(id),
      CONSTRAINT fk_shift_employee_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);