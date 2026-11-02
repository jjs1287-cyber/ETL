package org.etlforge.ui.style;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.etlforge.ui.UIManager2;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.LinkedTableRendererPreparer;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

public abstract class AbstractTableStyle implements TableStyle {
   private final LinkedTableRendererPreparer renderers;
   private TableGridMode gridMode = TableGridMode.NONE;

   protected AbstractTableStyle() {
      this.renderers = new LinkedTableRendererPreparer();
   }

   @Override
   public final void addRendererPreparer(TableRendererPreparer var1) {
      this.renderers.add(var1);
   }

   public final TableRendererPreparer getRendererPreparer() {
      return this.renderers;
   }

   @Override
   public void setGridMode(TableGridMode var1) {
      Preconditions.checkNotNull(var1, "mode");
      this.gridMode = var1;
   }

   @Override
   public void makeOver(JTable var1) {
      this.gridMode.applyTo(var1);
      if (var1 instanceof KongaTable) {
         ((KongaTable)var1).setRendererPreparer(this.renderers);
      }

      Font var2 = this.getFont(var1);
      var1.setFont(var2);
   }

   protected Font getFont(JTable var1) {
      return UIManager2.getFont("konga.tablestyle.font", var1.getFont());
   }

   protected boolean showVerticalGrid() {
      return false;
   }

   protected final void setGridColor(JTable var1, Color var2) {
      var1.setGridColor(var2);
   }

   protected final void setRowHeight(JTable var1, double var2) {
      int var4 = var1.getRowHeight();
      var1.setRowHeight((int)(var4 * var2));
   }

   protected final void setHeaderRenderer(JTable var1, Insets var2, float var3) {
      JTableHeader var4 = var1.getTableHeader();
      if (var4 != null) {
         TableCellRenderer var5 = var4.getDefaultRenderer();
         var4.setDefaultRenderer(new AbstractTableStyle.BlueTableHeader(var5, var2, var3));
      }
   }

   private static class BlueTableHeader extends DefaultTableCellRenderer {
      private static final Border defaultBorder = UIManager.getBorder("TableHeader.cellBorder");
      private static final Color BACKGROUND = lookupBackground();
      private static final Color FOREGROUND = lookupForeground();
      private final TableCellRenderer original;
      private final Border border;
      private final float fontSizeDelta;

      public BlueTableHeader(TableCellRenderer var1, Insets var2, float var3) {
         this.original = var1;
         this.border = new BorderBuilder(defaultBorder).empty(var2.top, var2.left, var2.bottom, var2.right);
         this.fontSizeDelta = var3;
         this.setHorizontalAlignment(2);
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         Component var7 = this.original != null
            ? this.original.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6)
            : super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         ((JComponent)var7).setBorder(this.border);
         if (var7 instanceof JLabel) {
            ((JLabel)var7).setHorizontalAlignment(10);
         }

         var7.setFont(this.getFont(var1));
         var7.setForeground(FOREGROUND);
         var7.setBackground(BACKGROUND);
         return var7;
      }

      private Font getFont(JTable var1) {
         Font var2 = Fonts.bold(var1);
         return var2.deriveFont(var2.getSize2D() + this.fontSizeDelta);
      }

      private static Color lookupBackground() {
         Color var0 = UIManager.getColor("konga.tableheader.background");
         if (var0 == null) {
            var0 = new Color(15792383);
         }

         return var0;
      }

      private static Color lookupForeground() {
         Color var0 = UIManager.getColor("konga.tableheader.foreground");
         if (var0 == null) {
            var0 = Color.BLACK;
         }

         return var0;
      }
   }
}
