package amit.irltime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE =
            new File("./config/irltime.json");

    public static IRLTimeConfig loadConfig() {
        if (!CONFIG_FILE.exists()) {
            saveConfig(new IRLTimeConfig()); // create default file
            return new IRLTimeConfig();
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, IRLTimeConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new IRLTimeConfig();
        }
    }

    public static void saveConfig(IRLTimeConfig config) {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
