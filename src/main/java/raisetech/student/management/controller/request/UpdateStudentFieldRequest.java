package raisetech.student.management.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import raisetech.student.management.controller.validation.ValidFieldValue;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidFieldValue
public class UpdateStudentFieldRequest {

  @NotBlank(message = "値を入力してください")
  private String value;

  @NotBlank(message = "フィールド名を入力してください")
  @Pattern(regexp = "^(name|furigana|nickname|mailAddress|address|age|gender|remark)?$", message = "正しいフィールド名を入力してください")
  private String field;

  @NotNull(message = "StudentIdは必須です")
  private Integer studentId;


  public UpdateStudentFieldRequest(String field, String value) {
    this.field = field;
    this.value = value;
    this.studentId = 1; // テスト用に適当な値
  }
}
