package org.huberg.SpiderNet.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/6/17.
 */
public class HashFunctionFactory {

    private static List<HashFunction> hashFunctions = new ArrayList<HashFunction>();
    private static List<HashFunction> BKDRFunctions = new ArrayList<HashFunction>();

    static {
        hashFunctions.add(new SDBMHash());
        hashFunctions.add(new DJBHash());
        hashFunctions.add(new RSHash());
        hashFunctions.add(new JSHash());
        hashFunctions.add(new BKDRHash());
        hashFunctions.add(new APHash());

        BKDRFunctions.add(new BKDRHash(31));
        BKDRFunctions.add(new BKDRHash(131));
        BKDRFunctions.add(new BKDRHash(1313));
        BKDRFunctions.add(new BKDRHash(13131));
        BKDRFunctions.add(new BKDRHash(131313));
        BKDRFunctions.add(new BKDRHash(1313133));

    }

    public static interface HashFunction{
        int hash(String str, int m);
    }
    private static class SDBMHash implements HashFunction {
        public int hash(String str, int m) {
            int value = 0;
            for(int i = 0; i < str.length(); ++ i) {
                value = str.charAt(i) + (value << 6) + (value<<16) - value;
            }
            return (m-1) & value;
        }
    }
    private static class DJBHash implements HashFunction {
        public int hash(String str, int m) {
            int value = 5831;
            for(int i = 0; i < str.length(); ++ i) {
                value = ((value << 5) + value) + str.charAt(i);
            }
            return (m-1) & value;
        }
    }
    private static class RSHash implements HashFunction {
        public int hash(String str, int m) {
            int b = 378551;
            int a = 63689;
            int value = 0;
            for (int i = 0; i < str.length(); ++ i) {
                value = value * a + str.charAt(i);
                a = a * b;
            }
            return value & (m-1);
        }
    }
    private static class JSHash implements HashFunction {
        public int hash(String str, int m) {
            int value = 1315423911;
            for(int i = 0; i < str.length(); ++ i) {
                value ^= ((value << 5) + str.charAt(i) + (value >> 2));
            }
            return value & (m-1);
        }
    }
    private static class BKDRHash implements HashFunction {
        private int seed;
        public BKDRHash() {
            seed = 131;
        }
        public BKDRHash(int seed) {
            this.seed = seed;
        }
        public int hash(String str, int m) {
            int value = 0;
            for(int i = 0; i < str.length(); ++ i) {
                value = value * seed + str.charAt(i);
            }
            return value & (m - 1);
        }
    }
    private static class APHash implements HashFunction {
        public int hash(String str, int m) {
            int value = 0;
            for (int i = 0; i < str.length(); ++ i) {
                if ((i&1) == 0) {
                    value ^= ((value << 7) ^ (str.charAt(i)) ^ (value >> 3));
                } else {
                    value ^= (~((value << 11) ^ (str.charAt(i)) ^ (value >> 5)));
                }
            }
            return value & (m-1);
        }
    }

    public static List<HashFunction> getHashFunctions() {
        return hashFunctions;
    }

    public static List<HashFunction> getBKDRHashFunctions() {
        return BKDRFunctions;
    }
}
