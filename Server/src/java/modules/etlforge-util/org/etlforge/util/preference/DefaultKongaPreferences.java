package org.etlforge.util.preference;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.commons.lang.StringUtils;

final class DefaultKongaPreferences extends KongaPreferences {
   private static final String NULL_FILE = "<NULL_FILE>";
   private static final String WIDTH = ".width";
   private static final String HEIGHT = ".height";
   private static final String X = ".X";
   private static final String Y = ".Y";
   private final Preferences userPrefs;

   public DefaultKongaPreferences(Class<?> var1) {
      this(var1, null);
   }

   public DefaultKongaPreferences(Class<?> var1, String var2) {
      String var3 = StringUtils.substringAfterLast(var1.getName(), ".");
      var3 = StringUtils.substringBefore(var3, "$");
      if (var2 != null) {
         var3 = var3 + var2;
      }

      this.userPrefs = Preferences.userNodeForPackage(var1).node(var3);
   }

   private DefaultKongaPreferences(Preferences var1) {
      this.userPrefs = var1;
   }

   @Override
   public KongaPreferences child(String var1) {
      return new DefaultKongaPreferences(this.userPrefs.node(var1));
   }

   @Override
   public boolean hasChild(String var1) {
      try {
         return this.userPrefs.nodeExists(var1);
      } catch (BackingStoreException var3) {
         return false;
      }
   }

   @Override
   public String[] getKeys() throws BackingStoreException {
      return this.userPrefs.keys();
   }

   @Override
   public boolean containsKey(String var1) {
      try {
         String[] var2 = this.userPrefs.keys();

         for (String var6 : var2) {
            if (var6.equals(var1)) {
               return true;
            }
         }
      } catch (BackingStoreException var7) {
         System.err.println(var7.getMessage());
      }

      return false;
   }

   @Override
   public void removeNode() throws BackingStoreException {
      this.userPrefs.removeNode();
   }

   @Override
   public void removeKey(String var1) {
      this.userPrefs.remove(var1);
      this.flush();
   }

   @Override
   public int getInt(String var1, int var2) {
      return this.userPrefs.getInt(var1, var2);
   }

   @Override
   public void putInt(String var1, int var2) {
      this.userPrefs.putInt(var1, var2);
      this.flush();
   }

   @Override
   public long getLong(String var1, long var2) {
      return this.userPrefs.getLong(var1, var2);
   }

   @Override
   public void putLong(String var1, long var2) {
      this.userPrefs.putLong(var1, var2);
      this.flush();
   }

   @Override
   public double getDouble(String var1, double var2) {
      return this.userPrefs.getDouble(var1, var2);
   }

   @Override
   public void putDouble(String var1, double var2) {
      this.userPrefs.putDouble(var1, var2);
      this.flush();
   }

   @Override
   public boolean getBoolean(String var1, boolean var2) {
      return this.userPrefs.getBoolean(var1, var2);
   }

   @Override
   public void putBoolean(String var1, boolean var2) {
      this.userPrefs.putBoolean(var1, var2);
      this.flush();
   }

   @Override
   public String getString(String var1, String var2) {
      return this.userPrefs.get(var1, var2);
   }

   @Override
   public void putString(String var1, String var2) {
      if (var2 != null) {
         this.userPrefs.put(var1, var2);
         this.flush();
      } else {
         this.removeKey(var1);
      }
   }

   @Override
   public Dimension getDimension(String var1, Dimension var2) {
      String var3 = this.userPrefs.get(var1 + ".width", "");
      if (var3.length() > 0) {
         String var4 = this.userPrefs.get(var1 + ".height", "");
         if (var4.length() > 0) {
            try {
               return new Dimension(Integer.parseInt(var3), Integer.parseInt(var4));
            } catch (NumberFormatException var6) {
            }
         }
      }

      return var2;
   }

   @Override
   public void putDimension(String var1, Dimension var2) {
      if (var2 != null) {
         String var3 = String.valueOf(var2.width);
         String var4 = String.valueOf(var2.height);
         this.userPrefs.put(var1 + ".width", var3);
         this.userPrefs.put(var1 + ".height", var4);
         this.flush();
      } else {
         this.removeKey(var1);
      }
   }

   @Override
   public Point getPoint(String var1, Point var2) {
      String var3 = this.userPrefs.get(var1 + ".X", "");
      if (var3.length() > 0) {
         String var4 = this.userPrefs.get(var1 + ".Y", "");
         if (var4.length() > 0) {
            try {
               return new Point(Integer.parseInt(var3), Integer.parseInt(var4));
            } catch (NumberFormatException var6) {
            }
         }
      }

      return var2;
   }

   @Override
   public void putPoint(String var1, Point var2) {
      if (var2 != null) {
         String var3 = String.valueOf(var2.x);
         String var4 = String.valueOf(var2.y);
         this.userPrefs.put(var1 + ".X", var3);
         this.userPrefs.put(var1 + ".Y", var4);
         this.flush();
      } else {
         this.removeKey(var1);
      }
   }

   @Override
   public File getFile(String var1, File var2) {
      String var3 = this.userPrefs.get(var1, "<NULL_FILE>");
      return "<NULL_FILE>".equals(var3) ? var2 : new File(var3);
   }

   @Override
   public void putFile(String var1, File var2) {
      if (var2 != null) {
         this.userPrefs.put(var1, var2.getAbsolutePath());
         this.flush();
      } else {
         this.removeKey(var1);
      }
   }

   @Override
   public <E extends Enum<E>> E getEnum(String var1, Class<E> var2, E var3) {
      String var4 = this.userPrefs.get(var1, null);
      if (var4 != null) {
         for (Enum var8 : (Enum[])var2.getEnumConstants()) {
            if (var8.name().equals(var4)) {
               return (E)var8;
            }
         }
      }

      return (E)var3;
   }

   @Override
   public <E extends Enum<E>> void putEnum(String var1, E var2) {
      if (var2 != null) {
         this.putString(var1, var2.name());
      } else {
         this.removeKey(var1);
      }
   }

   private void flush() {
      try {
         this.userPrefs.flush();
      } catch (BackingStoreException var2) {
         System.err.println(var2.getMessage());
      }
   }
}
