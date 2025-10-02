package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repoitory.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter  converter) {
    this.repository = repository;
    this.converter = converter;
  }


  public List<Student> searchStudentList() {
    return repository.searchStudent();

  }

  public List<StudentsCourses> searchStudentsCourseList() {
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
  public  void  registerCourse(List<StudentsCourses> studentsCourses){
    for (StudentsCourses sc : studentsCourses){
      repository.registerCourse(sc);
    }
  }

  public StudentDetail getStudentDetailById(int studentId){
    Student student = repository.searchStudentById(studentId);
    List<StudentsCourses> studentsCourses = repository.searchStudentCourseById(studentId);

    return converter.convertStudentDetails(student,studentsCourses);
  }

}
