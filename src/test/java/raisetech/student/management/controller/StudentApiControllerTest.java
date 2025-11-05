package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.request.LogicalDeleteStudentRequest;
import raisetech.student.management.controller.request.UpdateStudentFieldRequest;
import raisetech.student.management.controller.request.UpdateStudentsCoursesRequest;
import raisetech.student.management.data.Student;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentApiControllerTest {

  private MockMvc mockMvc;

  @Mock
  private StudentService service;

  @Mock
  StudentConverter converter;

  @InjectMocks
  private StudentApiController controller;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  /**
   * 受講生一覧検索のテスト
   *
   * @throws Exception
   */
  @Test
  void 受講生詳細の一覧検索ができて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/api/students"))
        .andExpect(status().isOk());

    verify(service, times(1)).getStudentDetail();
  }

  /**
   * studentエンティティの必須項目を正常値で設定したもの
   *
   * @return student
   */
  private Student createValidStudent() {
    Student student = new Student();
    student.setName("田中健一");
    student.setFurigana("タナカケンイチ");
    student.setMailAddress("kenichi.tanaka@aaaa.com");
    student.setAge(20);
    student.setGender("男性");
    return student;
  }

  /**
   * Studentエンティティに対して、全ての必須項目を正しく入力した場合にバリデーションエラーが発生しないことを確認するテスト。
   */
  @Test
  void 受講生詳細の受講生で適切な値を入力した際に異常が発生しないこと() {
    Student student = createValidStudent();

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  /**
   * Studentエンティティに対して、nameフィールドのみ不正な値が入力されてた場合に正しくエラーが発生するか確認するテスト
   */
  @Test
  void 受講生詳細の受講生でnameフィールドのエラーを検知すること() {
    Student student = createValidStudent();
    student.setName("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
  }

  /**
   * Studentエンティティに対して、mailAddressフィールドのみ不正な値が入力されてた場合に正しくエラーが発生するか確認するテスト
   */
  @Test
  void 受講生詳細の受講生でmailAddressフィールドのエラーを検知すること() {
    Student student = createValidStudent();
    student.setMailAddress("aaa");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
  }

  /**
   * 指定した studentId の受講生情報が取得できることを確認するテスト。
   *
   * @throws Exception
   */
  @Test
  void 指定したstudentIdの受講生を検索できること() throws Exception {
    int studentId = 1;
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(createValidStudent());
    studentDetail.getStudent().setStudentId(studentId);

    when(service.getStudentDetail(studentId)).thenReturn(studentDetail);

    mockMvc.perform(get("/api/students/{studentId}", studentId))
        .andExpect(status().isOk());

    verify(service, times(1)).getStudentDetail(studentId);
  }

  @Test
  void 指定したstudentIdが存在しない場合に404が返ること() throws Exception {
    int studentId = 999;

    // service側で存在しないIDの場合に例外を投げる設定
    when(service.getStudentDetail(studentId))
        .thenThrow(new EmptyResultDataAccessException("Student Not Found", 1));

    mockMvc.perform(get("/api/students/{studentId}", studentId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Student Not Found"));

    verify(service, times(1)).getStudentDetail(studentId);
  }

  /**
   * 新規受講生登録 API (/api/students) が呼び出せることを確認するテスト。
   *
   * @throws Exception MockMvc 実行時の例外
   */
  @Test
  void 新規受講生登録が呼び出せること() throws Exception {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(createValidStudent());

    when(service.registerStudent(any(StudentDetail.class))).thenReturn(studentDetail);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/students")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(studentDetail)))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }


  /**
   * 新規受講生登録において異常値が入力されたときのテスト nameの場合
   *
   * @throws Exception
   */
  @Test
  void 新規受講生登録でnameに不正値を入力した際にエラーが出ること() throws Exception {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(createValidStudent());
    studentDetail.getStudent().setName(" ");

    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/students")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(studentDetail)))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).registerStudent(any(StudentDetail.class));
  }

  /**
   * 新規受講生登録において異常値が入力されたときのテスト mailAddressの場合
   *
   * @throws Exception
   */
  @Test
  void 新規受講生登録でmailAddressに不正値を入力した際にエラーが出ること() throws Exception {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(createValidStudent());
    studentDetail.getStudent().setMailAddress("aaaa");

    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/students")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(studentDetail)))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).registerStudent(any(StudentDetail.class));
  }

  /**
   * 受講コース登録 API (/api/students/courses) が呼び出せることを確認するテスト。
   *
   * @throws Exception MockMvc 実行時の例外
   */
  @Test
  void 受講コース登録が呼び出せること() throws Exception {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(createValidStudent());

    when(service.registerCourse(any(StudentDetail.class))).thenReturn(studentDetail);

    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/students/courses")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(studentDetail)))
        .andExpect(status().isOk());

    verify(service, times(1)).registerCourse(any(StudentDetail.class));
  }

  /**
   * 受講生情報更新 API (/api/students PATCH) が呼び出せることを確認するテスト。
   *
   * @throws Exception MockMvc 実行時の例外
   */
  @Test
  void 受講生情報更新が呼び出せること() throws Exception {
    UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
    request.setStudentId(1);
    request.setField("name");
    request.setValue("田中太郎");

    StudentDetail updatedDetail = new StudentDetail();
    updatedDetail.setStudent(createValidStudent());

    when(service.updateStudentField(any(UpdateStudentFieldRequest.class)))
        .thenReturn(updatedDetail);

    mockMvc.perform(
            patch("/api/students")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request))
        )
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudentField(any(UpdateStudentFieldRequest.class));
  }

  /**
   * 受講生情報更新の際に不正値に対してエラーが出ること フィールド名が不正の場合
   *
   * @throws Exception
   */
  @Test
  void 受講生情報更新で不正なフィールド名を入力した際にエラーが出ること() throws Exception {
    UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
    request.setStudentId(1);
    request.setField("password");
    request.setValue("aaa");

    mockMvc.perform(
            patch("/api/students")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());

    verify(service, times(0)).updateStudentField(any(UpdateStudentFieldRequest.class));
  }

  /**
   * 受講生情報更新の際に不正値に対してエラーが出ること valueのみが不正な場合 カスタムアノテーションで管理
   *
   * @throws Exception
   */
  @Test
  void 受講生情報更新で不正なバリュー名を入力した際にエラーが出ること() throws Exception {
    UpdateStudentFieldRequest request = new UpdateStudentFieldRequest();
    request.setStudentId(1);
    request.setField("gender");
    request.setValue("男");

    mockMvc.perform(
            patch("/api/students")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());

    verify(service, times(0)).updateStudentField(any(UpdateStudentFieldRequest.class));
  }

  /**
   * 受講コース更新 API (/api/students/courses PATCH) が呼び出せることを確認するテスト。
   *
   * @throws Exception MockMvc 実行時の例外
   */
  @Test
  void 受講コース更新が呼び出せること() throws Exception {
    UpdateStudentsCoursesRequest request = new UpdateStudentsCoursesRequest();
    request.setStudentId(1);
    request.setCourseIds(List.of(101, 102));

    StudentDetail updatedDetail = new StudentDetail();
    updatedDetail.setStudent(createValidStudent());

    when(service.updateCourse(any(UpdateStudentsCoursesRequest.class)))
        .thenReturn(updatedDetail);

    mockMvc.perform(
            patch("/api/students/courses")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request))
        )
        .andExpect(status().isOk());

    verify(service, times(1)).updateCourse(any(UpdateStudentsCoursesRequest.class));
  }

  /**
   *受講コース更新時、存在しないstudentIdに対して404が返ることの確認
   * @throws Exception
   */
  @Test
  void 受講コース更新時で存在しないstudentIdの指定に対しエラーが出ることを確認するテスト()
      throws Exception {
    UpdateStudentsCoursesRequest request = new UpdateStudentsCoursesRequest();
    request.setStudentId(999);
    request.setCourseIds(List.of(1, 2));

    when(service.updateCourse(any(UpdateStudentsCoursesRequest.class))).thenThrow(
        new EmptyResultDataAccessException("Student Not Found", 1));

    mockMvc.perform(patch("/api/students/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Student Not Found"));
    verify(service, times(1)).updateCourse(any(UpdateStudentsCoursesRequest.class));
  }

  /**
   * 受講生の論理削除・復元のAPIが呼び出せることを確認するテスト
   */
  @Test
  void 受講生の論理削除が呼び出せること() throws Exception {
    LogicalDeleteStudentRequest request = new LogicalDeleteStudentRequest();
    request.setToDeleteIds(List.of(1, 2));
    request.setToRestoreIds(List.of(3));

    when(service.logicalDeleteStudent(request.getToDeleteIds(), request.getToRestoreIds()))
        .thenReturn(List.of());

    mockMvc.perform(
            patch("/api/students/logical-delete")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request))
        )
        .andExpect(status().isOk());

    verify(service, times(1))
        .logicalDeleteStudent(request.getToDeleteIds(), request.getToRestoreIds());
  }

  @Test
  void 受講の論理削除で存在しないstudentIdの指定に対しエラーが出ることを確認するテスト()
      throws Exception {
    LogicalDeleteStudentRequest request = new LogicalDeleteStudentRequest();
    request.setToDeleteIds(List.of(1,2));
    request.setToRestoreIds(List.of(3,4));

    when(service.logicalDeleteStudent(anyList(),anyList())).thenThrow(
        new EmptyResultDataAccessException("Student Not Found", 1));

    mockMvc.perform(patch("/api/students/logical-delete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Student Not Found"));
    verify(service, times(1)).logicalDeleteStudent(request.getToDeleteIds(),request.getToRestoreIds());
  }


}