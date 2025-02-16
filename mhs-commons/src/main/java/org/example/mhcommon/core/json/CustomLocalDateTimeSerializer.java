package org.example.mhcommon.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/*
1 serializer tuy chinh de  chuyen doi LocalDateTime thanh JSON co dinh dang mong muon
 */
public class CustomLocalDateTimeSerializer
        extends StdSerializer<LocalDateTime> {

    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]"); //su dung pattern nay

    public CustomLocalDateTimeSerializer() {
        this(null);
    }

    public CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    /*
    phuong thuc nay ghi LocalDateTime vao JSON duoi dang chuoi
    formatter.format(value) se chuyen doi LocalDateTime thanh chuoi
    gen.writeString(...) ghi chuoi do vao JSON
     */
    @Override
    public void serialize(
            LocalDateTime value,
            JsonGenerator gen,
            SerializerProvider arg2)
            throws IOException, JsonProcessingException {

        gen.writeString(formatter.format(value));
    }
}
