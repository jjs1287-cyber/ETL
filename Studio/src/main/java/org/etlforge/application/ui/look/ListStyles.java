package org.etlforge.application.ui.look;

import java.awt.Insets;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.widget.list.AlternateRowListRenderer;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListStyle;
import org.etlforge.ui.widget.list.PaddingListRenderer;

public final class ListStyles {
   public static ListStyle wide() {
      return wide(null);
   }

   public static ListStyle wide(ListCellRenderer var0) {
      return new ListStyles.Blue(var0, new Insets(4, 0, 4, 0));
   }

   public static ListStyle normal() {
      return normal(null);
   }

   public static ListStyle normal(ListCellRenderer var0) {
      return new ListStyles.Blue(var0, new Insets(2, 0, 2, 0));
   }

   public static ListStyle narrow() {
      return narrow(null);
   }

   public static ListStyle narrow(ListCellRenderer var0) {
      return new ListStyles.Blue(var0, null);
   }

   private ListStyles() {
   }

   private static final class Blue implements ListStyle {
      private final ListCellRenderer renderer;
      private final Insets padding;

      public Blue(ListCellRenderer var1, Insets var2) {
         this.renderer = var1 != null ? var1 : new DefaultListDecorRenderer();
         this.padding = var2;
      }

      @Override
      public void makeOver(JList var1) {
         ListCellRenderer var2 = var1.getCellRenderer();
         if (var2 == null) {
            var2 = this.renderer;
         }

         var1.setCellRenderer(this.createRenderer(var2, this.padding));
      }

      @Override
      public void makeOver(KongaListFactory var1) {
         var1.setCellRenderer(this.createRenderer(this.renderer, this.padding));
      }

      private ListCellRenderer createRenderer(ListCellRenderer var1, Insets var2) {
         if (var2 != null) {
            var1 = new PaddingListRenderer(var1, var2);
         }

         AlternateRowListRenderer var3 = new AlternateRowListRenderer(var1, AlternateColors.get());
         var3.setSeparatorColor(UIManager.getColor("Table.gridColor"));
         return var3;
      }
   }
}
