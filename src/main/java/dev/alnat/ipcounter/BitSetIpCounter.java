package dev.alnat.ipcounter;

import dev.alnat.tools.IPTools;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;


/**
 * Counter based on bitset
 * IP count list is 2ˆ32, but array size is only 2ˆ31, so generate 2 bit array
 */
public class BitSetIpCounter extends FileReaderIPCounter implements IPCounter {

    private final BitSet negative;
    private final BitSet positive;

    public BitSetIpCounter() {
        negative = new BitSet(Integer.MAX_VALUE);
        positive = new BitSet(Integer.MAX_VALUE);
    }

    @Override
    protected Long countIP(Reader reader) throws IOException {
        long count = 0;

        try (LineIterator it = IOUtils.lineIterator(reader)) {
            while (it.hasNext()) {
                String line = it.nextLine();
                var i = IPTools.ipToNumber(line);
                if (i < 0) {
                    negative.set(-1 * i);
                } else {
                    positive.set(i);
                }

                count++;
                if(count % 100_000_000 == 0) {
                    System.out.println("Processed " + count);
                }
            }
        }

        System.out.println("Processed " + count);
        return Integer.toUnsignedLong(negative.cardinality()) + Integer.toUnsignedLong(positive.cardinality());
    }

    @Override
    protected Long countIP(File file) throws IOException {
        long count = 0;

        try (LineIterator it = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name())) {
            while (it.hasNext()) {
                String line = it.nextLine();
                var i = IPTools.ipToNumber(line);
                if (i < 0) {
                    negative.set(-1 * i);
                } else {
                    positive.set(i);
                }
            }

            count++;
            if(count % 100_000_000 == 0) {
                System.out.println("Processed " + count);
            }
        }

        System.out.println("Processed " + count);
        return Integer.toUnsignedLong(negative.cardinality()) + Integer.toUnsignedLong(positive.cardinality());
    }
}
