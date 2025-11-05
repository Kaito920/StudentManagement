package raisetech.student.management.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import raisetech.student.management.controller.request.UpdateStudentFieldRequest;

/**
 * ValidFieldValue アノテーションに対応する実際の検証ロジック。
 */
public class FieldValueValidator implements ConstraintValidator<ValidFieldValue, UpdateStudentFieldRequest> {

  @Override
  public boolean isValid(UpdateStudentFieldRequest request, ConstraintValidatorContext context) {
    if (request == null) {
      return true;
    }
    String field = request.getField();
    String value = request.getValue();

    return switch (field) {
      case "name", "furigana", "nickname", "address" -> !value.isBlank();
      case "remark"->value.length()<=200;
      case "gender"->value.matches("^(男性|女性|その他)$");
      case "mailAddress"->value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
      case "age" -> value.matches("^[1-9][0-9]*$");
      default -> false;
    };
  }
}