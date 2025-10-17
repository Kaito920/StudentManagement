package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import raisetech.StudentManagement.data.Course;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

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
  List<Student> searchStudent();

  /**
   * 受講生単一検索 studentIdに紐づく任意の受講生情報を取得します。
   *
   * @param studentId 　受講生ID
   * @return 受講生情報
   */
  Student searchStudentById(@Param("studentId") int studentId);

  /**
   * コース情報一覧検索　全件検索を行うため条件指定は行いません。
   *
   * @return コース情報一覧（全件）
   */
  List<Course> searchCourse();

  /**
   * コース情報検索　選択されたコースIDと一致するコース情報を検索する。
   *
   * @param courseIds 検索対象のコースID
   * @return コースIDに一致するコース情報
   */
  List<Course> searchCoursesById(@Param("courseIds") List<Integer> courseIds);

  /**
   * 受講コース情報一覧検索　全件検索を行うため条件指定は行いません。
   *
   * @return 受講コース情報一覧（全件）
   */
  List<StudentCourse> searchStudentsCourses();

  /**
   * 受講コース情報検索　選択された受講生に紐づく受講コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  List<StudentCourse> searchStudentsCoursesById(@Param("studentId") int studentId);

  /**
   * 新規受講生登録　入力されたデータを持つ受講生を新たに登録します。 受講生IDは自動採番されます。
   *
   * @param student 受講生の持つフィールド情報
   */
  void registerStudent(Student student);

  /**
   * 新規コース登録　選択された受講生に対し新たに受講コースを追加します。
   *
   * @param studentsCourses 受講コース情報
   */
  void registerCourse(StudentCourse studentsCourses);

  /**
   * 受講コース情報削除　選択された受講生ID、コースIDの組み合わせが一致する受講コース情報を削除します。
   *
   * @param studentId 選択された受講生ID
   * @param courseId  選択されたコースID
   */
  void deleteStudentCourse(@Param("studentId") int studentId, @Param("courseId") int courseId);


  /**
   * 受講生情報更新　受講生IDから選択された受講生の名前を変更します。
   *
   * @param studentId 選択されt受講生ID
   * @param name      選択されたフィールド（ここでは名前）
   */
  void updateStudentName(@Param("studentId") int studentId, @Param("value") String name);
  void updateStudentFurigana(@Param("studentId") int studentId, @Param("value") String furigana);
  void updateStudentNickname(@Param("studentId") int studentId, @Param("value") String nickname);
  void updateStudentMailAddress(@Param("studentId") int studentId,
      @Param("value") String mailAddress);
  void updateStudentAddress(@Param("studentId") int studentId, @Param("value") String address);
  void updateStudentAge(@Param("studentId") int studentId, @Param("value") int age);
  void updateStudentGender(@Param("studentId") int studentId, @Param("value") String gender);
  void updateStudentRemark(@Param("studentId") int studentId, @Param("value") String remark);

  /**
   * 受講生論理削除　選択された受講生IDから、その受講生の削除フラグを更新（削除・復元）します。
   *
   * @param studentId 選択された受講生ID
   * @param isDeleted 　削除フラグ
   */
  void logicalDeleteStudent(@Param("studentId") int studentId,
      @Param("isDeleted") boolean isDeleted);


}


