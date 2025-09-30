package raisetech.StudentManagement.repoitory;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourse;


@Mapper

public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM courses")
  List<Courses> searchCourse();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourse> searchStudentsCourse();

  @Insert("INSERT INTO students(name,furigana,nickname,mail_address,address,age,gender) "
      + "VALUES(#{name},#{furigana},#{nickname},#{mailAddress},#{address},#{age},#{gender})")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert("INSERT INTO students_Courses(student_ID,course_ID,start_date,end_date)"
      + "VALUES(#{studentId},#{courseId},#{startDate},#{endDate})")
  void registerCourse(StudentsCourse studentsCourse);
}
