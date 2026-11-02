package org.etlforge.integration.client.server.ui.info;

import com.google.common.base.Strings;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.renderer.ListDecorTableRenderer;
import org.etlforge.util.version.Version;

final class TableRenderers {
   public static TableCellRenderer getNameRenderer() {
      return new DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            this.setIcon(ClientIcons.SERVER_16);
            return this;
         }
      };
   }

   public static TableCellRenderer getDateRenderer() {
      return new ListDecorTableRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            if (var2 instanceof DateCell) {
               if (((DateCell)var2).getDate() != null) {
                  this.setHorizontalAlignment(2);
               } else {
                  this.setHorizontalAlignment(0);
               }
            }

            return this;
         }

         @Override
         public void setText(String var1) {
            if (Strings.isNullOrEmpty(var1)) {
               super.setText("?");
            } else {
               super.setText(var1);
            }
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
               this.setToolTipText(var2.toString());
            } else {
               this.setHorizontalAlignment(0);
               this.setToolTipText(PackageResources.Viewer.UNKNOWN_VERSION);
            }

            return this;
         }
      };
   }

   public static TableCellRenderer getUserRenderer() {
      return new DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
            this.setIcon(ClientIcons.USER_16);
            return this;
         }
      };
   }

   private TableRenderers() {
   }
}
