package dev.alnat.ipcounter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

public interface IPCounter {

    Long countUniqueIP(File file) throws IOException;
    Long countUniqueIP(File file, boolean isZip) throws IOException;
    Long countUniqueIP(Reader reader) throws IOException;
    Long countUniqueIP(String filePath) throws IOException;
    Long countUniqueIP(String filePath, boolean isZip) throws IOException;

}
