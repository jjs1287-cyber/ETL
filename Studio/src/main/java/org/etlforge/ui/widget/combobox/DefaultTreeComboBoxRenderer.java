package org.etlforge.ui.widget.combobox;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import org.etlforge.ui.util.border.BorderBuilder;

public class DefaultTreeComboBoxRenderer implements ListCellRenderer {
   private final ListCellRenderer delegate;
   private TreeComboBoxModel model;

   public DefaultTreeComboBoxRenderer(TreeComboBoxModel var1, ListCellRenderer var2) {
      this.delegate = Preconditions.checkNotNull(var2);
      this.setModel(var1);
   }

   public void setModel(TreeComboBoxModel var1) {
      this.model = Preconditions.checkNotNull(var1);
   }

   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      Component var6 = this.delegate.getListCellRendererComponent(var1, var2, var3, var4, var5);
      this.calculateAndSetIndent(var6, var2, var3);
      return var6;
   }

   private void calculateAndSetIndent(Component var1, Object var2, int var3) {
      if (var1 instanceof JComponent) {
         int var4 = 0;
         if (var3 >= 0) {
            var4 = this.model.getIndentLevel(var2);
         }

         if (var4 > 0) {
            JComponent var5 = (JComponent)var1;
            Border var6 = var5.getBorder();
            BorderBuilder var7 = new BorderBuilder(var6).empty(new Insets(0, 16 * var4, 0, 0));
            var5.setBorder(var7);
         }
      }
   }
}
