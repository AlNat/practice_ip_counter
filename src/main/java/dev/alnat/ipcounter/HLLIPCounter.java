package dev.alnat.ipcounter;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.agkn.hll.HLL;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HLLIPCounter extends FileReaderIPCounter implements IPCounter {

    private final Integer log2m;
    private final Integer registerWidth;

    public HLLIPCounter() {
        this(Size.ORDINAL);
    }


    public HLLIPCounter(Size size) {
        this.log2m = size.getLog2m();
        this.registerWidth = size.getRegisterWidth();
    }


    @Override
    protected Long countIP(final File file) throws IOException {
        final HLL hll = new HLL(log2m, registerWidth);

        try (LineIterator it = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name())) {
            while (it.hasNext()) {
                String line = it.nextLine();
                var l = Hashing.murmur3_128().newHasher().putString(line, StandardCharsets.UTF_8).hash().asLong();
                hll.addRaw(l);
            }
        }

        return hll.cardinality();
    }

    @Override
    protected Long countIP(final Reader reader) throws IOException {
        final HLL hll = new HLL(log2m, registerWidth);

        try (LineIterator it = IOUtils.lineIterator(reader)) {
            while (it.hasNext()) {
                String line = it.nextLine();
                var l = Hashing.murmur3_128().newHasher().putString(line, StandardCharsets.UTF_8).hash().asLong();
                hll.addRaw(l);
            }
        }

        return hll.cardinality();
    }

    @Getter
    @RequiredArgsConstructor
    public enum Size {
        TINY(5, 3),
        SMALL(13, 3),
        ORDINAL(13, 4),
        BIG(17, 5),
        HUGE(23, 7),
        GIGANTIC(30, 8);

        private final Integer log2m;
        private final Integer registerWidth;
    }

}
