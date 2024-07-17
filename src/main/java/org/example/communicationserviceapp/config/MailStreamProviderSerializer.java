package org.example.communicationserviceapp.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.mail.Provider;
import org.eclipse.angus.mail.util.MailStreamProvider;

import java.io.IOException;
import java.lang.reflect.Field;

public class MailStreamProviderSerializer extends StdSerializer<MailStreamProvider> {

    public MailStreamProviderSerializer() {
        this(null);
    }

    public MailStreamProviderSerializer(Class<MailStreamProvider> t) {
        super(t);
    }

    @Override
    public void serialize(MailStreamProvider value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        // Use reflection to get all fields
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                // Ensure fields are accessible if private
                field.setAccessible(true);
                Object fieldValue = field.get(value);

                // Handle special cases like jakarta.mail.Provider$Type
                if (field.getType() == Provider.Type.class) {
                    Provider.Type type = (Provider.Type) fieldValue;
                    gen.writeObjectField(field.getName(), type.toString());
                } else {
                    // Write field name and value
                    gen.writeObjectField(field.getName(), fieldValue);
                }
            } catch (IllegalAccessException e) {
                throw new IOException("Could not access field " + field.getName(), e);
            }
        }

        gen.writeEndObject();
    }
}
