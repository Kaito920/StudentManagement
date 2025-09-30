package raisetech.StudentManagement.repoitory;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import org.springframework.stereotype.Repository;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourse;
import raisetech.StudentManagement.domain.StudentDetail;


@Mapper

public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourse> searchStudentsCourse();

  @Insert("INSERT INTO students(student_ID,name,furigana,nickname,mail_address,address,age,gender) "
      + "VALUES(#{studentId},#{name},#{furigana},#{nickname},#{mailAddress},#{address},#{age},#{gender})")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void insertStudent(Student student);
}
