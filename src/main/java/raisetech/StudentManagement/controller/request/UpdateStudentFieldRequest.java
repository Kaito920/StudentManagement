package raisetech.StudentManagement.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateStudentFieldRequest {

  private String value;
  private String field;
  private int studentId;

}
