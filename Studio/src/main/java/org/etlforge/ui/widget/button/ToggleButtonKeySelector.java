package org.etlforge.ui.widget.button;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import org.etlforge.lang.IntRing;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;

public final class ToggleButtonKeySelector {
   public static void install(Iterable<? extends JToggleButton> var0, JComponent var1) {
      ImmutableList var2 = ImmutableList.copyOf(var0);
      byte var3 = 1;
      ActionKeyBinding.install(var1, var3, KeyUtils.DOWN, new ToggleButtonKeySelector.Handler(var2, IntRing.Direction.FORWARD));
      ActionKeyBinding.install(var1, var3, KeyUtils.UP, new ToggleButtonKeySelector.Handler(var2, IntRing.Direction.BACKWARD));
   }

   private ToggleButtonKeySelector() {
   }

   private static final class Handler implements ActionListener {
      private final ImmutableList<? extends JToggleButton> buttons;
      private final IntRing ring;

      public Handler(ImmutableList<? extends JToggleButton> var1, IntRing.Direction var2) {
         this.buttons = var1;
         this.ring = var2.newRing(var1.size());
      }

      @Override
      public final void actionPerformed(ActionEvent var1) {
         int var2 = Iterables.indexOf(this.buttons, ButtonUtils.TOGGLE_BUTTON_SELECTED);
         int var3 = this.ring.next(var2);
         this.buttons.get(var3).setSelected(true);
      }
   }
}
