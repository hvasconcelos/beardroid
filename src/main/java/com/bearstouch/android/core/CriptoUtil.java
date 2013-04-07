/**
 * Copyright (C) 2013
 * Bearstouch Software : <mail@bearstouch.com>
 *
 * This file is part of Bearstouch Android Lib.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bearstouch.android.core;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CriptoUtil {
    /**
     * @param val
     * @return
     */
    public static String md5Digest(String val) {
        String result = null;

        if ((val != null) && (val.length() > 0)) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(val.getBytes());
                byte[] md5byte = md5.digest();
                BigInteger bigInt = new BigInteger(1, md5byte);
                String hashtext = bigInt.toString(16);
                // Now we need to zero pad it if you actually want the full 32 chars.
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                result = hashtext;
            } catch (NoSuchAlgorithmException nsae) {
                result = val.substring(0, 32);
            }
        }
        return result;
    }

    /**
     * @param val
     * @return
     */
    public static String sha1Digest(String val) {
        String result = null;

        if ((val != null) && (val.length() > 0)) {
            try {
                MessageDigest sha1digest = MessageDigest.getInstance("SHA-1");
                sha1digest.update(val.getBytes("UTF8"));
                byte[] sha1byte = sha1digest.digest();
                BigInteger bigInt = new BigInteger(1, sha1byte);
                String hashtext = bigInt.toString(16);
                // Now we need to zero pad it if you actually want the full 32 chars.
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                result = hashtext;
            } catch (Exception nsae) {
                result = val.substring(0, 32);
            }
        }
        return result;
    }
}
