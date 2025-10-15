package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.controller.request.UpdateStudentFieldRequest;
import raisetech.StudentManagement.controller.request.UpdateStudentsCoursesRequest;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.domain.UpdateStudentField;
import raisetech.StudentManagement.service.StudentService;

@RestController
public class StudentApiController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentApiController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  //受講生一覧表示
  @GetMapping("/api/students")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    List<Courses> courses = service.searchCourseList();


    return converter.convertStudentDetails(students, studentsCourses,courses);
  }

  //受講生単一検索
  @GetMapping("/api/students/{studentId}")
  public  ResponseEntity<StudentDetail> getStudent(@PathVariable int studentId){
    Student student = service.searchStudentById(studentId);
    List<StudentsCourses> studentsCourses = service.searchStudentsCoursesById(studentId);
    List<Courses> courses = service.searchCoursesByStudentId(studentId);

    StudentDetail studentDetail = converter.convertStudentDetails(student,studentsCourses,courses);

    return  ResponseEntity.ok(studentDetail);
  }

  //新規登録：受講生情報登録
  @PostMapping("/api/students")
  public ResponseEntity<StudentDetail> registerStudent(@Valid @RequestBody StudentDetail studentDetail) {
    Student student = converter.convertToStudent(studentDetail);
    Student registerStudent = service.registerStudent(student);

    studentDetail.setStudent(registerStudent);

    return ResponseEntity.ok(studentDetail);
  }


  //新規登録：受講コース登録
  @PostMapping("/api/students/courses")
  public ResponseEntity<StudentDetail> registerCourse(@Valid @RequestBody StudentDetail studentDetail) {
    List<StudentsCourses> studentsCourses = converter.convertToStudentsCourses(studentDetail);
   service.registerCourse(studentsCourses);

    int studentId = studentDetail.getStudent().getStudentId();
    Student student = service.searchStudentById(studentId);
    List<StudentsCourses> updatedStudentsCourses = service.searchStudentsCoursesById(studentId);
    List<Courses> updatedCourses = service.searchCoursesByStudentId(studentId);

    StudentDetail updateStudentDetail = converter.convertStudentDetails(student, updatedStudentsCourses, updatedCourses);

    return ResponseEntity.ok(updateStudentDetail);
  }

  //受講生情報更新処理(論理削除込み)
  @PatchMapping("/api/students")
  public ResponseEntity<String> updateField(@Valid @RequestBody UpdateStudentFieldRequest request) {
    int studentId = request.getStudentId();
    String field = request.getField();
    String value = request.getValue();

    //ホワイトリストチェック
    if (!UpdateStudentField.isValid(field)) {
      return ResponseEntity
          .badRequest()
          .body("更新可能なフィールドではありません: " + field);
    }

    service.updateStudentField(studentId, field, value);
    return ResponseEntity.ok("更新処理が成功しました");
  }

  //受講コース更新
  @PatchMapping("/api/students/courses")
  public ResponseEntity<StudentDetail> updateCourse(
      @Valid @RequestBody UpdateStudentsCoursesRequest request) {
    int studentId = request.getStudentId();
    List<Integer> courseIds = request.getCourseIds();
    service.updateCourse(studentId, courseIds);

    Student student = service.searchStudentById(studentId);
    List<StudentsCourses> studentsCourses = service.searchStudentsCoursesById(studentId);
    List<Courses> courses = service.searchCoursesByStudentId(studentId);

    StudentDetail studentDetail = converter.convertStudentDetails(student, studentsCourses, courses);

    return ResponseEntity.ok(studentDetail);
  }


}
