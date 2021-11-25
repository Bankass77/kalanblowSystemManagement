
package ml.kalanblowsystemmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum Gender {

    MALE("M", "Male"), FEMALE("F", "Female");

    @Setter
    private String valueGender;

    @Setter
    private String code;

    public static Gender fromCode(char code) {
        if (code == 'M' || code == 'm') {
            return MALE;
        }
        if (code == 'F' || code == 'f') {
            return FEMALE;
        }
        throw new UnsupportedOperationException("The code " + code + " is not supported!");
    }

    public static Gender getGenderByCode(String code) {
        for (Gender gender : Gender.values()) {
            if (gender.code.equals(code)) {
                return gender;
            }
        }
        return null;
    }
}
