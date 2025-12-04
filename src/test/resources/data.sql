INSERT INTO students (name, furigana, nickname, mail_address, address, age, gender, remark, isDeleted)
VALUES
('田中太郎', 'たなかたろう', 'たろちゃん', 'taro@example.com', '東京都新宿区', 25, '男性', '', 0),
('佐藤花子', 'さとうはなこ', 'はなちゃん', 'hanako@example.com', '東京都中野区', 22, '女性', '', 0),
('鈴木一郎', 'すずきいちろう', 'いっちゃん', 'ichiro@example.com', '東京都豊島区', 30, '男性', '', 0),
('山田桃子', 'やまだももこ', 'もも', 'momo@example.com', '東京都板橋区', 27, '女性', '', 0),
('高橋健', 'たかはしけん', 'けんけん', 'ken@example.com', '東京都世田谷区', 29, '男性', '', 0);

INSERT INTO courses (course_name, description)
VALUES
('Java基礎', 'Javaの基本文法と開発基礎を学ぶコース'),
('AWSクラウド基礎', 'AWSの主要サービスを学習するコース');

INSERT INTO students_courses (student_id, course_id, start_date, end_date)
VALUES
(1, 1, '2024-01-01', '2024-06-30'),
(1, 2, '2024-01-01', '2024-06-30'),
(2, 2, '2024-01-01', '2024-06-30'),
(3, 1, '2024-01-01', '2024-06-30'),
(3, 2, '2024-01-01', '2024-06-30'),
(4, 1, '2024-01-01', '2024-06-30'),
(5, 2, '2024-01-01', '2024-06-30');