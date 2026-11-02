package org.etlforge.ui.widget.list;

import com.google.common.base.Preconditions;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public abstract class DisabledListCellRenderer implements ListCellRenderer {
   private final ListCellRenderer adaptee;

   public DisabledListCellRenderer() {
      this(new DefaultListCellRenderer());
   }

   public DisabledListCellRenderer(ListCellRenderer var1) {
      this.adaptee = Preconditions.checkNotNull(var1);
   }

   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      Component var6 = this.adaptee.getListCellRendererComponent(var1, var2, var3, var4, var5);
      var6.setEnabled(!this.disableFor(var2));
      return var6;
   }

   protected abstract boolean disableFor(Object var1);
}
