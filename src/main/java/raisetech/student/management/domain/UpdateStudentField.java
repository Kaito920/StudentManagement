package raisetech.student.management.domain;

import lombok.Getter;

@Getter
public enum UpdateStudentField {
  NAME("name"),
  FURIGANA("furigana"),
  NICKNAME("nickname"),
  MAIL_ADDRESS("mailAddress"),
  ADDRESS("address"),
  AGE("age"),
  GENDER("gender"),
  REMARK("remark");

  private final  String fieldName;

  UpdateStudentField(String fieldName){
    this.fieldName = fieldName;
  }

  public static boolean isValid(String field) {
    for (UpdateStudentField f : values()) {
      if (f.getFieldName().equals(field)) {
        return true;
      }
    }
    return false;
  }

}
