package org.etlforge.ui.widget.tree;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.widget.label.UiProviderLabel;

public final class KongaTreeLabel {
   public KongaTreeLabel(KongaTree var1, JLabel var2) {
      this(var1, var2, var2.getInputMap(2), var2.getActionMap());
   }

   public KongaTreeLabel(KongaTree var1, JLabel var2, InputMap var3, ActionMap var4) {
      new UiProviderLabel(new UiProviderAdapter(var1), var2, var3, var4) {
         @Override
         protected void afterComponentWasFocused(UiProvider var1) {
            UiProviderAdapter var2x = (UiProviderAdapter)var1;
            KongaTree var3x = (KongaTree)var2x.getAdaptee();
            if (var3x.getSelectionCount() == 0) {
               var3x.selectFirstRow();
            }
         }
      };
   }
}
