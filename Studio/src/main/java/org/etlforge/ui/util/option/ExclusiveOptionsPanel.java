package org.etlforge.ui.util.option;

import com.google.common.collect.Sets;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.Orientation;
import org.etlforge.ui.widget.button.KongaRadioButton;

public final class ExclusiveOptionsPanel<T extends Option> extends AbstractOptionsPanel<T> {
   private final ButtonGroup buttonGroup = new ButtonGroup();

   public ExclusiveOptionsPanel(T[] var1, T var2, Orientation var3) {
      super((T[])var1, Sets.newHashSet((T[])(new Option[]{var2})), var3);
   }

   public final T getSelectedOption() {
      List var1 = this.getSelectedOptions();
      return (T)(var1.isEmpty() ? null : var1.get(0));
   }

   @Override
   protected final JToggleButton createSelectorButton(T var1, boolean var2) {
      KongaRadioButton var3 = new KongaRadioButton("", var2);
      this.buttonGroup.add(var3);
      this.registerSelectionListener(var3, (T)var1);
      return var3;
   }

   private void registerSelectionListener(KongaRadioButton var1, final T var2) {
      var1.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            if (var1) {
               ExclusiveOptionsPanel.this.fireSelectionChanged(var2);
            }
         }
      });
   }
}
