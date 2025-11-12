package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            DateTimeFormatter dateTimeFormatter = null;
            jsonWriter.value(localDateTime.format(dateTimeFormatter));
            return;
        }
        jsonWriter.nullValue();
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() != null) {
            DateTimeFormatter dateTimeFormatter = null;
            return LocalDateTime.parse(jsonReader.nextString(), dateTimeFormatter);
        }
        jsonReader.nextNull();
        return null;
    }
}
