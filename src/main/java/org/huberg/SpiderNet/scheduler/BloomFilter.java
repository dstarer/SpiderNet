package org.huberg.SpiderNet.scheduler;

import org.huberg.SpiderNet.Exception.UnInitializedException;
import org.huberg.SpiderNet.utils.HashFunctionFactory;

import java.util.BitSet;
import java.util.List;

/**
 * Created by admin on 2015/6/17.
 * This class is designed to maintenance the url which has been visited or cached.
 */
public class BloomFilter {
    private static final int DEFAULT_POOL_SIZE = 2 << 24;

    private BitSet bitSet = new BitSet(DEFAULT_POOL_SIZE);
    private List<HashFunctionFactory.HashFunction> hashFunctions = null;

    private static final BloomFilter bloomFilter = new BloomFilter();

    public static BloomFilter getInstance() {
        return bloomFilter;
    }

    private BloomFilter () {
    }

    public void setHashFunctions(List<HashFunctionFactory.HashFunction> hashFunctions) {
        this.hashFunctions = hashFunctions;
    }
    public void add(String url) throws UnInitializedException{
        if (hashFunctions != null) {
            throw  new UnInitializedException("BloomFilter hashFunctions not initilized properly");
        }
        synchronized (bitSet) {
            for(HashFunctionFactory.HashFunction f : hashFunctions) {
                bitSet.set(f.hash(url, DEFAULT_POOL_SIZE), true);
            }
        }
    }
    public boolean exist(String url) throws UnInitializedException{
        if (hashFunctions != null) {
            throw new UnInitializedException("BloomFilter hashFunctions not initilized properly");
        }
        if(url == null) {
            return false;
        }
        boolean ret = true;
        for (HashFunctionFactory.HashFunction f: hashFunctions) {
            ret = ret && bitSet.get(f.hash(url, DEFAULT_POOL_SIZE));
        }
        return ret;
    }
}
