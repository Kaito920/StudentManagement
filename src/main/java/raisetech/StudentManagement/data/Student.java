package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

  private int studentId;

  @NotBlank
  private String name;

  @NotBlank
  private String furigana;

  private String nickname;

  @NotBlank
  @Email
  private String mailAddress;


  private String address;

  @NotNull
  @Positive
  private int age;

  @Size(max = 10)
  @Pattern(regexp = "^(男性|女性|その他)?$", message = "性別は「男性」「女性」「その他」のいずれかを指定してください")
  private String gender;

  @Size(max = 200)
  private String remark;

  private boolean isDeleted;


}
