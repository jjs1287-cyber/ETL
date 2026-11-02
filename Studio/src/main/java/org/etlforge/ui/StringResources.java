package org.etlforge.ui;

public interface StringResources {
   boolean contains(String var1);

   String getString(String var1);

   String getString(String var1, String var2);

   MnemonicString getMnemonicString(String var1);

   MnemonicStringFactory getMnemonicsFactory();
}
