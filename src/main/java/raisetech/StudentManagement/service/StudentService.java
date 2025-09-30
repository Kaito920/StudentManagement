package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Courses;
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
    return repository.searchStudent();

  }

  public List<StudentsCourse> searchStudentsCourseList() {
    return repository.searchStudentsCourse();

  }

  public List<Courses> searchCourseList(){
    return repository.searchCourse();

  }

  @Transactional
  public void registerStudent(Student student) {
    repository.registerStudent(student);
  }

  @Transactional
  public  void  registerCourse(List<StudentsCourse> studentsCourse){
    for (StudentsCourse sc : studentsCourse){
      repository.registerCourse(sc);

    }
  }



}
