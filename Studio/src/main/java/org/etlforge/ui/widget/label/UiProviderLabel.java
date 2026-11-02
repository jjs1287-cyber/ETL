package org.etlforge.ui.widget.label;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import org.etlforge.ui.UiProvider;

public class UiProviderLabel {
   private final UiProvider component;

   public static KongaLabel create(String var0, UiProvider var1) {
      KongaLabel var2 = KongaLabel.withMnemonic(var0);
      new UiProviderLabel(var1, var2);
      return var2;
   }

   public UiProviderLabel(UiProvider var1, JLabel var2) {
      this(var1, var2, var2.getInputMap(2), var2.getActionMap());
   }

   public UiProviderLabel(UiProvider var1, JLabel var2, InputMap var3, ActionMap var4) {
      this.component = var1;
      this.installTrigger(var2, var3, var4);
   }

   private void installTrigger(JLabel var1, InputMap var2, ActionMap var3) {
      if (var1.getDisplayedMnemonic() != 0) {
         KeyStroke var4 = KeyStroke.getKeyStroke(var1.getDisplayedMnemonic(), 8, false);
         Object var5 = var2.get(var4);
         if (var5 == null) {
            var5 = var4.toString();
            var2.put(var4, var5);
         }

         var3.put(var5, new UiProviderLabel.Trigger());
      }
   }

   protected void afterComponentWasFocused(UiProvider var1) {
   }

   private final class Trigger extends AbstractAction {
      private Trigger() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         UiProviderLabel.this.component.requestFocus();
         UiProviderLabel.this.afterComponentWasFocused(UiProviderLabel.this.component);
      }
   }
}
