package org.etlforge.ui.widget.list;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JList;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.widget.label.UiProviderLabel;

public final class KongaListLabel {
   public KongaListLabel(KongaList var1, JLabel var2) {
      this(var1, var2, var2.getInputMap(2), var2.getActionMap());
   }

   public KongaListLabel(KongaList var1, JLabel var2, InputMap var3, ActionMap var4) {
      new UiProviderLabel(var1, var2, var3, var4) {
         @Override
         protected void afterComponentWasFocused(UiProvider var1) {
            KongaList var2x = (KongaList)var1;
            if (var2x.getSelection().isEmpty()) {
               var2x.selectFirst();
            }
         }
      };
   }

   public KongaListLabel(JList var1, JLabel var2) {
      this(var1, var2, var2.getInputMap(2), var2.getActionMap());
   }

   public KongaListLabel(JList var1, JLabel var2, InputMap var3, ActionMap var4) {
      new UiProviderLabel(new UiProviderAdapter(var1), var2, var3, var4) {
         @Override
         protected void afterComponentWasFocused(UiProvider var1) {
            UiProviderAdapter var2x = (UiProviderAdapter)var1;
            JList var3x = (JList)var2x.getAdaptee();
            if (var3x.getSelectedIndices().length == 0 && var3x.getModel().getSize() > 0) {
               var3x.setSelectedIndex(0);
            }
         }
      };
   }
}
