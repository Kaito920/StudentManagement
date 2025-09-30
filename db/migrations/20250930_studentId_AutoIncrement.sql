mysql> DESC students;
+--------------+--------------+------+-----+---------+----------------+
| Field        | Type         | Null | Key | Default | Extra          |
+--------------+--------------+------+-----+---------+----------------+
| student_ID   | int          | NO   | PRI | NULL    | auto_increment |
| name         | varchar(100) | YES  |     | NULL    |                |
| furigana     | varchar(100) | YES  |     | NULL    |                |
| nickname     | varchar(100) | YES  |     | NULL    |                |
| mail_address | varchar(100) | YES  |     | NULL    |                |
| address      | varchar(200) | YES  |     | NULL    |                |
| age          | int          | YES  |     | NULL    |                |
| gender       | varchar(100) | YES  |     | NULL    |                |
| remark       | varchar(200) | YES  |     | NULL    |                |
| isDeleted    | tinyint(1)   | NO   |     | 0       |                |
+--------------+--------------+------+-----+---------+----------------+
10 rows in set (0.00 sec)

mysql>