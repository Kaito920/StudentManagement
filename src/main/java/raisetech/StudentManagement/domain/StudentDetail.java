package raisetech.StudentManagement.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourse;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentsCourse> studentsCourses = new ArrayList<>();
  private List<Courses> courses = new ArrayList<>();

  // フォームから選択されたコースIDを受け取る
  private List<Integer> courseId = new ArrayList<>();


}
