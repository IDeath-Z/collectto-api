package converters;

import java.util.UUID;

import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class UUIDConverter extends SimpleArgumentConverter {
    
    @Override
    protected Object convert(Object source, Class<?> targetType) {
        if (source == null)
            return null;
        
        return UUID.fromString((String) source);
    }
}