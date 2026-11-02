package org.etlforge.integration.client.server.ui.user;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public final class RoleRenderers {
   public static final ListCellRenderer LIST = new RoleRenderers.ListRenderer();
   public static final TableCellRenderer TABLE = new RoleRenderers.TableRenderer();

   private static void decorate(JLabel var0, Object var1) {
      if (var1 instanceof User) {
         var0.setIcon(ClientIcons.USER_16);
         var0.setText(((User)var1).getName());
      } else if (var1 instanceof Group) {
         var0.setIcon(ClientIcons.GROUP_16);
         var0.setText(((Group)var1).getName());
      }
   }

   private RoleRenderers() {
   }

   private static class ListRenderer extends DefaultListCellRenderer {
      private ListRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         RoleRenderers.decorate(this, var2);
         return this;
      }
   }

   private static class TableRenderer extends DefaultTableCellRenderer {
      private TableRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         RoleRenderers.decorate(this, var2);
         return this;
      }
   }
}
