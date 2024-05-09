-- Вставка тестових даних до таблиці "Users"
INSERT INTO Users (username, password, profile_image) VALUES
    ('john_doe', 'password123', 'john_profile.jpg'),
    ('jane_smith', 'abc123', 'jane_profile.jpg'),
    ('alex_jones', 'pass456', NULL);

-- Вставка тестових даних до таблиці "Goals"
INSERT INTO Goals (id_user, description, id_category, start_date, end_date, status) VALUES
    (1, 'Почати здоровий спосіб життя', 1, '2024-05-01', '2024-06-30', 'active'),
    (2, 'Вивчити нову мову програмування', 2, '2024-06-01', '2024-09-30', 'active'),
    (3, 'Закінчити проект з веб-розробки', 3, '2024-07-01', '2024-08-15', 'postponed');

-- Вставка тестових даних до таблиці "Category"
INSERT INTO Category (name) VALUES
    ('Фітнес'),
    ('Освіта'),
    ('Робота');

-- Вставка тестових даних до таблиці "Steps"
INSERT INTO Steps (id_goal, description) VALUES
    (1, 'Придбати абонемент в спортзал'),
    (1, 'Розпочати тренування 3 рази на тиждень'),
    (2, 'Вивчити основи програмування на Python'),
    (3, 'Закінчити дизайн сторінок');

-- Вставка тестових даних до таблиці "Progress"
INSERT INTO Progress (id_user, id_goal, status) VALUES
    (1, 1, 'active'),
    (2, 2, 'active'),
    (3, 3, 'postponed');