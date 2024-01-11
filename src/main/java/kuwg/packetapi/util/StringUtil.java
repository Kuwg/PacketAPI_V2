package kuwg.packetapi.util;

import com.google.gson.Gson;

public class StringUtil {
    private static final Gson gson = new Gson();

    public static String toJson(String original) {
        return gson.toJson(original);
    }
}
