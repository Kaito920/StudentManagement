//studentテーブル作成
mysql> CREATE TABLE students(
    -> student_ID VARCHAR(10) PRIMARY KEY,
    -> name VARCHAR(100),
    -> furigana VARCHAR(100),
    -> nickname VARCHAR(100),
    -> mail_address VARCHAR(100),
    -> address VARCHAR(200),
    -> age INT,
    -> gender VARCHAR(100)
    -> );
Query OK, 0 rows affected (0.04 sec)

//students_coursesテーブル作成
  student_IDは外部キー
mysql> CREATE TABLE students_courses(
    -> course_ID VARCHAR(10) PRIMARY KEY,
    -> student_ID VARCHAR(10),
    -> course_name VARCHAR(100),
    -> start_date DATE,
    -> end_date DATE,
    -> FOREIGN KEY (student_ID) REFERENCES students(student_ID) ON DELETE CASCADE ON UPDATE CASCADE
    -> );
Query OK, 0 rows affected (0.03 sec)