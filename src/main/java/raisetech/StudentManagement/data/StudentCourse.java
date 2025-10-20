package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StudentCourse {


  private int courseId;


  private int studentId;


  private LocalDate startDate;


  private LocalDate endDate;


  private Course course;

}
