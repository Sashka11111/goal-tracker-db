-- Додавання користувача
INSERT INTO Users (id_user, username, password, profile_image)
VALUES (1, 'Sashka', 'Sashka123', NULL),
       (2, 'Dashka', 'Dashka67', NULL),
       (3, 'Mashka', 'Mashka111', NULL);

-- Додавання категорії
INSERT INTO Category (id_category, name)
VALUES (1, 'Саморозвиток'),
       (2, 'Спорт'),
       (3, 'Карєра');

-- Додавання цілей
INSERT INTO Goals (id_goal, id_user, name_goal, description, id_category, start_date, end_date, status)
VALUES (1, 1, 'Прочитати 10 книг цього року', 'Поставив собі за мету прочитати щонайменше 10 книг у цьому році', 1, '2024-05-15', '2024-12-31', 'active'),
       (2, 1, 'Почати щоденні ранкові пробіжки', 'Розпочати кожен день з короткої пробіжки для здорового способу життя', 2, '2024-06-01', '2024-12-31', 'active'),
       (3, 1, 'Підвищити кваліфікацію на курсах PM', 'Закінчити курси управління проектами та отримати сертифікат', 3, '2024-06-20', '2024-12-31', 'active');

-- Додавання кроків для цілей
INSERT INTO Steps (id_step, id_goal, description)
VALUES (1, 1, 'Придбати список книг'),
       (2, 1, 'Визначити графік читання'),
       (3, 2, 'Підготувати трасу пробіжки'),
       (4, 2, 'Розпочати пробіжку вранці'),
       (5, 3, 'Зареєструватися на курси'),
       (6, 3, 'Вивчити основні теми');

-- Додавання прогресу
INSERT INTO Progress (id_progress, id_user, id_goal, status)
VALUES (1, 1, 1, 'active'),
       (2, 1, 2, 'active'),
       (3, 1, 3, 'active');
