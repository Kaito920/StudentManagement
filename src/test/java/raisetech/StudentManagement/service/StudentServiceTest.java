package raisetech.StudentManagement.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.controller.request.UpdateStudentFieldRequest;
import raisetech.StudentManagement.controller.request.UpdateStudentsCoursesRequest;
import raisetech.StudentManagement.data.Course;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  @Spy
  @InjectMocks
  private StudentService sut;

  /**
   * 受講生情報一覧検索のテスト - リポジトリとコンバーターの処理が適切の行われていることを確認する
   */
  @Test
  void getStudentDetail_リポジトリとコンバーターの処理が適切の行われていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<Course> courseList = new ArrayList<>();
    when(repository.searchStudent()).thenReturn(studentList);
    when(repository.searchStudentsCourses()).thenReturn(studentCourseList);
    when(repository.searchCourse()).thenReturn(courseList);

    List<StudentDetail> actual = sut.getStudentDetail();

    verify(repository, times(1)).searchStudent();
    verify(repository, times(1)).searchStudentsCourses();
    verify(repository, times(1)).searchCourse();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList, courseList);
  }

  /**
   * 受講生情報単体検索　 - リポジトリとコンバーターの呼び出しが適切であることを確認する - 内部メソッド（コース情報検索の正常系テスト）はここでは簡略し個別でテストを行う
   */
  @Test
  void getStudentDetail_studentId_リポジトリとコンバーターの呼び出しが適切であること() {
    int studentId = 1;

    Student student = new Student();
    List<StudentCourse> studentCourses = new ArrayList<>();
    List<Course> courses = new ArrayList<>();

    when(repository.searchStudentById(studentId)).thenReturn(student);
    when(repository.searchStudentsCoursesById(studentId)).thenReturn(studentCourses);
    doReturn(courses).when(sut).searchCourseByStudentId(studentId);

    StudentDetail actual = sut.getStudentDetail(studentId);

    verify(repository, times(1)).searchStudentById(studentId);
    verify(repository, times(1)).searchStudentsCoursesById(studentId);
    verify(converter, times(1)).convertStudentDetails(student, studentCourses, courses);
  }

  /**
   * コース情報検索の正常系テスト -studentIdに紐づくStudentCourseが1件以上存在する場合
   */
  @Test
  void searchCourseByStudentId_正常系_リポジトリの呼び出しが適切であること() {
    int studentId = 1;

    StudentCourse sc = new StudentCourse();
    sc.setCourseId(10);
    List<StudentCourse> studentCourses = new ArrayList<>();
    studentCourses.add(sc);

    List<Course> expectedCourses = new ArrayList<>();
    expectedCourses.add(new Course());

    when(repository.searchStudentsCoursesById(studentId)).thenReturn(studentCourses);
    when(repository.searchCoursesById(anyList())).thenReturn(expectedCourses);

    List<Course> actual = sut.searchCourseByStudentId(studentId);

    verify(repository, times(1)).searchStudentsCoursesById(studentId);
    verify(repository, times(1)).searchCoursesById(anyList());

    assertEquals(expectedCourses, actual);
  }

  /**
   * コース情報検索の境界系テスト - studentIdに紐づくStudentCourseが存在しない場合
   */
  @Test
  void searchCourseByStudentId_境界系_studentCourseが空の場合() {
    int studentId = 1;

    List<StudentCourse> studentCourses = new ArrayList<>();

    when(repository.searchStudentsCoursesById(studentId)).thenReturn(studentCourses);

    List<Course> actual = sut.searchCourseByStudentId(studentId);

    verify(repository, times(1)).searchStudentsCoursesById(studentId);
    verify(repository, times(0)).searchCoursesById(anyList());

    assertTrue(actual.isEmpty(), "受講コースがない場合は空リストを返す");

  }

  /**
   * 受講生新規登録のテスト - リポジトリとコンバーターの呼び出しが適切であることを確認する
   */
  @Test
  void registerStudent_リポジトリとコンバーターの呼び出しが適切であること() {
    StudentDetail studentDetail = new StudentDetail();
    Student student = new Student();

    when(converter.convertToStudent(studentDetail)).thenReturn(student);
    doNothing().when(repository).registerStudent(any(Student.class));

    StudentDetail actual = sut.registerStudent(studentDetail);

    verify(converter, times(1)).convertToStudent(studentDetail);
    verify(repository, times(1)).registerStudent(student);

    assertSame(studentDetail, actual, "戻り値は引数のStudentDetailインスタンスと同一であるべき");
    assertEquals(student, actual.getStudent(),
        "StudentDetailに変換後のStudentが設定されているべき");
  }

  /**
   * 新規コース登録のテスト
   * - リポジトリとコンバーターの呼び出しが適切であることを確認する
   * - getStudentDetailはまた別でテストを行う
   */
  @Test
  void registerCourse_依存関係の確認() {
    Student student = new Student();
    student.setStudentId(1);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    List<StudentCourse> studentCourse = new ArrayList<>();
    StudentCourse sc = new StudentCourse();
    studentCourse.add(sc);

    when(converter.convertToStudentsCourses(studentDetail)).thenReturn(studentCourse);
    doNothing().when(repository).registerCourse(any(StudentCourse.class));
    StudentDetail expectedStudentDetail = new StudentDetail();
    doReturn(expectedStudentDetail).when(sut).getStudentDetail(1);

    StudentDetail actual = sut.registerCourse(studentDetail);

    verify(converter, times(1)).convertToStudentsCourses(studentDetail);
    verify(repository, times(1)).registerCourse(any(StudentCourse.class));
    verify(sut, times(1)).getStudentDetail(1);

    assertSame(expectedStudentDetail, actual, "戻り値は getStudentDetail() の戻り値と同一であるべき");
  }

  /**
   * 受講生情報更新の正常系テスト
   * - 更新対象が文字列
   * - 代表としてnicknameでテスト
   */
  @Test
  void updateStudentField_正常系_文字列_依存関係確認(){
    int studentId = 1;
    String nickname = "タロウ";
    UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
    request.setStudentId(studentId);
    request.setField("nickname");
    request.setValue(nickname);

    StudentDetail expectedStudentDetail = new StudentDetail();
    doReturn(expectedStudentDetail).when(sut).getStudentDetail(studentId);

    StudentDetail actual = sut.updateStudentField(request);

    verify(repository,times(1)).updateStudentNickname(studentId,nickname);
    verify(sut,times(1)).getStudentDetail(studentId);
    assertSame(expectedStudentDetail,actual,"戻り値は getStudentDetail() の戻り値と同一であるべき");
  }

  /**
   * 受講生情報更新の正常系テスト
   * - 更新対象が数値
   * - 代表としてageでテスト
   */
  @Test
  void updateStudentField_正常系_数値_依存関係確認(){
    int age = 20;
    int studentId = 1;
    UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
    request.setStudentId(studentId);
    request.setField("age");
    request.setValue(String.valueOf(age));

    StudentDetail expectedStudentDetail = new StudentDetail();
    doReturn(expectedStudentDetail).when(sut).getStudentDetail(studentId);

    StudentDetail actual = sut.updateStudentField(request);

    verify(repository,times(1)).updateStudentAge(studentId,age);
    verify(sut,times(1)).getStudentDetail(studentId);
    assertSame(expectedStudentDetail,actual,"戻り値は getStudentDetail() の戻り値と同一であるべき");
  }

  /**
   * 受講生情報更新の異常系テスト
   * - 更新対象が数値
   * - 代表としてageでテスト
   */
  @Test
  void updateStudentField_異常系_数値_依存関係確認(){
    String age = "abc";
    int studentId = 1;
    UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
    request.setStudentId(studentId);
    request.setField("age");
    request.setValue(age);

    assertThrows(NumberFormatException.class,
        () -> sut.updateStudentField(request),
        "数値フィールドに数値でない値をセットした場合は例外が発生するべき");

  }

  /**
   * 受講生情報更新の正常系テスト
   * - 更新対象が特殊バリデーションフィールドを持つ
   * - genderでテスト
   */
  @Test
  void updateStudentField_正常系_gender_依存関係確認(){
    int studentId =1;
    String gender = "男性";
    UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
    request.setStudentId(studentId);
    request.setField("gender");
    request.setValue(gender);

    StudentDetail expectedStudentDetail = new StudentDetail();
    doReturn(expectedStudentDetail).when(sut).getStudentDetail(studentId);

    StudentDetail actual = sut.updateStudentField(request);

    verify(repository,times(1)).updateStudentGender(studentId,gender);
    verify(sut,times(1)).getStudentDetail(studentId);
    assertSame(expectedStudentDetail,actual,"戻り値は getStudentDetail() の戻り値と同一であるべき");
  }

  /**
   * 受講生情報更新異常系のテスト
   * -更新対象が特殊バリデーションフィールドを持つ
   * - genderでテスト
   */
  @Test
  void updateStudentField_異常系_gender_依存関係確認(){
    int studentId =1;
    String[] invalidGenders = {"","男","だんせい","men",null};
    for (String gender: invalidGenders) {
      UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
      request.setStudentId(studentId);
      request.setField("gender");
      request.setValue(gender);

      assertThrows(IllegalArgumentException.class,
          ()-> sut.updateStudentField(request),
          "不正な gender 値 (" + gender + ") の場合は例外が発生するべき");
    }
  }

  /**
   * 受講生情報更新のテスト
   * -不正なフィールドの場合
   */
  @Test
  void updateStudentField_不正なフィールド名(){
    int studentId =1;
    String nickname = "タロウ";
    UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
    request.setStudentId(studentId);
    request.setField("password");
    request.setValue(nickname);

    assertThrows(IllegalArgumentException.class,
        ()-> sut.updateStudentField(request),
        "不正なフィールド名の場合は例外が発生するべき");
  }

  /**
   * 受講コース更新の正常系テスト
   * - 新規コース追加
   */
  @Test
  void updateCourse_正常系_コース追加_依存関係関係(){
    int studentId = 1;
    List<Integer> newCourseIds = List.of(1,2);
    List<Course> currentCourse = new ArrayList<>();
    StudentDetail expectedStudentDetail = new StudentDetail();

    doReturn(currentCourse).when(sut).searchCourseByStudentId(studentId);
    doReturn(expectedStudentDetail).when(sut).getStudentDetail(studentId);

    UpdateStudentsCoursesRequest request = new UpdateStudentsCoursesRequest();
    request.setStudentId(studentId);
    request.setCourseIds(newCourseIds);

    StudentDetail actual = sut.updateCourse(request);

    verify(repository, times(newCourseIds.size())).registerCourse(any(StudentCourse.class));
    verify(repository,never()).deleteStudentCourse(anyInt(),anyInt());
    verify(sut, times(1)).getStudentDetail(studentId);
    assertSame(expectedStudentDetail, actual);
  }

  /**
   * 受講コース更新の正常系テスト
   * - 既存受講コース削除
   */
  @Test
  void updateCourse_正常系_コース削除_依存関係関係(){
    int studentId = 1;
    List<Course> currentCourse = new ArrayList<>();
    Course course1 = new Course();
    Course course2 = new Course();
    course1.setCourseId(1);
    course2.setCourseId(2);
    currentCourse.add(course1);
    currentCourse.add(course2);

    List<Integer> newCourseIds = new ArrayList<>();
    StudentDetail expectedStudentDetail = new StudentDetail();

    doReturn(currentCourse).when(sut).searchCourseByStudentId(studentId);
    doReturn(expectedStudentDetail).when(sut).getStudentDetail(studentId);

    UpdateStudentsCoursesRequest request = new UpdateStudentsCoursesRequest();
    request.setStudentId(studentId);
    request.setCourseIds(newCourseIds);

    StudentDetail actual = sut.updateCourse(request);

    verify(repository, never()).registerCourse(any(StudentCourse.class));
    verify(repository, times(1)).deleteStudentCourse(studentId, course1.getCourseId());
    verify(repository, times(1)).deleteStudentCourse(studentId, course2.getCourseId());
    verify(sut, times(1)).getStudentDetail(studentId);
    assertSame(expectedStudentDetail, actual);
  }


  /**
   * 受講コース更新の正常系テスト
   * - 新規コース追加、既存受講コース削除同時
   */
  @Test
  void updateCourse_正常系_コース追加削除同時_依存関係関係(){
  }

  /**
   * 受講コース更新の正常系テスト
   * - 差分なし（何も実行しない場合）
   */
  @Test
  void updateCourse_正常系_差分なし_依存関係関係(){
  }

  /**
   * 受講コース更新の異常系テスト
   * - 存在しないstudentIdが指定された場合
   */
  @Test
  void updateCourse_異常系_studentId_依存関係関係(){
  }

  /**
   * 受講コース更新の異常系テスト
   * - courseIdがnullまたは空リスト
   */
  @Test
  void updateCourse_異常系_courseId_依存関係関係(){
  }




}

