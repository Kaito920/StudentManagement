package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudent();

  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourse();

  }

  public List<StudentsCourses> searchStudentsCoursesById(int studentId){
    return  repository.searchStudentCourseById(studentId);
  }

  public List<Courses> searchCourseList() {
    return repository.searchCourse();

  }

  public List<Courses> searchCoursesByStudentId(int studentId){
    List<StudentsCourses> studentsCourses = repository.searchStudentCourseById(studentId);
    List<Integer> courseIds = studentsCourses.stream()
        .map(StudentsCourses::getCourseId)
        .toList();

    return repository.searchCoursesById(courseIds);
  }



  @Transactional
  public Student registerStudent(Student student) {
    repository.registerStudent(student);
    return student;
  }

  @Transactional
  public void registerCourse(List<StudentsCourses> studentsCourses) {
    for (StudentsCourses sc : studentsCourses) {
      repository.registerCourse(sc);
    }
  }

  public StudentDetail getStudentDetailById(int studentId) {
    Student student = repository.searchStudentById(studentId);
    List<StudentsCourses> studentsCourses = repository.searchStudentCourseById(studentId);
    List<Courses> courses = repository.searchCourse().reversed();

    for (StudentsCourses sc : studentsCourses) {
      Courses course = repository.searchCourseById(sc.getCourseId());
      sc.setCourses(course);
    }
    return converter.convertStudentDetails(student, studentsCourses,courses);
  }

  public Student searchStudentById(int studentId) {
    return repository.searchStudentById(studentId);
  }

  @Transactional
  public void updateStudentField(int studentId, String field, String value) {
    switch (field) {
      case "name":
        repository.updateStudentName(studentId, value);
        break;
      case "furigana":
        repository.updateStudentFurigana(studentId, value);
        break;
      case "nickname":
        repository.updateStudentNickname(studentId, value);
        break;
      case "mailAddress":
        repository.updateStudentMailAddress(studentId, value);
        break;
      case "address":
        repository.updateStudentAddress(studentId, value);
        break;
      case "age":
        repository.updateStudentAge(studentId, Integer.parseInt(value));
        break;
      case "gender":
        repository.updateStudentGender(studentId, value);
        break;
      case "remark":
        repository.updateStudentRemark(studentId, value);
        break;
    }
  }

  @Transactional
  public void updateCourse(int studentId, List<Integer> newCourseIds) {
    List<StudentsCourses> studentsCourses = repository.searchStudentCourseById(studentId);
    List<Integer> currentCourseIds = studentsCourses.stream()
        .map(StudentsCourses::getCourseId)
        .toList();

    List<Integer> toInsert = newCourseIds.stream()
        .filter(id -> !currentCourseIds.contains(id))
        .toList();
    for (Integer courseId : toInsert) {
      StudentsCourses newCourses = new StudentsCourses();
      newCourses.setStudentId(studentId);
      newCourses.setCourseId(courseId);
      newCourses.setStartDate(LocalDate.now());
      newCourses.setEndDate(LocalDate.now().plusMonths(3));
      repository.registerCourse(newCourses);
    }

    List<Integer> toDelete = currentCourseIds.stream()
        .filter(id -> !newCourseIds.contains(id))
        .toList();

    for (Integer courseId : toDelete) {
      repository.deleteStudentCourse(studentId, courseId);
    }
    
  }

  public void logicalDeleteStudent(List<Integer> checkedStudentIds) {
    if (checkedStudentIds == null) {
      checkedStudentIds = new ArrayList<>();
    }
    List<Student> students = repository.searchStudent();
    List<Integer> allStudentIds = students.stream()
        .map(Student::getStudentId)
        .toList();

    for (Integer studentId : allStudentIds) {
      boolean isDeleted = checkedStudentIds.contains(studentId);
      repository.logicalDeleteStudent(studentId, isDeleted);
    }
  }


}


