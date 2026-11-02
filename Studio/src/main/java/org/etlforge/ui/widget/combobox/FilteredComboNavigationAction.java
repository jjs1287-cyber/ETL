package org.etlforge.ui.widget.combobox;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;

public abstract class FilteredComboNavigationAction extends AbstractAction {
   private final Predicate<Integer> filter;

   public static void installAll(JComboBox var0, Predicate<Integer> var1) {
      new FilteredComboUpAction(var1).install(var0);
      new FilteredComboDownAction(var1).install(var0);
      new FilteredComboHomeAction(var1).install(var0);
      new FilteredComboEndAction(var1).install(var0);
      new FilteredComboPageUpAction(var1).install(var0);
      new FilteredComboPageDownAction(var1).install(var0);
   }

   protected FilteredComboNavigationAction(String var1, KeyStroke var2, Predicate<Integer> var3) {
      super(var1);
      this.filter = Preconditions.checkNotNull(var3, "filter");
      this.putValue("AcceleratorKey", var2);
   }

   public final void install(JComboBox var1) {
      String var2 = (String)this.getValue("Name");
      KeyStroke var3 = (KeyStroke)this.getValue("AcceleratorKey");
      var1.getInputMap().put(var3, var2);
      var1.getActionMap().put(var2, this);
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      JComboBox var2 = (JComboBox)var1.getSource();
      int var3 = var2.getSelectedIndex();
      int var4 = this.nextIndex(var2, var3);
      if (var4 >= 0) {
         var2.setSelectedIndex(var4);
      }
   }

   protected abstract int nextIndex(JComboBox var1, int var2);

   protected final boolean isSelectableIndex(int var1) {
      return this.filter.apply(var1);
   }
}
