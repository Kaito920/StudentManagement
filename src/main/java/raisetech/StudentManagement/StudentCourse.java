package raisetech.StudentManagement;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StudentCourse {

  private String courseID;
  private String studentID;
  private String courseName;
  private Date startDate;
  private Date endDate;

}
