package raisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

@ExtendWith(MockitoExtension.class)
class StudentConverterTest {

  @InjectMocks
  private StudentConverter converter;

  private Student student1;
  private Student student2;
  private Course course1;
  private Course course2;
  private StudentCourse studentCourse1;
  private StudentCourse studentCourse2;

  @BeforeEach
  void setUp() {
    student1 = new Student();
    student1.setStudentId(1);

    student2 = new Student();
    student2.setStudentId(2);

    course1 = new Course();
    course1.setCourseId(1);

    course2 = new Course();
    course2.setCourseId(2);
  }

  @Test
  void convertStudentDetails受講生とコースが正しくマッピングされること_一覧表示() {
    List<Student> studentList = List.of(student1, student2);
    List<Course> courseList = List.of(course1, course2);

    List<StudentCourse> studentCourses = new ArrayList<>();

    StudentCourse sc1 = new StudentCourse();
    sc1.setStudentId(student1.getStudentId());
    sc1.setCourseId(course1.getCourseId());
    studentCourses.add(sc1);

    StudentCourse sc2 = new StudentCourse();
    sc2.setStudentId(student2.getStudentId());
    sc2.setCourseId(course1.getCourseId());
    studentCourses.add(sc2);

    StudentCourse sc3 = new StudentCourse();
    sc3.setStudentId(student2.getStudentId());
    sc3.setCourseId(course2.getCourseId());
    studentCourses.add(sc3);

    List<StudentDetail> actual = converter.convertStudentDetails(studentList,
        studentCourses, courseList);

    assertThat(actual).hasSize(2);

    StudentDetail studentDetail1 = actual.stream()
        .filter(detail1 -> detail1.getStudent().getStudentId() == 1)
        .findFirst()
        .orElseThrow();
    assertThat(studentDetail1.getStudentCourseList()).hasSize(1);
    assertThat(studentDetail1.getCourseList()).extracting("courseId")
        .containsExactly(course1.getCourseId());

    StudentDetail studentDetail2 = actual.stream()
        .filter(detail2 -> detail2.getStudent().getStudentId() == 2)
        .findFirst()
        .orElseThrow();
    assertThat(studentDetail2.getStudentCourseList()).hasSize(2);
    assertThat(studentDetail2.getCourseList()).extracting("courseId")
        .containsExactly(course1.getCourseId(), course2.getCourseId());
  }

  @Test
  void convertStudentDetails受講生とコースが正しくマッピングされること_個別表示() {
    Student student = student1;
    List<Course> courseList = List.of(course1, course2);

    List<StudentCourse> studentCourses = new ArrayList<>();

    StudentCourse sc1 = new StudentCourse();
    sc1.setStudentId(student.getStudentId());
    sc1.setCourseId(course1.getCourseId());
    studentCourses.add(sc1);

    StudentCourse sc2 = new StudentCourse();
    sc2.setStudentId(student.getStudentId());
    sc2.setCourseId(course2.getCourseId());
    studentCourses.add(sc2);

    StudentDetail actual = converter.convertStudentDetails(student,
        studentCourses, courseList);

    assertThat(actual.getStudent().getStudentId()).isEqualTo(1);
    assertThat(actual.getStudentCourseList()).hasSize(2);
    assertThat(actual.getCourseList())
        .extracting("courseId")
        .containsExactly(course1.getCourseId(), course2.getCourseId());
  }

  @Test
  void convertToStudent_detailからStudentに正しく変換されること() {
    Student testStudent = new Student();

    testStudent.setName("test");
    testStudent.setFurigana("テスト");
    testStudent.setNickname("tester");
    testStudent.setMailAddress("test@aaaa.com");
    testStudent.setAddress("東京");
    testStudent.setAge(10);
    testStudent.setGender("男性");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(testStudent);

    Student actual = converter.convertToStudent(studentDetail);

    assertThat(actual).usingRecursiveComparison().isEqualTo(testStudent);

  }

  @Test
  void convertToStudentsCourses_detailからstudentCourseに正しく変換されること() {
    Student testStudent = new Student();
    testStudent.setStudentId(1);

    StudentDetail sc = new StudentDetail();
    sc.setStudent(testStudent);
    sc.setCourseIds(List.of(1, 2));

    List<StudentCourse> actual = converter.convertToStudentsCourses(sc);

    assertThat(actual).hasSize(2);

    assertThat(actual)
        .extracting(StudentCourse::getStudentId)
        .containsOnly(1);
    assertThat(actual)
        .extracting(StudentCourse::getCourseId)
        .containsExactly(1, 2);

    assertThat(actual.getFirst().getStartDate()).isNotNull();
    assertThat(actual.getFirst().getEndDate()).isEqualTo(
        actual.getFirst().getStartDate().plusMonths(6));
  }

  @Test
  void convertToStudentsCourses_コースIDが空の場合は空リストを返すこと() {
    Student student = new Student();
    student.setStudentId(1);

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setCourseIds(List.of());

    List<StudentCourse> actual = converter.convertToStudentsCourses(detail);

    assertThat(actual).isEmpty();
  }

}

