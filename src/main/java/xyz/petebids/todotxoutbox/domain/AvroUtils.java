package xyz.petebids.todotxoutbox.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import xyz.petebids.todotxoutbox.TodoEvent;

import java.io.ByteArrayOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AvroUtils {

    @SneakyThrows

    public static byte[] eventToBytes(TodoEvent todoEvent) {

        SpecificDatumWriter<TodoEvent> writer = new SpecificDatumWriter<>(TodoEvent.class);
        byte[] data;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        final BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(stream, null);

        writer.write(todoEvent, binaryEncoder);
        binaryEncoder.flush();
        data = stream.toByteArray();
        return data;
    }
}
