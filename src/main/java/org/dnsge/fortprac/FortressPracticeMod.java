package org.dnsge.fortprac;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FortressPracticeMod implements ModInitializer {

    public static final String WORLD_COUNT_KEY = "world_count";


    private static boolean currentlyPracticing = false;

    public static final Logger LOGGER = LogManager.getLogger("fortress-practice");

    @Override
    public void onInitialize() {
        FortressPracticeMod.initConfigStore();
        LOGGER.info("Initialized fortress-practice mod");
    }

    public static void setCurrentlyPracticing(boolean currentlyPracticing) {
        FortressPracticeMod.currentlyPracticing = currentlyPracticing;
    }

    public static boolean getCurrentlyPracticing() {
        return FortressPracticeMod.currentlyPracticing;
    }

    private static void initConfigStore() {
        (new File("config")).mkdir();
        (new File("config/fortress_practice")).mkdir();
    }

    public static File getPlayerStateFile() {
        return new File("config/fortress_practice/player_state.dat");
    }

    /**
     * @return The properties file, guaranteed to exist
     * @throws IOException if creating the file fails
     */
    private static File getPropertiesFile() throws IOException {
        var f = new File("config/fortress_practice/properties.properties");
        if (!f.exists()) {
            f.createNewFile();
        }
        return f;
    }

    private static Properties getProperties() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(getPropertiesFile())) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }
    }

    private static void saveProperties(Properties properties) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(getPropertiesFile())) {
            properties.store(outputStream, "fortress-practice mod properties");
        }
    }

    public static int getAndIncrementPracticeNumber() {
        try {
            Properties properties = getProperties();
            if (properties.isEmpty() || !properties.containsKey(WORLD_COUNT_KEY)) {
                properties.setProperty(WORLD_COUNT_KEY, String.valueOf(1));
                saveProperties(properties);
                return 1;
            }

            // Get and increment world count
            int current = Integer.parseInt(properties.getProperty(WORLD_COUNT_KEY));
            current++;

            // Replace and save with new value
            properties.setProperty(WORLD_COUNT_KEY, String.valueOf(current));
            saveProperties(properties);

            return current;
        } catch (IOException e) {
            LOGGER.error("Failed to get and increment practice number", e);
            return 0;
        }
    }
}
