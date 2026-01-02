use dbtodo; 

CREATE TABLE IF NOT EXISTS user (
    id int AUTO_INCREMENT PRIMARY KEY,
    username varchar(50) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    email varchar(100)
);

CREATE TABLE IF NOT EXISTS todo_list (
    id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(100) NOT NULL,
    user_id int,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tasks (
    id int AUTO_INCREMENT PRIMARY KEY,
    title varchar(255) NOT NULL,
    note text,
    completed BOOLEAN DEFAULT FALSE,
    due_date date,
    list_id int,
    FOREIGN KEY (list_id) REFERENCES todo_list(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS shares (
  id int AUTO_INCREMENT PRIMARY KEY,
  share_code varchar(12) NOT NULL,
  list_id int NOT NULL,
  owner_id int NOT NULL,
  expires_at datetime NOT NULL,

  UNIQUE KEY uq_code (share_code),
  INDEX idx_shares_list_id (list_id),
  INDEX idx_shares_owner_id (owner_id),
  INDEX idx_shares_expires_at (expires_at),

  CONSTRAINT fk_shares_list
    FOREIGN KEY (list_id) REFERENCES todo_list(id)
    ON DELETE CASCADE,

  CONSTRAINT fk_shares_owner
    FOREIGN KEY (owner_id) REFERENCES user(id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS password_reset (
  id int AUTO_INCREMENT PRIMARY KEY,
  user_id int NOT NULL,
  code_hash varchar(64) NOT NULL,
  expires_at datetime NOT NULL,
  used boolean default false,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX (user_id),
  INDEX (expires_at),
  CONSTRAINT fk_reset_user
    FOREIGN KEY (user_id) REFERENCES `user`(id)
    ON DELETE CASCADE
);


  -- INSERT INTO user (username, password, email) VALUES
  -- ('pva', '123456', 'pva@gmail.com'),
  -- ('john_doe', 'password123', 'john123@gmail.com');

  -- INSERT INTO todo_list (name, user_id) VALUES
  -- ('Personal', 1),
  -- ('Work', 1),
  -- ('Groceries', 2);

  -- INSERT INTO tasks (title, note, completed, due_date, list_id) VALUES
  -- ('Buy groceries', 'Milk, Bread, Eggs', FALSE, '2024-07-01', 3),
  -- ('Finish project report', 'Due by end of the week', FALSE, '2024-06-30', 2),
  -- ('Call mom', 'Check in and see how she is doing', TRUE, NULL, 1);

select*from user;
select*from todo_list;
select*from tasks;
