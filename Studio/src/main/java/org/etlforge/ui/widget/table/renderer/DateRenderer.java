package org.etlforge.ui.widget.table.renderer;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateRenderer extends DefaultTableCellRenderer {
   private final DateFormat format;

   public DateRenderer(DateFormat var1) {
      this.format = var1;
   }

   public DateRenderer(String var1) {
      this(new SimpleDateFormat(var1));
   }

   public static DateRenderer dateAndTime() {
      return new DateRenderer(DateFormat.getDateTimeInstance());
   }

   public static DateRenderer dateAndTime(int var0, int var1) {
      return new DateRenderer(DateFormat.getDateTimeInstance(var0, var1));
   }

   public static DateRenderer dateOnly(int var0) {
      return new DateRenderer(DateFormat.getDateInstance(var0));
   }

   public static DateRenderer timeOnly(int var0) {
      return new DateRenderer(DateFormat.getTimeInstance(var0));
   }

   public void setTimeZone(TimeZone var1) {
      this.format.setTimeZone(var1);
   }

   public void useUTC() {
      this.setTimeZone(TimeZone.getTimeZone("UTC"));
   }

   public void useLocalTime() {
      this.setTimeZone(TimeZone.getDefault());
   }

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      String var7;
      if (var2 instanceof Date) {
         var7 = this.format.format(var2);
      } else if (var2 != null) {
         var7 = var2.toString();
      } else {
         var7 = null;
      }

      this.setText(var7);
      this.setToolTipText(var7);
      return this;
   }
}
