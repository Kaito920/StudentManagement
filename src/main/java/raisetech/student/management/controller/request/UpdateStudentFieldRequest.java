package raisetech.student.management.controller.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import raisetech.student.management.controller.validation.ValidFieldValue;

@Getter
@Setter
@ValidFieldValue
public class UpdateStudentFieldRequest {

  @NotBlank
  private String value;
  @NotBlank
  private String field;
  @NotNull
  private Integer studentId;

}
