package dev.alnat.ipcounter;

import dev.alnat.tools.IPTools;
import org.apache.commons.io.input.CharSequenceReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.alnat.tools.Tools.getFile;

class HLLIPCounterTest {

    private final IPCounter counter = new HLLIPCounter(HLLIPCounter.Size.BIG);


    @Test
    void testSingleIPInFile() {
        try {
            var count = counter.countUniqueIP(getFile("single_ip.txt"));
            Assertions.assertEquals(1, count);
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testSmallRangeInFile() {
        try {
            var count = counter.countUniqueIP(getFile("small_range.txt"));
            Assertions.assertEquals(255, count);
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }


    @Test
    void testSmallRangeInZIPFile() {
        try {
            var count = counter.countUniqueIP(getFile("simple_file.txt.zip"), true);
            Assertions.assertEquals(255, count);
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testSmallReputeRangeInFile() {
        try {
            var count = counter.countUniqueIP(getFile("repeate_small_range.txt"));
            Assertions.assertEquals(255, count);
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }


    @Test
    void testSomeIPRangeInReader() {
        Stream<String> ipAddresses = IPTools.getRange("192.168.0.1", 254);
        Reader targetReader = new CharSequenceReader(ipAddresses.collect(Collectors.joining("\n")));

        try {
            var count = counter.countUniqueIP(targetReader);
            Assertions.assertEquals(254, count);
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    /**
     * HLL is Probability algorithms, so we can't be sure, that count is accurate
     * so test only some limit
     */
    @ParameterizedTest
    @ValueSource(ints = {
            512,
            1_024,
            4_096,
            8_192,
            65_536,
            65_536,
            1_048_576,
            4_096_000,
            16_777_216
    })
    void testIPRange(Integer size) {
        Stream<String> ipAddresses = IPTools.getRange("192.168.0.1", size);
        Reader targetReader = new CharSequenceReader(ipAddresses.collect(Collectors.joining("\n")));

        try {
            var count = counter.countUniqueIP(targetReader);
            Assertions.assertTrue(Math.abs(size - count) < (size / 100)); // 1%
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0.0.0.0",
            "255.255.255.255",
            "0.0.0.255",
            "0.0.255.255",
            "0.255.255.255",
            "10.0.0.1",
            "192.168.0.1"
    })
    void testSingleBoundedIP(final String ip) {
        final Reader targetReader = new CharSequenceReader(ip);

        try {
            var count = counter.countUniqueIP(targetReader);
            Assertions.assertEquals(1, count);
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

}
