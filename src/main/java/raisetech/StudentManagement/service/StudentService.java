package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourse;
import raisetech.StudentManagement.repoitory.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudent().stream()
        .filter(s -> s.getAge()>=30&&s.getAge()<40)
        .toList();
  }

  public List<StudentsCourse> searchStudentsCourseList() {
    return repository.searchStudentsCourse().stream()
        .filter(s-> s.getCourseName().equals("JAVA_course"))
        .toList();
  }

}
