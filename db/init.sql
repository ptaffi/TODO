use dbtodo; 

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS todo_list (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    note TEXT,
    completed BOOLEAN DEFAULT FALSE,
    due_date DATE,
    list_id INT,
    FOREIGN KEY (list_id) REFERENCES todo_list(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS shares (
  id INT AUTO_INCREMENT PRIMARY KEY,
  share_code VARCHAR(12) NOT NULL,
  list_id INT NOT NULL,
  owner_id INT NOT NULL,
  expires_at DATETIME NOT NULL,

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
