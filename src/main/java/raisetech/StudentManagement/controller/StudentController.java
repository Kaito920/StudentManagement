package raisetech.StudentManagement.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Courses;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
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

  // メニュー画面の表示
  @GetMapping("/studentMenu")
  public String studentMenuView() {
    return "studentMenu";
  }

  //受講生一覧表示(表示のみ、更新、削除対応)
  @GetMapping("/studentList")
  public String getStudentList(
      @RequestParam(value = "mode", required = false, defaultValue = "view") String mode,
      Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    model.addAttribute("mode", mode);

    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public List<StudentsCourses> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }

  //新規登録：受講生情報入力画面呼び出し
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  //新規登録：受講生情報登録　→　コース選択確認画面へ
  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    //Service経由でデータベースに保存
    Student student = converter.convertToStudent(studentDetail);
    service.registerStudent(student);

    return "redirect:/registerCourseConfirmView?studentId=" + student.getStudentId();
  }

  //新規登録：受講コース選択をするか確認する画面表示 Y/N
  @GetMapping("/registerCourseConfirmView")
  public String registerCourseConfirmView(@RequestParam("studentId") int studentId, Model model) {
    StudentDetail detail = new StudentDetail();
    Student student = new Student();
    student.setStudentId(studentId);
    detail.setStudent(student);

    model.addAttribute("studentDetail", detail);
    return "registerCourseConfirm";
  }

  //新規登録：Y/N選択に対応する画面へ移行
  //N：受講生一覧(終了)
  //Y：受講コース選択
  @PostMapping("/registerCourseConfirm")
  public String registerCourseConfirm(@ModelAttribute StudentDetail studentDetail) {
    int studentId = studentDetail.getStudent().getStudentId();
    return "redirect:/registerCourseView?studentId=" + studentId;
  }

  //新規登録：受講コース選択画面表示
  @GetMapping("/registerCourseView")
  public String registerCourseView(@RequestParam int studentId, Model model) {
    StudentDetail detail = new StudentDetail();
    Student student = new Student();
    detail.setStudent(student);
    student.setStudentId(studentId);

    model.addAttribute("studentDetail", detail);

    List<Courses> courseList = service.searchCourseList();
    model.addAttribute("courseList", courseList);

    return "registerCourse";
  }

  //新規登録：受講コース登録 → 受講生一覧表示
  @PostMapping("/registerCourse")
  public String registerCourse(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerCourse";
    }
    //Service経由でデータベースに保存
    List<StudentsCourses> studentsCourses = converter.convertToStudentsCourses(studentDetail);
    service.registerCourse(studentsCourses);

    return "redirect:/studentList";
  }

  //受講生情報更新：更新するCOLUMN選択画面表示
  @GetMapping("/updateStudent/{studentId}")
  public String updateStudent(@PathVariable int studentId, Model model) {
    StudentDetail detail = service.getStudentDetailById(studentId);
    model.addAttribute("studentDetail", detail);
    return "updateStudent";
  }

  //受講生フィールドの編集画面表示
  @GetMapping("/updateEachField/{field}/{studentId}")
  public String updateField(
      @PathVariable("studentId") int studentId,
      @PathVariable("field") String field,
      Model model) {
    Student student = service.searchStudentById(studentId);
    String label = "";
    String value = switch (field) {
      case "name" -> {
        label = "名前";
        yield student.getName();
      }
      case "furigana" -> {
        label = "カナ名";
        yield student.getFurigana();
      }
      case "nickname" -> {
        label = "ニックネーム";
        yield student.getNickname();
      }
      case "mailAddress" -> {
        label = "メールアドレス";
        yield student.getMailAddress();
      }
      case "address" -> {
        label = "地域";
        yield student.getAddress();
      }
      case "age" -> {
        label = "年齢";
        yield String.valueOf(student.getAge());
      }
      case "gender" -> {
        label = "性別";
        yield student.getGender();
      }
      case "remark" -> {
        label = "備考";
        yield student.getRemark();
      }
      default -> throw new IllegalArgumentException("不正なフィールド名です: " + field);
    };

    model.addAttribute("label", label);
    model.addAttribute("value", value);
    model.addAttribute("field", field);
    model.addAttribute("studentId", studentId);

    return "updateField";
  }

  //受講生情報更新処理
  @PostMapping("/updateField")
  public String updateField(
      @RequestParam int studentId,
      @RequestParam String field,
      @RequestParam String value) {
    service.updateStudentField(studentId, field, value);
    return "redirect:/updateStudent/" + studentId;
  }

  //コース情報更新画面表示
  @GetMapping("/updateCourseView")
  public String showUpdateCoursePage(
      @RequestParam int studentId, Model model) {
    Student student = service.searchStudentById(studentId);
    List<Courses> courseList = service.searchCourseList();

    model.addAttribute("student", student);
    model.addAttribute("studentId", studentId);
    model.addAttribute("courseList", courseList);

    return "updateCourse";
  }

  //受講コース更新
  @PostMapping("/updateCourse")
  public String updateCourse(
      @RequestParam int studentId,
      @RequestParam(value = "courseIds", required = false) List<Integer> courseIds) {
    if (courseIds == null) {
      courseIds = new ArrayList<>();
    }
    service.updateCourse(studentId, courseIds);
    return "redirect:/updateStudent/" + studentId;
  }

  //論理削除処理
  @PostMapping("/logicalDeleteStudent")
  public  String logicalDeleteStudent(@RequestParam(name = "studentIds",required = false)List<Integer>checkedStudentId){
    if (checkedStudentId != null && !checkedStudentId.isEmpty()){
      service.logicalDeleteStudent(checkedStudentId);
    }
    return  "redirect:/studentList?";
  }
}
