package org.etlforge.util.preference;

import com.google.common.base.Preconditions;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.prefs.BackingStoreException;

public abstract class KongaPreferences {
   public static KongaPreferences getPreferences(Class<?> var0) {
      return getPreferences(var0, null);
   }

   public static KongaPreferences getPreferences(PreferencesKey var0) {
      return getPreferences(var0.keyClass(), var0.keySuffix());
   }

   public static KongaPreferences getPreferences(Class<?> var0, String var1) {
      Preconditions.checkNotNull(var0, "cls");
      return new DefaultKongaPreferences(var0, var1);
   }

   public abstract String[] getKeys() throws BackingStoreException;

   public abstract boolean containsKey(String var1);

   public abstract void removeKey(String var1);

   public abstract void removeNode() throws BackingStoreException;

   public abstract int getInt(String var1, int var2);

   public abstract void putInt(String var1, int var2);

   public abstract long getLong(String var1, long var2);

   public abstract void putLong(String var1, long var2);

   public abstract double getDouble(String var1, double var2);

   public abstract void putDouble(String var1, double var2);

   public abstract boolean getBoolean(String var1, boolean var2);

   public abstract void putBoolean(String var1, boolean var2);

   public abstract String getString(String var1, String var2);

   public abstract void putString(String var1, String var2);

   public abstract <E extends Enum<E>> E getEnum(String var1, Class<E> var2, E var3);

   public abstract <E extends Enum<E>> void putEnum(String var1, E var2);

   public abstract Dimension getDimension(String var1, Dimension var2);

   public abstract void putDimension(String var1, Dimension var2);

   public abstract Point getPoint(String var1, Point var2);

   public abstract void putPoint(String var1, Point var2);

   public abstract File getFile(String var1, File var2);

   public abstract void putFile(String var1, File var2);

   public abstract KongaPreferences child(String var1);

   public abstract boolean hasChild(String var1);
}
