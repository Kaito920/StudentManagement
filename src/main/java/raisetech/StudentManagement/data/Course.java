package raisetech.StudentManagement.data;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Course {

  private int courseId;

  @NotNull
  private String courseName;


  private String description;


}
