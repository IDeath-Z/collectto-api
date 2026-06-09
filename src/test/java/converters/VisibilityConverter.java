package converters;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

import com.collectto.api_collectto.domain.enums.Visibility;

public class VisibilityConverter extends TypedArgumentConverter<String, Visibility> {

    protected VisibilityConverter() {
        super(String.class, Visibility.class);
    }

    @Override
    protected Visibility convert(String source) throws ArgumentConversionException {
        if (source == null)
            return null;

        try {
            return Visibility.fromCode(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ArgumentConversionException("Unknown Visibility value: " + source, e);
        }
    }
}
