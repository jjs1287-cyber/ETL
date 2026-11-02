package org.etlforge.ui.widget.table.renderer;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class LinkedTableRendererPreparer implements TableRendererPreparer {
   private final List<TableRendererPreparer> preparers = new ArrayList<>();

   public void add(TableRendererPreparer var1) {
      Preconditions.checkNotNull(var1, "p");
      this.preparers.add(var1);
   }

   public void remove(TableRendererPreparer var1) {
      this.preparers.remove(var1);
   }

   @Override
   public boolean doDefaultPreparation() {
      return true;
   }

   @Override
   public Component prepare(JTable var1, TableCellRenderer var2, Component var3, int var4, int var5) {
      Component var6 = var3;

      for (TableRendererPreparer var8 : this.preparers) {
         var6 = var8.prepare(var1, var2, var3, var4, var5);
      }

      return var6;
   }
}
