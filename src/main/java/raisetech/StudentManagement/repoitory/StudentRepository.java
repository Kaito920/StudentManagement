package raisetech.StudentManagement.repoitory;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;


@Mapper

public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM courses")
  List<Courses> searchCourse();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourse();

  @Insert("INSERT INTO students(name,furigana,nickname,mail_address,address,age,gender,remark,isDeleted) "
      + "VALUES(#{name},#{furigana},#{nickname},#{mailAddress},#{address},#{age},#{gender},#{remark},false)")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert("INSERT INTO students_Courses(student_ID,course_ID,start_date,end_date)"
      + "VALUES(#{studentId},#{courseId},#{startDate},#{endDate})")
  void registerCourse(StudentsCourses studentsCourses);

  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student searchStudentById(@Param("studentId") int studentId);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchStudentCourseById(@Param("studentId") int studentId);

  @Select("SELECT * FROM courses WHERE course_id = #{courseId}")
  Courses searchCourseById(@Param("courseId") int courseId);

  @Select({
      "<script>",
      "SELECT * FROM courses WHERE course_id IN",
      "<foreach item='id' collection='courseIds' open='(' separator=',' close=')'>",
      "#{id}",
      "</foreach>",
      "</script>"
  })
  List<Courses> searchCoursesById(@Param("courseIds") List<Integer> courseIds);


  @Delete("DELETE FROM students_courses WHERE student_id = #{studentId} AND course_id = #{courseId}")
  void deleteStudentCourse(@Param("studentId") int studentId, @Param("courseId") int courseId);




  @Update("UPDATE students SET name = #{value} WHERE student_id = #{studentId}")
  void updateStudentName(@Param("studentId") int studentId, @Param("value") String name);

  @Update("UPDATE students SET furigana = #{value} WHERE student_id = #{studentId}")
  void updateStudentFurigana(@Param("studentId") int studentId, @Param("value") String furigana);

  @Update("UPDATE students SET nickname = #{value} WHERE student_id = #{studentId}")
  void updateStudentNickname(@Param("studentId") int studentId, @Param("value") String nickname);

  @Update("UPDATE students SET mail_address = #{value} WHERE student_id = #{studentId}")
  void updateStudentMailAddress(@Param("studentId") int studentId, @Param("value") String mailAddress);

  @Update("UPDATE students SET address = #{value} WHERE student_id = #{studentId}")
  void updateStudentAddress(@Param("studentId") int studentId, @Param("value") String address);

  @Update("UPDATE students SET age = #{value} WHERE student_id = #{studentId}")
  void updateStudentAge(@Param("studentId") int studentId, @Param("value") int age);

  @Update("UPDATE students SET gender = #{value} WHERE student_id = #{studentId}")
  void updateStudentGender(@Param("studentId") int studentId, @Param("value") String gender);

  @Update("UPDATE students SET remark = #{value} WHERE student_id = #{studentId}")
  void updateStudentRemark(@Param("studentId") int studentId, @Param("value") String remark);

  @Update("UPDATE students SET isDeleted = #{isDeleted} WHERE student_id = #{studentId}")
  void logicalDeleteStudent(@Param("studentId") int studentId, @Param("isDeleted") boolean isDeleted);



}


