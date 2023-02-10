package cn.cidea.core.spring.serializer.masking.jackson;

import cn.cidea.core.spring.serializer.masking.DataMaskingFunc;
import cn.cidea.core.spring.serializer.masking.DataMaskingOperation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: CIdea
 */
public final class DataMaskingSerializer extends StdScalarSerializer<Object> {
    private final DataMaskingOperation operation;

    public DataMaskingSerializer() {
        super(String.class, false);
        this.operation = null;
    }

    public DataMaskingSerializer(DataMaskingOperation operation) {
        super(String.class, false);
        this.operation = operation;
    }


    public boolean isEmpty(SerializerProvider prov, Object value) {
        String str = (String) value;
        return str.isEmpty();
    }

    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (Objects.isNull(operation)) {
            String content = DataMaskingFunc.ALL_MASK.getOperation().mask((String) value);
            gen.writeString(content);
        } else {
            String content = operation.mask((String) value);
            gen.writeString(content);
        }
    }

    public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        this.serialize(value, gen, provider);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        this.visitStringFormat(visitor, typeHint);
    }
}
