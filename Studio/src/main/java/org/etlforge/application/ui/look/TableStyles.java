package org.etlforge.application.ui.look;

import javax.swing.JTable;
import javax.swing.UIManager;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

public final class TableStyles {
   static final String NARROW = "konga.tablestyle.narrow";
   static final String NORMAL = "konga.tablestyle.normal";
   static final String WIDE = "konga.tablestyle.wide";
   private static final TableStyle NO_STYLE = new TableStyle() {
      @Override
      public void addRendererPreparer(TableRendererPreparer var1) {
      }

      @Override
      public void makeOver(JTable var1) {
      }

      @Override
      public void setGridMode(TableGridMode var1) {
      }
   };

   public static TableStyle wide() {
      return createStyle(UIManager.getString("konga.tablestyle.wide"));
   }

   public static TableStyle normal() {
      return createStyle(UIManager.getString("konga.tablestyle.normal"));
   }

   public static TableStyle narrow() {
      return createStyle(UIManager.getString("konga.tablestyle.narrow"));
   }

   private static TableStyle createStyle(String var0) {
      try {
         Class var1 = Class.forName(var0);
         return (TableStyle)var1.newInstance();
      } catch (Exception var2) {
         var2.printStackTrace();
         return NO_STYLE;
      }
   }

   public static TableStyle selector() {
      TableStyle var0 = normal();
      var0.setGridMode(TableGridMode.NONE);
      return var0;
   }

   private TableStyles() {
   }
}
