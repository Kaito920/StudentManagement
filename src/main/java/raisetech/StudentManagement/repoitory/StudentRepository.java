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

/**
 * 受講生情報、受講生コース情報、コース情報の３つのテーブルと紐づくRepository
 */
@Mapper

public interface StudentRepository {

  /**
   * 受講生一覧検索 全件検索を行うため条件指定は行いません。
   *
   * @return 受講生一覧（全件）
   */
  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  /**
   * 受講生単一検索 studentIdに紐づく任意の受講生情報を取得します。
   *
   * @param studentId 　受講生ID
   * @return 受講生情報
   */
  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student searchStudentById(@Param("studentId") int studentId);

  /**
   * コース情報一覧検索　全件検索を行うため条件指定は行いません。
   *
   * @return コース情報一覧（全件）
   */
  @Select("SELECT * FROM courses")
  List<Courses> searchCourse();

  /**
   * 受講情報一覧検索　全件検索を行うため条件指定は行いません。
   *
   * @return 受講情報一覧（全件）
   */
  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourse();

  /**
   * 新規受講生登録　入力されたデータを持つ受講生を新たに登録します。 受講生IDは自動採番されます。
   *
   * @param student 受講生の持つフィールド情報
   */
  @Insert(
      "INSERT INTO students(name,furigana,nickname,mail_address,address,age,gender,remark,isDeleted) "
          + "VALUES(#{name},#{furigana},#{nickname},#{mailAddress},#{address},#{age},#{gender},#{remark},false)")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  /**
   * 新規コース登録　選択された受講生に対し新たに受講コースを追加します。
   *
   * @param studentsCourses 受講コース情報
   */
  @Insert("INSERT INTO students_Courses(student_ID,course_ID,start_date,end_date)"
      + "VALUES(#{studentId},#{courseId},#{startDate},#{endDate})")
  void registerCourse(StudentsCourses studentsCourses);

  /**
   * 受講コース情報検索　選択された受講生に紐づく受講コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchStudentCourseById(@Param("studentId") int studentId);

  /**
   * コース情報検索　選択されたコースIDと一致するコース情報を検索する。
   *
   * @param courseIds 検索対象のコースID
   * @return コースIDに一致するコース情報
   */
  @Select({
      "<script>",
      "SELECT * FROM courses WHERE course_id IN",
      "<foreach item='id' collection='courseIds' open='(' separator=',' close=')'>",
      "#{id}",
      "</foreach>",
      "</script>"
  })
  List<Courses> searchCoursesById(@Param("courseIds") List<Integer> courseIds);


  /**
   * 受講コース情報削除　選択された受講生ID、コースIDの組み合わせが一致する受講コース情報を削除します。
   *
   * @param studentId 選択された受講生ID
   * @param courseId  選択されたコースID
   */
  @Delete("DELETE FROM students_courses WHERE student_id = #{studentId} AND course_id = #{courseId}")
  void deleteStudentCourse(@Param("studentId") int studentId, @Param("courseId") int courseId);


  /**
   * 受講生情報更新　受講生IDから選択された受講生の名前を変更します。
   *
   * @param studentId 選択されt受講生ID
   * @param name      選択されたフィールド（ここでは名前）
   */
  @Update("UPDATE students SET name = #{value} WHERE student_id = #{studentId}")
  void updateStudentName(@Param("studentId") int studentId, @Param("value") String name);

  @Update("UPDATE students SET furigana = #{value} WHERE student_id = #{studentId}")
  void updateStudentFurigana(@Param("studentId") int studentId, @Param("value") String furigana);

  @Update("UPDATE students SET nickname = #{value} WHERE student_id = #{studentId}")
  void updateStudentNickname(@Param("studentId") int studentId, @Param("value") String nickname);

  @Update("UPDATE students SET mail_address = #{value} WHERE student_id = #{studentId}")
  void updateStudentMailAddress(@Param("studentId") int studentId,
      @Param("value") String mailAddress);

  @Update("UPDATE students SET address = #{value} WHERE student_id = #{studentId}")
  void updateStudentAddress(@Param("studentId") int studentId, @Param("value") String address);

  @Update("UPDATE students SET age = #{value} WHERE student_id = #{studentId}")
  void updateStudentAge(@Param("studentId") int studentId, @Param("value") int age);

  @Update("UPDATE students SET gender = #{value} WHERE student_id = #{studentId}")
  void updateStudentGender(@Param("studentId") int studentId, @Param("value") String gender);

  @Update("UPDATE students SET remark = #{value} WHERE student_id = #{studentId}")
  void updateStudentRemark(@Param("studentId") int studentId, @Param("value") String remark);

  /**
   * 受講生論理削除　選択された受講生IDから、その受講生の削除フラグを更新（削除・復元）します。
   *
   * @param studentId 選択された受講生ID
   * @param isDeleted 　削除フラグ
   */
  @Update("UPDATE students SET isDeleted = #{isDeleted} WHERE student_id = #{studentId}")
  void logicalDeleteStudent(@Param("studentId") int studentId,
      @Param("isDeleted") boolean isDeleted);


}


