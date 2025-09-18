mysql> ALTER TABLE students
    -> ADD remark VARCHAR(200),
    -> ADD isDeleted BOOLEAN NOT NULL DEFAULT 0;
Query OK, 0 rows affected (0.08 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> SELECT*FROM students
    -> ;
+------------+--------------+-----------------------+-----------+-------------------------+--------------+------+--------+--------+-----------+
| student_ID | name         | furigana              | nickname  | mail_address            | address      | age  | gender | remark | isDeleted |
+------------+--------------+-----------------------+-----------+-------------------------+--------------+------+--------+--------+-----------+
| 1          | 早川健太     | ハヤカワケンタ        | ケンタ    | hayakawa.kenta@aaaa.com | 千葉県       |   25 | 男性   | NULL   |         0 |
| 2          | 木下陸       | キノシタリク          | リク      | kinoshita.riku@aaaa.com | 神奈川県     |   30 | 男性   | NULL   |         0 |
| 3          | 金井由梨     | カナイユリ            | ゆり      | kanai.yuri@aaaa.com     | 大阪府       |   24 | 女性   | NULL   |         0 |
| 4          | 齋藤花子     | サイトウハナコ        | はなこ    | hanako.saito@aaaa.com   | 東京都       |   18 | 女性   | NULL   |         0 |
+------------+--------------+-----------------------+-----------+-------------------------+--------------+------+--------+--------+-----------+
4 rows in set (0.00 sec)