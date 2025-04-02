package org.grupp2.sdpproject.Utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class StringListConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return ""; // return an empty string if the attribute is null or empty
        }
        // Join the list into a comma-separated string
        return attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return ""; // return an empty string if the database value is null or empty
        }
        // In this case, we are not actually splitting the data, as it's a String value
        return dbData;
    }
}
