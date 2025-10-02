package raisetech.StudentManagement.controller.converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;

@Component
public class StudentConverter {

  //一覧表示用
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentsCourses> convertStudentsCourses = studentsCourses.stream()
          .filter(studentsCourse -> student.getStudentId()==studentsCourse.getStudentId())
          .collect(Collectors.toList());
      studentDetail.setStudentsCourses(convertStudentsCourses);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  //個別表示用
  public StudentDetail convertStudentDetails(Student student,List<StudentsCourses> studentsCourses){
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);

    return studentDetail;
  }

  public Student convertToStudent(StudentDetail detail) {
    Student student = new Student();
    student.setName(detail.getStudent().getName());
    student.setFurigana(detail.getStudent().getFurigana());
    student.setNickname(detail.getStudent().getNickname());
    student.setMailAddress(detail.getStudent().getMailAddress());
    student.setAddress(detail.getStudent().getAddress());
    student.setAge(detail.getStudent().getAge());
    student.setGender(detail.getStudent().getGender());

    return student;
  }

  public List<StudentsCourses> convertToStudentsCourses(StudentDetail detail) {
    List<StudentsCourses> studentsCourses = new ArrayList<>();

    for (Integer courseIds : detail.getCourseIds()) {
      StudentsCourses sc = new StudentsCourses();
      sc.setStudentId(detail.getStudent().getStudentId());
      sc.setCourseId(courseIds);
      sc.setStartDate(LocalDate.now());
      sc.setEndDate(LocalDate.now().plusMonths(6));
      studentsCourses.add(sc);
    }

    return studentsCourses;
  }





}

