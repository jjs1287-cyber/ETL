package org.etlforge.ui.widget.button;

import com.google.common.base.Supplier;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;

public class KongaRadioButton extends JRadioButton implements KongaWidget {
   private final ToggleButtonSelectionReceivers receivers = new ToggleButtonSelectionReceivers(this);

   public KongaRadioButton() {
   }

   public KongaRadioButton(Icon var1) {
      super(var1);
   }

   public KongaRadioButton(Action var1) {
      super(var1);
   }

   public KongaRadioButton(MnemonicString var1) {
      this(var1.getRaw());
   }

   public KongaRadioButton(String var1) {
      super(var1);
   }

   public KongaRadioButton(Icon var1, boolean var2) {
      super(var1, var2);
   }

   public KongaRadioButton(String var1, boolean var2) {
      super(var1, var2);
   }

   public KongaRadioButton(MnemonicString var1, boolean var2) {
      this(var1.getRaw(), var2);
   }

   public KongaRadioButton(String var1, Icon var2) {
      super(var1, var2);
   }

   public KongaRadioButton(MnemonicString var1, Icon var2) {
      super(var1.getRaw(), var2);
   }

   public KongaRadioButton(String var1, Icon var2, boolean var3) {
      super(var1, var2, var3);
   }

   public KongaRadioButton(MnemonicString var1, Icon var2, boolean var3) {
      super(var1.getRaw(), var2, var3);
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

   public boolean isSelectedThreadSafely() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            return KongaRadioButton.this.isSelected();
         }
      });
   }
}
