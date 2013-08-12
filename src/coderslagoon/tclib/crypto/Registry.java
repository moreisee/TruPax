/*
Copyright 2010-2013 CODERSLAGOON

This file is part of TruPax.

TruPax is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

TruPax is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
TruPax. If not, see http://www.gnu.org/licenses/.
*/

package coderslagoon.tclib.crypto;

import java.util.HashMap;
import java.util.Map;

import coderslagoon.tclib.util.TCLibException;

/**
 * To store algorithm classes which can then be used for header decryption
 * attempts, where different combinations of hash functions and block ciphers
 * have to be tried out. 
 * @param <T> The algorithm class the registry is storing.
 */
public class Registry<T extends Algorithm> {
    private Map<String, Class<? extends T>> map = new
        HashMap<String, Class<? extends T>>();

    /**
     * Add a class to the registry.
     * @param clazz The class to add
     * @param test True if the implementation should be tested first. This is
     * done by creating an instance and then calling its test method.
     * @throws TCLibException If any error occurred (during testing).
     */
    public void add(Class<? extends T> clazz, boolean test) throws TCLibException {
        try {
            T alg = clazz.newInstance();
            if (test) {
                alg.test();
            }
            this.map.put(alg.name(), clazz);
        }
        catch (TCLibException tle) {
            throw tle;
        }
        catch (Throwable err) {
            throw new TCLibException(err);
        }
    }

    /**
     * Look up an algorithm.
     * @param name The algorithm name.
     * @return The associated class or null if not found.
     * @throws TCLibException
     */
    public Class<? extends T> lookup(String name) {
        return this.map.get(name);
    }

    /**
     * @return Names of all the registered algorithms.
     */
    public String[] names() {
        return this.map.keySet().toArray(new String[0]);
    }

    ///////////////////////////////////////////////////////////////////////////

    /** All of the supported block ciphers. */ 
    public final static Registry<BlockCipher  > _blockCiphers  = new Registry<BlockCipher  >();
    /** All of the supported hash functions. */ 
    public final static Registry<Hash.Function> _hashFunctions = new Registry<Hash.Function>();

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Sets up the global registries. Should be called at process start time.
     * @param test True if the algorithms should be tested first.
     * @throws TCLibException If any error occurred (during testing).
     */
    public static void setup(boolean test) throws TCLibException {
        clear();

        _blockCiphers.add(AES256.class, test);

        _hashFunctions.add(RIPEMD160.class, test);
    }

    /**
     * Unregisters all of the algorithms from the global registries. 
     */
    public static void clear() {
        _blockCiphers .map.clear();
        _hashFunctions.map.clear();
    }
}
