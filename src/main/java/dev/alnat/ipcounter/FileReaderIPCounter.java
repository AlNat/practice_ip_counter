package dev.alnat.ipcounter;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

/**
 * Base class, provides file reading feature
 */
public abstract class FileReaderIPCounter implements IPCounter {

    @Override
    public Long countUniqueIP(final String filePath) throws IOException {
        return countUniqueIP(filePath, false);
    }

    @Override
    public Long countUniqueIP(File file, boolean isZip) throws IOException {
        return countUniqueIP(file.getPath(), isZip);
    }

    @Override
    public Long countUniqueIP(final Reader reader) throws IOException {
        return countIP(reader);
    }

    @Override
    public Long countUniqueIP(final File file) throws IOException {
        return countIP(file);
    }

    @Override
    public Long countUniqueIP(final String filePath, final boolean isZip) throws IOException {
        final Path path = Paths.get(filePath);

        if (!isZip) {
            return countIP(new File(path.toUri()));
        }

        try(var zipFile = new ZipFile(new File(path.toUri()))) {
            var entry = zipFile.entries().nextElement();

            Reader zipReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
            return countIP(zipReader);
        }
    }


    protected abstract Long countIP(final Reader reader) throws IOException;
    protected abstract Long countIP(final File file) throws IOException;


}
