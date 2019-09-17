package jukebox;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

public class Util {

    private static final String UTF8 = "UTF-8";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    public static <T> T readLocalJSONFileToObject(String localJsonFile, Class<T> clazz) throws IOException {
        InputStream inputStream = readFromLocalResourceToInputStream(localJsonFile);
        String jsonString = transformInputStreamToString(new BufferedInputStream(inputStream));
        return readJSONToObject(jsonString, clazz);
    }

    public static InputStream readFromLocalResourceToInputStream(String localResourceFile) throws FileNotFoundException {
        InputStream in = Util.class.getResourceAsStream(localResourceFile);
        if (in == null) {
            throw new FileNotFoundException(String.format("Resource not found: %s", localResourceFile));
        }
        return in;
    }

    public static String transformInputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(UTF8);
    }

    public static <T> T readJSONToObject(String jsonString, Class<T> clazz) throws IOException {
        return mapper.readValue(jsonString, clazz);
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() <= 0;
    }

    public static boolean isNullOrEmpty(List list) {
        return list == null || list.size() <= 0;
    }
}
