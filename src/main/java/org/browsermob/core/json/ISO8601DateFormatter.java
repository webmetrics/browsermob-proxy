package com.browsermob.core.json;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.ScalarSerializerBase;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;

public class ISO8601DateFormatter extends ScalarSerializerBase<Date> {
    public final static ISO8601DateFormatter instance = new ISO8601DateFormatter();

    public ISO8601DateFormatter() {
        super(java.util.Date.class);
    }

    @Override
    public void serialize(java.util.Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        DateFormat df = (DateFormat) provider.getConfig().getDateFormat().clone();
        jgen.writeString(df.format(value));
    }


    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        return createSchemaNode("string", true);
    }

}
