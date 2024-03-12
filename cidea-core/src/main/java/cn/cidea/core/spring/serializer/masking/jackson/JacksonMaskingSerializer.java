package cn.cidea.core.spring.serializer.masking.jackson;

import cn.cidea.core.spring.serializer.masking.MaskMethod;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * @author: CIdea
 */
public final class JacksonMaskingSerializer extends StdScalarSerializer<Object> {

    private final Class<? extends MaskMethod> mask;

    @SneakyThrows
    public JacksonMaskingSerializer(Class<? extends MaskMethod> mask) {
        super(String.class, false);
        this.mask = mask;
    }


    public boolean isEmpty(SerializerProvider prov, Object value) {
        String str = (String) value;
        return str.isEmpty();
    }

    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Object content = MaskMethod.get(mask).mask(value);
        gen.writeObject(content);
    }

    public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        this.serialize(value, gen, provider);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        this.visitStringFormat(visitor, typeHint);
    }
}
