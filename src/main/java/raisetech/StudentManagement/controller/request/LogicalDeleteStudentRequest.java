package raisetech.StudentManagement.controller.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogicalDeleteStudentRequest {
  @NotNull
  private List<Integer> toDeleteIds;

  @NotNull
  private List<Integer> toRestoreIds;
}
