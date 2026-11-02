package org.etlforge.util.crypto;

public interface StringEncrypter {
   byte[] encrypt(String var1) throws StringEncryptionException;
}
