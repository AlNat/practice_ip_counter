package dev.alnat.tools;

import dev.alnat.ipcounter.IPCounter;
import lombok.experimental.UtilityClass;

import java.io.File;

@UtilityClass
public class Tools {

    public static File getFile(String resourceName) {
        ClassLoader classLoader = IPCounter.class.getClassLoader();
        return new File(classLoader.getResource(resourceName).getFile());
    }

}
