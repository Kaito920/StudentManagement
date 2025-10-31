package raisetech.student.management.controller.request;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Getter
@Setter

public class UpdateStudentFieldRequest {

  @NotBlank
  private String value;
  @NotBlank
  private String field;
  @NotNull
  private Integer studentId;

}
