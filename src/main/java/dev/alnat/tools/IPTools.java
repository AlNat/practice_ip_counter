package dev.alnat.tools;

import com.google.common.net.InetAddresses;
import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@UtilityClass
public class IPTools {

    public static int ipToNumber(String ip) {
        var address = InetAddresses.forString(ip);
        return ByteBuffer.wrap(address.getAddress()).getInt();
    }

    public static String numberToIP(int value) {
        var i = InetAddresses.fromInteger(value);
        return i.getHostAddress();
    }

    public static Stream<String> getRange(String ip, Integer count) {
        var first = ipToNumber(ip);
        return IntStream
                .rangeClosed(first, first + count - 1)
                .boxed()
                .map(IPTools::numberToIP);
    }

}
