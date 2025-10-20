package raisetech.StudentManagement.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Course {

  private int courseId;

  @NotBlank
  private String courseName;


  private String description;


}
