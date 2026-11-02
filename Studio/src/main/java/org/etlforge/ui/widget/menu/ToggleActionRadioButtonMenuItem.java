package org.etlforge.ui.widget.menu;

import java.beans.PropertyChangeEvent;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JRadioButtonMenuItem;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.util.AbstractActionPropertyChangeListener;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.button.ButtonUtils;

public class ToggleActionRadioButtonMenuItem extends JRadioButtonMenuItem implements KongaWidget {
   public ToggleActionRadioButtonMenuItem(ToggleAction var1) {
      super(var1);
      this.setSelected(var1.isSelected());
      var1.addPropertyChangeListener(new ToggleActionRadioButtonMenuItem.OnOffListener(this, var1));
   }

   @Override
   public void setText(String var1) {
      MnemonicString var2 = MNEMONIC_FACTORY.fromString(var1 != null ? var1 : "");
      super.setText(var2.getText());
      ButtonUtils.applyMnemonics(this, var2);
   }

   @Override
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
   }

   private static class OnOffListener extends AbstractActionPropertyChangeListener {
      public OnOffListener(ToggleActionRadioButtonMenuItem var1, ToggleAction var2) {
         super(var1, var2);
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if ("ToggleAction#ON_OFF".equals(var1.getPropertyName())) {
            AbstractButton var2 = (AbstractButton)this.getObservingButton();
            if (var2 == null) {
               Action var3 = (Action)var1.getSource();
               var3.removePropertyChangeListener(this);
            } else if (var2 instanceof ToggleActionRadioButtonMenuItem) {
               ToggleAction var4 = (ToggleAction)this.getActionListenedTo();
               ((ToggleActionRadioButtonMenuItem)var2).setSelected(var4.isSelected());
            }
         }
      }
   }
}
