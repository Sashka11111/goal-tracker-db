-- Створення таблиці "Users"
CREATE TABLE Users (
    id_user       INTEGER PRIMARY KEY AUTOINCREMENT
                                  NOT NULL,
    username      VARCHAR (20)    NOT NULL,
    password      VARCHAR (20)    NOT NULL,
    profile_image TEXT
    REFERENCES Goals (id_user)
);

-- Створення таблиці "Goals"
CREATE TABLE Goals (
    id_goal     [INT AUTO_INCREMENT] PRIMARY KEY,
    id_user     INT                  NOT NULL,
    description VARCHAR (255)        NOT NULL,
    id_category INT                  NOT NULL,
    start_date  DATE                 NOT NULL,
    end_date    DATE                 NOT NULL,
    status      VARCHAR (100)            CHECK (Status IN ('active', 'completed', 'postponed') )
);

-- Створення таблиці "Category"
CREATE TABLE Category (
    id_category INTEGER NOT NULL
    PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR (50)    NOT NULL
        REFERENCES Goals (id_category)
);

-- Створення таблиці "Steps"
create table Steps
(
    id_step     INTEGER
        primary key autoincrement,
    id_goal     INTEGER
        references Goals
            on update cascade on delete cascade,
    description TEXT
);

-- Створення таблиці "Progress"
CREATE TABLE Progress (
    id_progress [INT AUTO_INCREMENT] PRIMARY KEY,
    id_user     INT                  NOT NULL,
    id_goal     INT                  NOT NULL,
    status      VARCHAR (100)           CHECK (Status IN ('active', 'completed', 'postponed') ),
    FOREIGN KEY (
    id_goal
    )
    REFERENCES Goals (id_goal)
);