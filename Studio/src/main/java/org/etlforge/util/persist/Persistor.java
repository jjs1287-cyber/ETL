package org.etlforge.util.persist;

public interface Persistor {
   String RESERVED_KEY_PREFIX = "konga.";

   String getName();

   Persistor createChild(String var1);

   Persistor getFirstChild(String var1);

   Iterable<Persistor> getChildren(String var1);

   Iterable<Persistor> getAllChildren();

   void removeChildren(String var1);

   void removeAllChildren();

   void putString(String var1, String var2);

   String getString(String var1);

   void putText(String var1, String var2);

   String getText(String var1);

   void putInteger(String var1, int var2);

   Integer getInteger(String var1);

   void putLong(String var1, long var2);

   Long getLong(String var1);

   void putDouble(String var1, double var2);

   Double getDouble(String var1);

   void putBoolean(String var1, boolean var2);

   Boolean getBoolean(String var1);
}
