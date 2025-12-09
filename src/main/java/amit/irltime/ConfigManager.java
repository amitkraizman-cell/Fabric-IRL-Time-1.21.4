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
            IRLTimeConfig cfg = new IRLTimeConfig();
            saveConfig(cfg); // create default file
            return cfg;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            IRLTimeConfig cfg = GSON.fromJson(reader, IRLTimeConfig.class);

            // safety: if old config had string / invalid value, enum might be null
            if (cfg.corner == null) {
                cfg.corner = IRLTimeConfig.Corner.TOP_RIGHT;
            }

            return cfg;
        } catch (Exception e) {
            e.printStackTrace();
            // fall back to default config
            IRLTimeConfig cfg = new IRLTimeConfig();
            saveConfig(cfg);
            return cfg;
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
