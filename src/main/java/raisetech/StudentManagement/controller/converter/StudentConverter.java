package raisetech.StudentManagement.controller.converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、コース情報もしくはその逆の変換を行うコンバーター
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする
   *
   * @param students        受講生一覧
   * @param studentsCourses 受講生コース情報のリスト
   * @param courses         コース情報のリスト
   * @return 受講生詳細のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses, List<Courses> courses) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    Map<Integer, Courses> courseMap = courses.stream()
        .collect(Collectors.toMap(Courses::getCourseId, Function.identity()));

    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentsCourses> convertStudentsCourses = studentsCourses.stream()
          .filter(studentsCourse -> student.getStudentId() == studentsCourse.getStudentId())
          .collect(Collectors.toList());
      studentDetail.setStudentsCourses(convertStudentsCourses);

      List<Courses> studentCourses = convertStudentsCourses.stream()
          .map(sc -> courseMap.get(sc.getCourseId()))
          .filter(Objects::nonNull) // 念のため null を除外
          .collect(Collectors.toList());
      studentDetail.setCourses(studentCourses);

      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  //個別表示用
  public StudentDetail convertStudentDetails(Student student,
      List<StudentsCourses> studentsCourses, List<Courses> courses) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);

    Map<Integer, Courses> courseMap = courses.stream()
        .collect(Collectors.toMap(Courses::getCourseId, Function.identity()));

    List<Courses> studentCourses = studentsCourses.stream()
        .map(sc -> courseMap.get(sc.getCourseId()))
        .filter(Objects::nonNull)
        .toList();

    studentDetail.setCourses(courses);

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

