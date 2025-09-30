package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourse;
import raisetech.StudentManagement.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourse> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentsCourse> convertStudentsCourses = studentsCourses.stream()
          .filter(studentsCourse -> student.getStudentId()==studentsCourse.getStudentId())
          .collect(Collectors.toList());
      studentDetail.setStudentsCourses(convertStudentsCourses);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
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

  public List<StudentsCourse> convertToStudentsCourses(StudentDetail detail) {
    return detail.getStudentsCourses().stream()
        .map(course -> {
          StudentsCourse studentsCourse = new StudentsCourse();
          studentsCourse.setStudentId(detail.getStudent().getStudentId());
          studentsCourse.setCourseId(course.getCourseId());
          return studentsCourse;
        })
        .collect(Collectors.toList());
  }


}

