package org.etlforge.ui.util.option;

import com.google.common.collect.Sets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import org.etlforge.ui.util.Orientation;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class MultipleOptionsPanel<T extends Option> extends AbstractOptionsPanel<T> {
   public MultipleOptionsPanel(T[] var1, T[] var2, Orientation var3) {
      super((T[])var1, Sets.newHashSet((T[])var2), var3);
   }

   @Override
   protected JToggleButton createSelectorButton(T var1, boolean var2) {
      KongaCheckBox var3 = new KongaCheckBox(var2);
      this.registerSelectionListener(var3);
      return var3;
   }

   private void registerSelectionListener(JCheckBox var1) {
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            MultipleOptionsPanel.this.fireSelectionChanged();
         }
      });
   }
}
