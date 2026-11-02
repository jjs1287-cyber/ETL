package org.etlforge.application.ui.window.editor.service;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.prefs.BackingStoreException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.util.preference.KongaPreferences;

public class EditorPreferencesMemento implements EditorMemento {
   private static final String LOCATION = "Location";
   private static final String SIZE = "Size";

   public void store(Editor var1, Point var2, Dimension var3) {
      try {
         KongaPreferences var4 = getNode(var1.getMementoPath(), false);
         if (var4 != null) {
            var4.putPoint("Location", var2);
            var4.putDimension("Size", var3);
         }
      } catch (Exception var5) {
         ErrorLog.log(this.getClass(), "Failed to store editor size and location in preferences", var5);
      }
   }

   public Rectangle getBounds(Editor var1, Rectangle var2) {
      try {
         KongaPreferences var3 = getNode(var1.getMementoPath(), true);
         if (var3 == null) {
            return var2;
         }

         Point var4 = var3.getPoint("Location", var2.getLocation());
         Dimension var5 = var3.getDimension("Size", var2.getSize());
         return new Rectangle(var4, var5);
      } catch (Exception var6) {
         ErrorLog.log(this.getClass(), "Failed to restore editor size and location from preferences", var6);
         return var2;
      }
   }

   @Override
   public void remove(String[] var1) {
      try {
         KongaPreferences var2 = getNode(var1, true);
         if (var2 != null) {
            var2.removeNode();
         }
      } catch (BackingStoreException var3) {
         var3.printStackTrace();
      }
   }

   private static KongaPreferences getNode(String[] var0, boolean var1) {
      if (var0.length == 0) {
         return null;
      }

      KongaPreferences var2 = KongaPreferences.getPreferences(EditorPreferencesMemento.class);
      KongaPreferences var3 = var2;

      for (String var7 : var0) {
         if (var1 && !var3.hasChild(var7)) {
            return null;
         }

         var3 = var3.child(var7);
      }

      return var3;
   }
}
