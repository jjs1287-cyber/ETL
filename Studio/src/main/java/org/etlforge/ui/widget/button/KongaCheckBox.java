package org.etlforge.ui.widget.button;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.border.Border;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;

public class KongaCheckBox extends JCheckBox implements KongaWidget {
   private final ToggleButtonSelectionReceivers receivers = new ToggleButtonSelectionReceivers(this);

   public KongaCheckBox() {
   }

   public KongaCheckBox(boolean var1) {
      this("", var1);
   }

   public KongaCheckBox(Action var1) {
      super(var1);
   }

   public KongaCheckBox(Icon var1, boolean var2) {
      super(var1, var2);
   }

   public KongaCheckBox(Icon var1) {
      super(var1);
   }

   public KongaCheckBox(String var1, boolean var2) {
      super(var1, var2);
   }

   public KongaCheckBox(String var1, Icon var2, boolean var3) {
      super(var1, var2, var3);
   }

   public KongaCheckBox(String var1, Icon var2) {
      super(var1, var2);
   }

   public KongaCheckBox(String var1) {
      super(var1);
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

   public final void addSelectionChangeListener(Receiver<Boolean> var1) {
      this.receivers.add(var1);
   }

   public final void removeSelectionChangeListener(Receiver<Boolean> var1) {
      this.receivers.remove(var1);
   }

   public KongaCheckBox withBorder(Border var1) {
      this.setBorder(var1);
      return this;
   }
}
