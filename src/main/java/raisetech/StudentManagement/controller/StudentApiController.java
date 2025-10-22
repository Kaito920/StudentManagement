package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.controller.request.LogicalDeleteStudentRequest;
import raisetech.StudentManagement.controller.request.UpdateStudentFieldRequest;
import raisetech.StudentManagement.controller.request.UpdateStudentsCoursesRequest;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索、登録、更新を行うREST APIとして受け付けるController
 */
@Validated
@RestController
public class StudentApiController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentApiController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索 全件検索を行うため条件指定は行いません。
   *
   * @return 受講生一覧（全件）
   */
  @Operation(summary = "一覧検索",description = "受講生の一覧を検索します。")
  @GetMapping("/api/students")
  public ResponseEntity<List<StudentDetail>> getStudentList() {
    return  ResponseEntity.ok(service.getStudentDetail());
  }

  /**
   * 受講生単一検索 studentIdに紐づく任意の受講生情報を取得します。
   *
   * @param studentId 　受講生ID
   * @return 受講生情報
   */
  @Operation(summary = "受講生検索",description = "受講生IDに紐づく受講生情報を検索します。")
  @GetMapping("/api/students/{studentId}")
  public ResponseEntity<StudentDetail> getStudent(@PathVariable int studentId) {
    return ResponseEntity.ok(service.getStudentDetail(studentId));
  }

  /**
   * 新規登録：受講生登録 入力した情報を持つ受講生を新たに登録します。
   *
   * @param studentDetail 登録する受講生の情報（名前、メールアドレスなど）
   * @return 登録した受講生情報
   */
  @Operation(summary = "受講生登録",description = "新規受講生を登録します。")
  @PostMapping("/api/students")
  public ResponseEntity<StudentDetail> registerStudent(
      @Valid @RequestBody StudentDetail studentDetail) {
    StudentDetail registerStudent = service.registerStudent(studentDetail);

    return ResponseEntity.ok(registerStudent);
  }

  /**
   * 新規登録：受講コース登録
   *
   * @param studentDetail 受講生と登録されたコースの情報
   * @return 登録後の受講生情報
   */
  @Operation(summary = "受講コース登録",description = "選択した受講生に任意の受講コースを登録します。")
  @PostMapping("/api/students/courses")
  public ResponseEntity<StudentDetail> registerCourse(
      @Valid @RequestBody StudentDetail studentDetail) {
    StudentDetail updateStudentDetail = service.registerCourse(studentDetail);

    return ResponseEntity.ok(updateStudentDetail);
  }

  /**
   * 受講生情報更新（論理削除を除く）
   *
   * @param request 更新内容（対象フィールドと更新値）
   * @return 更新後の受講生情報（またはエラーメッセージ）
   */
  @Operation(summary = "受講生情報更新",description = "選択した受講生の持つ情報を任意に一つ更新します。")
  @PatchMapping("/api/students")
  public ResponseEntity<StudentDetail> updateField(@Valid @RequestBody UpdateStudentFieldRequest request) {
      StudentDetail updatedStudent = service.updateStudentField(request);
      return ResponseEntity.ok(updatedStudent);
  }

  /**
   * 受講コース更新 指定した受講生に対し受講コースを新たに再登録、または削除を行う
   *
   * @param request 受講生IDと更新後の受講コースID一覧
   * @return 更新後の受講生詳細情報
   */
  @Operation(summary = "受講コース更新",description = "選択した受講生の持つコース情報を更新します。")
  @PatchMapping("/api/students/courses")
  public ResponseEntity<StudentDetail> updateCourse(
      @Valid @RequestBody UpdateStudentsCoursesRequest request) {
    StudentDetail updateStudentCourse = service.updateCourse(request);
    return ResponseEntity.ok(updateStudentCourse);
  }

  /**
   * 受講生の論理削除または復元　指定した受講生に対し削除フラグの切り替えを行う。
   *
   * @param request フラグの切り替えリクエスト（削除or復元）
   * @return 更新後の受講生一覧
   */
  @Operation(summary = "受講生削除（論理）",description = "選択した受講生の論理削除、または復元を行います。")
  @PatchMapping("/api/students/logical-delete")
  public ResponseEntity<List<StudentDetail>> logicalDeleteStudent(@Valid @RequestBody
  LogicalDeleteStudentRequest request) {
    List<StudentDetail> studentList = service.logicalDeleteStudent(
        request.getToDeleteIds(),
        request.getToRestoreIds()
    );
    return ResponseEntity.ok(studentList);
  }
}
