package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class JsonExporter {
    public static void export(List<GeoIPService.GeoInfo> data, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(data, writer);
            System.out.println("GeoIP attack data exported to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }
}
