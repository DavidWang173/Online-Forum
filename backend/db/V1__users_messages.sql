CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       nickname VARCHAR(50),
                       avatar VARCHAR(255),
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       last_login DATETIME
);

CREATE TABLE private_messages (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  sender_id BIGINT NOT NULL,
                                  receiver_id BIGINT NOT NULL,
                                  content TEXT NOT NULL,
                                  sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  is_read BOOLEAN DEFAULT FALSE,
                                  type VARCHAR(20) DEFAULT 'TEXT',
                                  media_url TEXT,
                                  FOREIGN KEY (sender_id) REFERENCES users(id),
                                  FOREIGN KEY (receiver_id) REFERENCES users(id)
);

CREATE TABLE chat_groups (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(50) NOT NULL,
                             description TEXT,
                             created_by BIGINT NOT NULL,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE group_members (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               group_id BIGINT NOT NULL,
                               user_id BIGINT NOT NULL,
                               joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               UNIQUE (group_id, user_id),
                               FOREIGN KEY (group_id) REFERENCES chat_groups(id),
                               FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE group_messages (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                group_id BIGINT NOT NULL,
                                sender_id BIGINT NOT NULL,
                                content TEXT NOT NULL,
                                sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                type VARCHAR(20) DEFAULT 'TEXT',
                                media_url TEXT,
                                FOREIGN KEY (group_id) REFERENCES chat_groups(id),
                                FOREIGN KEY (sender_id) REFERENCES users(id)
);