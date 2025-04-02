package org.grupp2.sdpproject.Utils;

import javafx.scene.control.TextFormatter;

import java.math.BigDecimal;

public class TextformatUtil {

    public TextFormatter<Integer> byteFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change; // Tillåt att radera allt
            }
            if (newText.matches("^\\d*$")) { // Endast siffror
                try {
                    int value = Integer.parseInt(newText);
                    if (value >= 0 && value <= 255) { // Inom 1-byte-gränsen
                        return change;
                    }
                } catch (NumberFormatException e) {
                    return null; // Ignorera ogiltig inmatning
                }
            }
            return null; // Ignorera om det inte är siffror
        });
    }

    public TextFormatter<Short> shortFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change; // Tillåt att radera allt
            }
            if (newText.matches("^\\d*$")) { // Tillåter negativa och positiva siffror
                try {
                    int value = Integer.parseInt(newText);
                    if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) { // Kontrollera gränser för short
                        return change;
                    }
                } catch (NumberFormatException e) {
                    return null; // Ignorera ogiltig inmatning
                }
            }
            return null; // Ignorera om det inte är siffror eller "-"
        });
    }

    public TextFormatter<BigDecimal> bigDecimalFormatter(int precision, int scale) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change; // Tillåt att radera allt
            }
            if (newText.matches("^\\d{0," + (precision - scale) + "}(\\.\\d{0," + scale + "})?$")) { // Tillåter rätt format
                try {
                    BigDecimal value = new BigDecimal(newText);
                    // Beräkna maxvärdet dynamiskt
                    StringBuilder maxValueBuilder = new StringBuilder();
                    for (int i = 0; i < precision - scale; i++) {
                        maxValueBuilder.append('9');
                    }
                    if (scale > 0) {
                        maxValueBuilder.append('.');
                        for (int i = 0; i < scale; i++) {
                            maxValueBuilder.append('9');
                        }
                    }
                    BigDecimal maxValue = new BigDecimal(maxValueBuilder.toString());

                    // Kontrollera om värdet ligger inom intervallet
                    if (value.compareTo(BigDecimal.ZERO) >= 0 && value.compareTo(maxValue) <= 0) {
                        return change;
                    }
                } catch (NumberFormatException e) {
                    return null; // Ignorera ogiltig inmatning
                }
            }
            return null; // Ignorera om formatet är fel
        });
    }

}
