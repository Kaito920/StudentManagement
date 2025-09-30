package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentsCourse> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));

    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public List<StudentsCourse> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }

  //受講生情報入力画面呼び出し
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  //受講生情報登録　→　コース選択確認画面へ
  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    //Service経由でデータベースに保存
    Student student = converter.convertToStudent(studentDetail);
    service.registerStudent(student);

    return "redirect:/registerCourseConfirmView?studentId="+ student.getStudentId();
  }

  //受講コース選択をするか確認する画面表示 Y/N
  @GetMapping("/registerCourseConfirmView")
  public String registerCourseConfirmView(@RequestParam("studentId") int studentId, Model model) {
    StudentDetail detail = new StudentDetail();
    Student student = new Student();
    student.setStudentId(studentId);
    detail.setStudent(student);

    model.addAttribute("studentDetail", detail);
    return "registerCourseConfirm";
  }

  //Y/N選択に対応する画面へ移行
  //N：受講生一覧(終了)
  //Y：受講コース選択
  @PostMapping("/registerCourseConfirm")
  public String registerCourseConfirm(@ModelAttribute StudentDetail studentDetail) {
    int studentId = studentDetail.getStudent().getStudentId();
    return "redirect:/registerCourseView?studentId="+ studentId;
  }

  //受講コース選択画面表示
  @GetMapping("/registerCourseView")
  public String registerCourseView(@RequestParam int studentId, Model model) {
    StudentDetail detail = new StudentDetail();
    Student student = new Student();
    detail.setStudent(student);
    student.setStudentId(studentId);

    model.addAttribute("studentDetail", detail);

    List<Courses> courseList =service.searchCourseList();
    model.addAttribute("courseList", courseList);

    return "registerCourse";
  }

  //受講コース登録 → 受講生一覧表示
  @PostMapping("/registerCourse")
  public String registerCourse(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerCourse";
    }
    //Service経由でデータベースに保存
    List<StudentsCourse> studentsCourses = converter.convertToStudentsCourses(studentDetail);
    service.registerCourse(studentsCourses);

    return "redirect:/studentList";
  }


}