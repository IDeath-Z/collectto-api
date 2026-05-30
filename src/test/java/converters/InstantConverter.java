package converters;

import java.time.Instant;

import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class InstantConverter extends SimpleArgumentConverter {
    
    @Override
    protected Object convert(Object source, Class<?> targetType) {
        if (source == null)
            return null;
        
        return Instant.parse((String) source);
    }
}
