package org.etlforge.ui.widget.button;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JToggleButton;
import org.etlforge.system.callback.Receiver;

final class ToggleButtonSelectionReceivers {
   private final JToggleButton button;
   private boolean state;
   private final List<Receiver<Boolean>> stateReceivers = new CopyOnWriteArrayList<>();

   public ToggleButtonSelectionReceivers(JToggleButton var1) {
      this.button = var1;
      this.state = var1.isSelected();
      var1.addItemListener(new ToggleButtonSelectionReceivers.Listener());
   }

   public final void add(Receiver<Boolean> var1) {
      this.stateReceivers.add(var1);
   }

   public final void remove(Receiver<Boolean> var1) {
      this.stateReceivers.remove(var1);
   }

   private final class Listener implements ItemListener {
      private Listener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         boolean var2 = ToggleButtonSelectionReceivers.this.button.isSelected();
         if (var2 != ToggleButtonSelectionReceivers.this.state) {
            for (Receiver var4 : ToggleButtonSelectionReceivers.this.stateReceivers) {
               var4.handle(var2);
            }

            ToggleButtonSelectionReceivers.this.state = var2;
         }
      }
   }
}
