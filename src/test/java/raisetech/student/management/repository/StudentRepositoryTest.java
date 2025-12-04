package raisetech.student.management.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索ができること() {
    List<Student> actual = sut.searchStudent();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生ID検索によって該当の受講生が取得できること() {
    Student actual = sut.searchStudentById(1);

    assertThat(actual).isNotNull();
    assertThat(actual.getStudentId()).isEqualTo(1);
    assertThat(actual.getName()).isEqualTo("田中太郎");
  }

  @Test
  void 存在しない受講生IDによる検索でNULLを返すこと() {
    Student actual = sut.searchStudentById(10);

    assertThat(actual).isNull();
  }

  @Test
  void コースの一覧検索ができること() {
    List<Course> actual = sut.searchCourse();

    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void IDによるコース検索が正しくできること() {
    List<Course> actual = sut.searchCoursesById(List.of(1));

    assertThat(actual.size()).isEqualTo(1);

    Course course = actual.get(0);
    assertThat(course.getCourseId()).isEqualTo(1);
    assertThat(course.getCourseName()).isEqualTo("Java基礎");
  }

  @Test
  void 存在しないIDによるコース検索で空のリストを返すこと() {
    List<Course> actual = sut.searchCoursesById(List.of(10));

    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生コース情報の一覧検索ができること() {
    List<StudentCourse> actual = sut.searchStudentsCourses();

    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  void 受講生IDによる受講生コース情報の検索ができること() {
    List<StudentCourse> actual = sut.searchStudentsCoursesById(1);

    assertThat(actual.size()).isEqualTo(2);

    assertThat(actual)
        .extracting(StudentCourse::getCourseId)
        .containsExactlyInAnyOrder(1, 2);

    assertThat(actual)
        .extracting(StudentCourse::getStudentId)
        .containsOnly(1);
  }

  @Test
  void 存在しない受講生IDによる受講生コース情報の検索で空のリストを返すこと() {
    List<StudentCourse> actual = sut.searchStudentsCoursesById(10);

    assertThat(actual).isEmpty();
  }


  @Test
  void 受講生の登録が正しくできること() {
    Student student = new Student();
    student.setName("早川健太");
    student.setFurigana("ハヤカワケンタ");
    student.setNickname("ケンタ");
    student.setMailAddress("kenta@exaple.com");
    student.setAddress("神奈川県");
    student.setAge(26);
    student.setGender("男性");
    student.setRemark("甘いものが好き");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.searchStudent();
    assertThat(actual.size()).isEqualTo(6);

    Student added = sut.searchStudentById(student.getStudentId());

    assertThat(added).isNotNull();
    assertThat(added.getName()).isEqualTo("早川健太");
    assertThat(added.getFurigana()).isEqualTo("ハヤカワケンタ");
    assertThat(added.getNickname()).isEqualTo("ケンタ");
    assertThat(added.getMailAddress()).isEqualTo("kenta@exaple.com");
    assertThat(added.getAddress()).isEqualTo("神奈川県");
    assertThat(added.getAge()).isEqualTo(26);
    assertThat(added.getGender()).isEqualTo("男性");
    assertThat(added.getRemark()).isEqualTo("甘いものが好き");
    assertThat(added.isDeleted()).isFalse();
  }

  @Test
  void コース情報の登録が正しくできること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(5);
    studentCourse.setCourseId(1);
    studentCourse.setStartDate(LocalDate.of(2025, 1, 1));
    studentCourse.setEndDate(LocalDate.of(2025, 6, 30));

    sut.registerCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentsCoursesById(5);
    assertThat(actual.size()).isEqualTo(2);

    assertThat(actual)
        .extracting(StudentCourse::getCourseId)
        .containsExactlyInAnyOrder(1, 2);
  }

  @Test
  void 受講コース状況が正しく削除されること(){
    sut.deleteStudentCourse(1,1);

    List<StudentCourse> actual = sut.searchStudentsCoursesById(1);
    assertThat(actual.size()).isEqualTo(1);

    assertThat(actual)
        .extracting(StudentCourse::getCourseId)
        .containsExactly(2);
  }

  @Test
  void 受講生情報の更新が正しくできていること_name_furigana(){
    Student before = sut.searchStudentById(1);
    sut.updateStudentName(1,"本田太郎");
    sut.updateStudentFurigana(1,"ホンダタロウ");

    Student actual = sut.searchStudentById(1);
    assertThat(actual.getName()).isEqualTo("本田太郎");
    assertThat(actual.getFurigana()).isEqualTo("ホンダタロウ");
    Student after = sut.searchStudentById(1);

    assertThat(after.getStudentId()).isEqualTo(before.getStudentId());
    assertThat(after.getNickname()).isEqualTo(before.getNickname());
    assertThat(after.getMailAddress()).isEqualTo(before.getMailAddress());
    assertThat(after.getAddress()).isEqualTo(before.getAddress());
    assertThat(after.getAge()).isEqualTo(before.getAge());
    assertThat(after.getGender()).isEqualTo(before.getGender());
    assertThat(after.getRemark()).isEqualTo(before.getRemark());
  }

  @Test
  void 受講生情報の更新が正しくできていること_mailAddress(){
    Student before = sut.searchStudentById(1);
    sut.updateStudentMailAddress(1,"tarochan@example.com");

    Student actual = sut.searchStudentById(1);
    assertThat(actual.getMailAddress()).isEqualTo("tarochan@example.com");
    Student after = sut.searchStudentById(1);

    assertThat(after.getStudentId()).isEqualTo(before.getStudentId());
    assertThat(after.getName()).isEqualTo(before.getName());
    assertThat(after.getFurigana()).isEqualTo(before.getFurigana());
    assertThat(after.getNickname()).isEqualTo(before.getNickname());
    assertThat(after.getAddress()).isEqualTo(before.getAddress());
    assertThat(after.getAge()).isEqualTo(before.getAge());
    assertThat(after.getGender()).isEqualTo(before.getGender());
    assertThat(after.getRemark()).isEqualTo(before.getRemark());
  }

  @Test
  void 受講生情報の更新が正しくできていること_age(){
    Student before = sut.searchStudentById(1);
    sut.updateStudentAge(1,28);

    Student actual = sut.searchStudentById(1);
    assertThat(actual.getAge()).isEqualTo(28);
    Student after = sut.searchStudentById(1);

    assertThat(after.getStudentId()).isEqualTo(before.getStudentId());
    assertThat(after.getName()).isEqualTo(before.getName());
    assertThat(after.getFurigana()).isEqualTo(before.getFurigana());
    assertThat(after.getNickname()).isEqualTo(before.getNickname());
    assertThat(after.getMailAddress()).isEqualTo(before.getMailAddress());
    assertThat(after.getAddress()).isEqualTo(before.getAddress());
    assertThat(after.getGender()).isEqualTo(before.getGender());
    assertThat(after.getRemark()).isEqualTo(before.getRemark());
  }

  @Test
  void 受講生情報の更新が正しくできていること_gender(){
    Student before = sut.searchStudentById(1);
    sut.updateStudentGender(1,"その他");

    Student actual = sut.searchStudentById(1);
    assertThat(actual.getGender()).isEqualTo("その他");
    Student after = sut.searchStudentById(1);

    assertThat(after.getStudentId()).isEqualTo(before.getStudentId());
    assertThat(after.getName()).isEqualTo(before.getName());
    assertThat(after.getFurigana()).isEqualTo(before.getFurigana());
    assertThat(after.getNickname()).isEqualTo(before.getNickname());
    assertThat(after.getAddress()).isEqualTo(before.getAddress());
    assertThat(after.getAddress()).isEqualTo(before.getAddress());
    assertThat(after.getAge()).isEqualTo(before.getAge());
    assertThat(after.getRemark()).isEqualTo(before.getRemark());
  }

  @Test
  void 受講生の論理削除が正しくできること_削除(){
    Student before = sut.searchStudentById(1);
    sut.logicalDeleteStudent(1,true);

    Student actual = sut.searchStudentById(1);
    assertThat(actual.isDeleted()).isTrue();
    Student after = sut.searchStudentById(1);

    assertThat(after.getStudentId()).isEqualTo(before.getStudentId());
    assertThat(after.getName()).isEqualTo(before.getName());
    assertThat(after.getFurigana()).isEqualTo(before.getFurigana());
    assertThat(after.getNickname()).isEqualTo(before.getNickname());
    assertThat(after.getMailAddress()).isEqualTo(before.getMailAddress());
    assertThat(after.getAddress()).isEqualTo(before.getAddress());
    assertThat(after.getAge()).isEqualTo(before.getAge());
    assertThat(after.getGender()).isEqualTo(before.getGender());
    assertThat(after.getRemark()).isEqualTo(before.getRemark());
  }

  @Test
  void 受講生の論理削除が正しくできること_復元(){
    Student before = sut.searchStudentById(1);
    sut.logicalDeleteStudent(1,false);

    Student actual = sut.searchStudentById(1);
    assertThat(actual.isDeleted()).isFalse();
    Student after = sut.searchStudentById(1);

    assertThat(after.getStudentId()).isEqualTo(before.getStudentId());
    assertThat(after.getName()).isEqualTo(before.getName());
    assertThat(after.getFurigana()).isEqualTo(before.getFurigana());
    assertThat(after.getNickname()).isEqualTo(before.getNickname());
    assertThat(after.getMailAddress()).isEqualTo(before.getMailAddress());
    assertThat(after.getAddress()).isEqualTo(before.getAddress());
    assertThat(after.getAge()).isEqualTo(before.getAge());
    assertThat(after.getGender()).isEqualTo(before.getGender());
    assertThat(after.getRemark()).isEqualTo(before.getRemark());
  }
}