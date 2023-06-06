# IP Counter

The task from https://github.com/Ecwid/new-job/blob/master/IP-Addr-Counter.md

Simplify: There is a HUGE file with a lot of IP addresses. Calculate unique count of IPs in it.

Expected that's all the IPs is well-formatted and correct


## Usage

```
java -jar ip_counter.jar /FULL/PATH/TO/FILE.txt
```

if file names `*.zip` it will be opened too.

-------

## Implementation

### The first solution  --  BITSET

The idea that IP address can be present as integer value.
So we can fill bit array, when this IP found in file -- we will fill it number in this array.
And theoretically we can stop when we fill the all bits (not realistic -- local IP probably will be not included).
In fact - it's a simple bitmap implementation with 2 bitmap arrays (due to arrays size limits)

### The second solution  --  HyperLogLog

Probability algorithms, like `HyperLogLog`, due 2 guesses:
* Files are big, so probably cardinality is huge too
* Absolute cardinality solves only with counting unique hash, but we need more efficient way

Uses the common implementation of `HLL` and `murmur3` hashing

See also https://www.moderndescartes.com/essays/hyperloglog/

-------

### Memory consumption

Both solution one-pass processing, and requires less memory, then ordinal hashtable.
But the first solution guarantee accurate calculation, the other one -- only probability.

Theoretically memory consumption:
* BitSet -- 2ˆ32 (4,294,967,296) (due size of IPV4 addresses)
* HLL -- 2 ^ log2m * reg-width, for example 2ˆ14 * 4 = 2ˆ14 * 2ˆ2 = 2ˆ16

In this calculation not included some additional memory costs, e.g. zip streaming, array and other ds overhead, etc.

So, if we've got IPV4 only, and we need the exact count of unique IP -- BitMap is our choose, otherwise -- HLL.


#### Benchmark

Test file from the link. MBP16 as stand. fFew repeat, best value.

* BitSet -- Found 1 000 000 000 unique IP in 8 000 000 000 zipped IPs with time 23M 19S 
* HLL -- Found 1 000 115 043 unique IP in 8 000 000 000 zipped IPs with time 25M 46S
