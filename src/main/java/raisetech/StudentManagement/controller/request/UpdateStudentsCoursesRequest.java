package raisetech.StudentManagement.controller.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentsCoursesRequest {

  @NotNull
  private  Integer studentId;

  private List<Integer> courseIds;

}
