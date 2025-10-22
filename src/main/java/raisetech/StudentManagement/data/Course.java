package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "コース情報")
@Getter
@Setter

public class Course {

  private int courseId;

  @NotBlank
  private String courseName;


  private String description;


}
