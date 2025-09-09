package raisetech.StudentManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  private String name = "Kouji ENAMI";
  private String age = "37";

  public static void main(String[] args) {
    //localhost:8080
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/StudentInfo")
  public String getStudentInfo() {
    return name + " " + age + "歳";
  }

  @PostMapping("/StudentInfo")
  public void setStudentInfo(String name, String age) {
    this.name = name;
    this.age = age;
  }

  @PostMapping("/StudentName")
  public void updateStudentName(String name) {
    this.name = name;
  }

}
