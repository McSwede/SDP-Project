package org.grupp2.sdpproject.Utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.grupp2.sdpproject.ENUM.Rating;

@Converter(autoApply = true)
public class EnumConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        if (rating == null) {
            return null;
        }
        // Convert Java Enum to MySQL-friendly String
        return rating.name().replace("_", "-");
    }

    @Override
    public Rating convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        // Convert MySQL ENUM string back to Java Enum
        return Rating.valueOf(dbData.replace("-", "_"));
    }


}
