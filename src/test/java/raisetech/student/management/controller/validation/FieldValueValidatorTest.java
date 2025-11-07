package raisetech.student.management.controller.validation;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import raisetech.student.management.controller.request.UpdateStudentFieldRequest;

class FieldValueValidatorTest {

  FieldValueValidator validator = new FieldValueValidator();
  ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

  /**
   * リクエストがnullの際にtrueを返すことを確認するテスト
   */
  @Test
  void nullRequest() {
    assertTrue(validator.isValid(null, context), "requestがnullの場合はtrue");
  }

  /**
   * name, furigana, nickname, addressが!value.isBlank()を満たすことを確認するテスト
   */
  @Test
  void blankField() {
    assertTrue(validator.isValid(new UpdateStudentFieldRequest("name", "田中健"), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("name", ""), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("name", null), context));
  }

  /**
   * remarkにおいてvalue.length()<=200を満たすことを確認するテスト
   */
  @Test
  void remarkLength() {
    assertTrue(validator.isValid(new UpdateStudentFieldRequest("remark", "200文字以内"), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("remark", null), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("remark", "a".repeat(201)), context));
  }

  /**
   * genderにおいてvalue.matches("^(男性|女性|その他)$")を満たすことを確認するテスト
   */
  @Test
  void gender() {
    assertTrue(validator.isValid(new UpdateStudentFieldRequest("gender", "男性"), context));
    assertTrue(validator.isValid(new UpdateStudentFieldRequest("gender", "女性"), context));
    assertTrue(validator.isValid(new UpdateStudentFieldRequest("gender", "その他"), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("gender", null), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("gender", "指定外文字"), context));
  }

  /**
   * mailAddressにおいてその形式が守られているかのテスト
   */
  @Test
  void mailAddress() {
    assertTrue(validator.isValid(new UpdateStudentFieldRequest("mailAddress", "abc@aaa.com"), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("mailAddress", "aaa"), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("mailAddress", null), context));
  }

  /**
   * ageにおいて値が正の整数であることを確認するテスト
   */
  @Test
  void age() {
    assertTrue(validator.isValid(new UpdateStudentFieldRequest("age", "1"), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("age", "-1"), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("age", "a"), context));
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("age", null), context));
  }

  @Test
  void notApplicableField() {
    assertFalse(validator.isValid(new UpdateStudentFieldRequest("notApplicableField", "value"), context));
  }


}

