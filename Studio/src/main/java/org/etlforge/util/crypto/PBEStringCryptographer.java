package org.etlforge.util.crypto;

public interface PBEStringCryptographer extends StringCryptographer {
   void setAlgorithm(String var1);

   void setPassphrase(String var1) throws StringCryptographerException;
}
