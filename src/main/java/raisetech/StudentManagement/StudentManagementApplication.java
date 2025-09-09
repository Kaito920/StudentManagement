package raisetech.StudentManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  @Autowired
  private StudentRepository repository;


  public static void main(String[] args) {
    //localhost:8080
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/Student")
  public String getStudent(@RequestParam String name) {
    Student student = repository.searchByName(name);
    return student.getName() + " " + student.getAge() + "歳";
  }

  @PostMapping("/Student")
  public void registerStudent(String name, int age) {
    repository.registerStudent(name, age);
  }

  @PatchMapping("/Student")
  public void updateStudent(String name, int age) {
    repository.updateStudent(name, age);
  }

  @DeleteMapping("/Student")
  public void deleteStudent(String name) {
    repository.deleteStudent(name);
  }

}
