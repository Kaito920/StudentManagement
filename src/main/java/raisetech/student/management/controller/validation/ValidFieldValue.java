package raisetech.student.management.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UpdateStudentFieldRequest に対して、
 * field と value の組み合わせが正しいかどうかを検証するアノテーション。
 */
@Documented
@Constraint(validatedBy = FieldValueValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFieldValue {
  String message() default "不正なフィールド名または値です";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

}
