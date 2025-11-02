package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentApiController.class)
class StudentApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

 @MockBean
  private StudentService service;

 @MockBean StudentConverter converter;

 private Validator validator= Validation.buildDefaultValidatorFactory().getValidator();

  /**
   * 受講生一覧検索のテスト
   * @throws Exception
   */
  @Test
  void 受講生詳細の一覧検索ができて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/api/students"))
        .andExpect(status().isOk());

    verify(service,times(1)).getStudentDetail();
  }

  /**
   *
   */
  @Test
  void 受講生詳細の受講生で適切な値を入力した際に異常が発生しないこと(){
    Student student = new Student();
    student.setName("田中健一");
    student.setFurigana("タナカケンイチ");
    student.setMailAddress("kenichi.tanaka@aaaa.com");
    student.setAge(20);
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }



}