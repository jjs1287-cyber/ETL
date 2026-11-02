package org.etlforge.integration.client.ui.jitterpack.table;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;
import org.etlforge.util.version.Version;

final class TableRenderers {
   public static TableCellRenderer getLocationRenderer() {
      return new DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (var2 instanceof JitterPackLocationTableItem) {
               this.setIcon(JitterpackResources.IMPORT_16);
               this.setToolTipText(this.getText());
            } else {
               this.setIcon(null);
               this.setToolTipText("");
            }

            return this;
         }
      };
   }

   public static TableCellRenderer getStringRenderer() {
      return new DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            this.setToolTipText(this.getText());
            return this;
         }
      };
   }

   public static TableCellRenderer getVersionRenderer() {
      return new DefaultTableCellRenderer() {
         @Override
         protected void setValue(Object var1) {
            if (var1 instanceof Version) {
               super.setValue(var1.toString());
            } else {
               super.setValue("?");
            }
         }

         @Override
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (var2 instanceof Version) {
               this.setHorizontalAlignment(2);
            } else {
               this.setHorizontalAlignment(0);
            }

            return this;
         }
      };
   }

   public static TableCellRenderer getDateRenderer() {
      return new DefaultTableCellRenderer() {
         private final DateFormat fmt = DateFormat.getDateInstance();

         @Override
         protected void setValue(Object var1) {
            if (var1 instanceof Date) {
               super.setValue(this.fmt.format((Date)var1));
            } else {
               super.setValue("?");
            }
         }
      };
   }

   private TableRenderers() {
   }
}
