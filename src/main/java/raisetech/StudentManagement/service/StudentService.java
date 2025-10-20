package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.controller.request.UpdateStudentFieldRequest;
import raisetech.StudentManagement.controller.request.UpdateStudentsCoursesRequest;
import raisetech.StudentManagement.data.Course;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.domain.UpdateStudentField;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービス 受講生情報の検索、登録、更新を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細一覧検索 全件検索を行うため条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<Student> searchStudentList() {
    return repository.searchStudent();

  }

  /**
   * 受講生詳細単一検索 studentIdに紐づく任意の受講生情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  public Student searchStudentById(int studentId) {
    return repository.searchStudentById(studentId);
  }

  /**
   * 受講コース情報一覧検索　全件を検索するため条件指定は行いません。
   *
   * @return 受講生コース情報一覧（全件）
   */
  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentsCourses();

  }

  /**
   * 受講コース情報検索　studentIdに紐づく任意の受講コース情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講コース情報
   */
  public List<StudentCourse> searchStudentCourseByStudentId(int studentId) {
    return repository.searchStudentsCoursesById(studentId);
  }

  /**
   * コース情報一覧検索 全件を検索するため条件指定は行いません。
   *
   * @return コース情報一覧（全件）
   */
  public List<Course> searchCourseList() {
    return repository.searchCourse();

  }

  /**
   * コース情報検索　指定された受講生IDに関連付けられた全てのコース情報を取得します。
   *
   * @param studentId 受講生ID
   * @return コース情報
   */
  public List<Course> searchCourseByStudentId(int studentId) {
    List<StudentCourse> studentsCourses = repository.searchStudentsCoursesById(studentId);
    List<Integer> courseIds = studentsCourses.stream()
        .map(StudentCourse::getCourseId)
        .toList();

    return repository.searchCoursesById(courseIds);
  }


  /**
   * 受講生情報一覧検索　全件検索を行うため条件指定は行いません。
   *
   * @return 受講生詳細情報（全件）
   */
  public List<StudentDetail> getStudentDetail() {
    List<Student> studentList = searchStudentList();
    List<StudentCourse> studentCourseList = searchStudentCourseList();
    List<Course> courseList = searchCourseList();

    return converter.convertStudentDetails(studentList, studentCourseList, courseList);
  }

  /**
   * 受講生情報検索　studentIdに紐づく任意の受講コース情報からコース情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  public StudentDetail getStudentDetail(int studentId) {
    Student student = searchStudentById(studentId);
    List<StudentCourse> studentCourseList = searchStudentCourseByStudentId(studentId);
    List<Course> courseList = searchCourseByStudentId(studentId);

    return converter.convertStudentDetails(student, studentCourseList, courseList);
  }

  /**
   * 受講生新規登録　受講生の基本情報をデータベースに登録します。
   *
   * @param studentDetail 登録する受講生の基本情報
   * @return 登録された受講生詳細情報
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = converter.convertToStudent(studentDetail);
    repository.registerStudent(student);
    studentDetail.setStudent(student);
    return studentDetail;
  }

  /**
   * 受講コース登録　studentIdに紐づく任意の受講生に１件以上のコース情報を登録します。
   *
   * @param studentDetail 登録する受講生の基本情報とコース情報
   * @return 登録された受講生詳細情報
   */
  @Transactional
  public StudentDetail registerCourse(StudentDetail studentDetail) {
    List<StudentCourse> studentCourseList = converter.convertToStudentsCourses(studentDetail);
    for (StudentCourse sc : studentCourseList) {
      repository.registerCourse(sc);
    }
    int studentId = studentDetail.getStudent().getStudentId();
    return getStudentDetail(studentId);
  }

  /**
   * 受講生情報更新　studentIdに紐づく任意の受講生情報からフィールドを指定し更新します。
   *
   * @param request 更新リクエスト（受講生ID、フィールド名、更新値）
   * @return 更新後の受講生詳細情報
   */
  @Transactional
  public StudentDetail updateStudentField(UpdateStudentFieldRequest request) {
    int studentId = request.getStudentId();
    String field = request.getField();
    String value = request.getValue();

    if (!UpdateStudentField.isValid(field)) {
      throw new IllegalArgumentException("更新可能なフィールドではありません: " + field);
    }

    if ("gender".equals(field)) {
      if (!value.matches("^(男性|女性|その他)$")) {
        throw new IllegalArgumentException("性別は「男性」「女性」「その他」のいずれかを指定してください");
      }
    }

    switch (field) {
      case "name" -> repository.updateStudentName(studentId, value);
      case "furigana" -> repository.updateStudentFurigana(studentId, value);
      case "nickname" -> repository.updateStudentNickname(studentId, value);
      case "mailAddress" -> repository.updateStudentMailAddress(studentId, value);
      case "address" -> repository.updateStudentAddress(studentId, value);
      case "age" -> repository.updateStudentAge(studentId, Integer.parseInt(value));
      case "gender" -> repository.updateStudentGender(studentId, value);
      case "remark" -> repository.updateStudentRemark(studentId, value);
      default -> throw new IllegalArgumentException("不正なフィールド名です：" + field);
    }
    return getStudentDetail(studentId);
  }

  /**
   * 受講コース更新 studentIdに紐づく任意の受講生の受講コース情報を更新します。 新規になるコースは追加、受講中のものが選択されなければ削除します。
   *
   * @param request 更新リクエスト（受講生ID、新しい受講コースID一覧）
   * @return コース更新後の受講生詳細情報
   */
  @Transactional
  public StudentDetail updateCourse(UpdateStudentsCoursesRequest request) {
    int studentId = request.getStudentId();
    List<Integer> newCourseIds = request.getCourseIds();

    //現在の受講コース取得
    List<Course> currentCourse = searchCourseByStudentId(studentId);
    List<Integer> currentCourseIds = getCourseIds(currentCourse);

    //追加する受講コース
    insertCourse(newCourseIds, currentCourseIds, studentId);

    //削除する受講コース
    deleteCourses(currentCourseIds, newCourseIds, studentId);

    return getStudentDetail(studentId);
  }

  /**
   * 現在の受講コースからコースIDを抽出するメソッド
   *
   * @param courses 現在登録されているコース
   * @return 現在登録されているコースからコースIDを抽出しリスト化したもの
   */
  private List<Integer> getCourseIds(List<Course> courses) {
    return courses.stream()
        .map(Course::getCourseId)
        .toList();
  }

  /**
   * 新たに受講コースを追加するメソッド
   *
   * @param newCourseIds     新たに登録するコースのID
   * @param currentCourseIds 現在登録されているコースのID
   * @param studentId        対象の受講生ID
   */
  private void insertCourse(List<Integer> newCourseIds, List<Integer> currentCourseIds,
      int studentId) {
    List<Integer> toInsert = newCourseIds.stream()
        .filter(id -> !currentCourseIds.contains(id))
        .toList();
    for (Integer courseId : toInsert) {
      StudentCourse newCourses = new StudentCourse();
      newCourses.setStudentId(studentId);
      newCourses.setCourseId(courseId);
      newCourses.setStartDate(LocalDate.now());
      newCourses.setEndDate(LocalDate.now().plusMonths(3));
      repository.registerCourse(newCourses);
    }
  }

  /**
   * コースリストのうち、新規登録に含まれていないものを削除するメソッド
   *
   * @param currentCourseIds 現在登録しているコースのID
   * @param newCourseIds     新たに登録するコースのID
   * @param studentId        対象の受講生ID
   */
  private void deleteCourses(List<Integer> currentCourseIds, List<Integer> newCourseIds,
      int studentId) {
    List<Integer> toDelete = currentCourseIds.stream()
        .filter(id -> !newCourseIds.contains(id))
        .toList();
    for (Integer courseId : toDelete) {
      repository.deleteStudentCourse(studentId, courseId);
    }
  }

  /**
   * 受講生情報論理削除　studentIdに紐づく任意の受講生情報の削除フラグを更新します。
   *
   * @param toDeleteIds  削除する受講生のID
   * @param toRestoreIds 復元する受講生のID
   * @return 論理削除後の受講生情報一覧（true,falseかかわらず全件）
   */
  @Transactional
  public List<StudentDetail> logicalDeleteStudent(
      List<Integer> toDeleteIds,
      List<Integer> toRestoreIds) {

    if (toDeleteIds != null) {
      for (Integer id : toDeleteIds) {
        repository.logicalDeleteStudent(id, true);
      }
    }
    if (toRestoreIds != null) {
      for (Integer id : toRestoreIds) {
        repository.logicalDeleteStudent(id, false);
      }
    }
    return getStudentDetail();
  }


}


