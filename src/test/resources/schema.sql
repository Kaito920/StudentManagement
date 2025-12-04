CREATE TABLE IF NOT EXISTS students
(
    student_ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    furigana VARCHAR(100),
    nickname VARCHAR(100),
    mail_address VARCHAR(100),
    address VARCHAR(200),
    age INT,
    gender VARCHAR(100),
    remark VARCHAR(200),
    isDeleted BOOLEAN
);

CREATE TABLE IF NOT EXISTS courses
(
    course_ID INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100),
    description TEXT
);

CREATE TABLE IF NOT EXISTS students_courses
(
    student_ID INT NOT NULL,
    course_ID INT NOT NULL,
    start_date DATE,
    end_date DATE,
    PRIMARY KEY (student_ID, course_ID)
);