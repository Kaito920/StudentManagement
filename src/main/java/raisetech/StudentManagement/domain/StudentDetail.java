package raisetech.StudentManagement.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentsCourses> studentsCourses = new ArrayList<>();
  private List<Courses> courses = new ArrayList<>();

  // フォームから選択されたコースIDを受け取る
  private List<Integer> courseIds = new ArrayList<>();


}
