mysql> SELECT*FROM courses;
+-----------+-------------+------------------------------------------+
| course_ID | course_name | description                              |
+-----------+-------------+------------------------------------------+
|         1 | JAVA_course | JAVAの基礎について学ぶコース             |
|         2 | AWS_course  | AWSの基礎について学ぶコース              |
+-----------+-------------+------------------------------------------+
2 rows in set (0.00 sec)

mysql> DESC courses;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| course_ID   | int          | NO   | PRI | NULL    | auto_increment |
| course_name | varchar(100) | NO   |     | NULL    |                |
| description | text         | YES  |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
3 rows in set (0.00 sec)

mysql> SHOW CREATE TABLE students_courses\G
*************************** 1. row ***************************
       Table: students_courses
Create Table: CREATE TABLE `students_courses` (
  `course_ID` int NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `student_ID` int NOT NULL,
  PRIMARY KEY (`course_ID`,`student_ID`),
  KEY `fk_student` (`student_ID`),
  CONSTRAINT `fk_course` FOREIGN KEY (`course_ID`) REFERENCES `courses` (`course_ID`),
  CONSTRAINT `fk_student` FOREIGN KEY (`student_ID`) REFERENCES `students` (`student_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
1 row in set (0.00 sec)
mysql> SELECT*FROM students_courses;
+------------+-----------+------------+------------+
| student_ID | course_ID | start_date | end_date   |
+------------+-----------+------------+------------+
|          2 |         1 | 2025-09-11 | 2025-12-11 |
|          3 |         1 | 2025-09-30 | 2025-12-30 |
|          4 |         1 | 2025-06-20 | 2025-09-20 |
|          1 |         2 | 2025-04-20 | 2025-10-20 |
|          2 |         2 | 2025-12-19 | 2026-06-19 |
|          3 |         2 | 2025-09-30 | 2026-03-11 |
+------------+-----------+------------+------------+
6 rows in set (0.00 sec)
