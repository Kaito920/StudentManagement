package raisetech.StudentManagement.controller.converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Course;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、コース情報もしくはその逆の変換を行うコンバーター
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする
   *
   * @param studentList        受講生一覧
   * @param studentCourseList 受講生コース情報のリスト
   * @param courses         コース情報のリスト
   * @return 受講生詳細のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList, List<Course> courses) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    Map<Integer, Course> courseMap = courses.stream()
        .collect(Collectors.toMap(Course::getCourseId, Function.identity()));

    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentsCourse -> student.getStudentId() == studentsCourse.getStudentId())
          .collect(Collectors.toList());
      studentDetail.setStudentCourseList(convertStudentCourseList);

      List<Course> studentCourses = convertStudentCourseList.stream()
          .map(sc -> courseMap.get(sc.getCourseId()))
          .filter(Objects::nonNull) // 念のため null を除外
          .collect(Collectors.toList());
      studentDetail.setCourseList(studentCourses);

      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  //個別表示用
  public StudentDetail convertStudentDetails(Student student,
      List<StudentCourse> studentsCourses, List<Course> courses) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentsCourses);

    Map<Integer, Course> courseMap = courses.stream()
        .collect(Collectors.toMap(Course::getCourseId, Function.identity()));

    List<Course> studentCourses = studentsCourses.stream()
        .map(sc -> courseMap.get(sc.getCourseId()))
        .filter(Objects::nonNull)
        .toList();

    studentDetail.setCourseList(courses);

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

  public List<StudentCourse> convertToStudentsCourses(StudentDetail detail) {
    List<StudentCourse> studentsCourses = new ArrayList<>();

    for (Integer courseIds : detail.getCourseIds()) {
      StudentCourse sc = new StudentCourse();
      sc.setStudentId(detail.getStudent().getStudentId());
      sc.setCourseId(courseIds);
      sc.setStartDate(LocalDate.now());
      sc.setEndDate(LocalDate.now().plusMonths(6));
      studentsCourses.add(sc);
    }

    return studentsCourses;
  }


}

