package org.etlforge.ui.widget.menu;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.button.ButtonUtils;

public class KongaCheckBoxMenuItem extends JCheckBoxMenuItem implements KongaWidget {
   public KongaCheckBoxMenuItem() {
   }

   public KongaCheckBoxMenuItem(Icon var1) {
      super(var1);
   }

   public KongaCheckBoxMenuItem(String var1) {
      super(var1);
   }

   public KongaCheckBoxMenuItem(Action var1) {
      super(var1);
   }

   public KongaCheckBoxMenuItem(String var1, Icon var2) {
      super(var1, var2);
   }

   public KongaCheckBoxMenuItem(String var1, boolean var2) {
      super(var1, var2);
   }

   public KongaCheckBoxMenuItem(String var1, Icon var2, boolean var3) {
      super(var1, var2, var3);
   }

   @Override
   public void setText(String var1) {
      MnemonicString var2 = MNEMONIC_FACTORY.fromString(var1 != null ? var1 : "");
      super.setText(var2.getText());
      ButtonUtils.applyMnemonics(this, var2);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      throw new NotImplementedYetException();
   }

   @Override
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }
}
