package ml.kalanblowSystemManagement.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

import ml.kalanblowSystemManagement.model.Gender;

@Component
@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Character> {

	@Override
	public Character convertToDatabaseColumn(Gender attribute) {

		if (attribute == null) {
			return null;
		}
		return attribute.getValueGender().charAt(0);
	}

	@Override
	public Gender convertToEntityAttribute(Character dbData) {
		if (dbData == null) {
			return null;
		}
		return Gender.fromCode(dbData);
	}
}
