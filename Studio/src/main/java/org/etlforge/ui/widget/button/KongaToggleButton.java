package org.etlforge.ui.widget.button;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;

public class KongaToggleButton extends JToggleButton implements KongaWidget {
   private boolean toolbarButton;
   private boolean useSmallActionIcon;

   public KongaToggleButton() {
   }

   public KongaToggleButton(Icon var1) {
      super(var1);
   }

   public KongaToggleButton(String var1) {
      super(var1);
   }

   public KongaToggleButton(Action var1) {
      super(var1);
   }

   public KongaToggleButton(Icon var1, boolean var2) {
      super(var1, var2);
   }

   public KongaToggleButton(String var1, boolean var2) {
      super(var1, var2);
   }

   public KongaToggleButton(String var1, Icon var2) {
      super(var1, var2);
   }

   public KongaToggleButton(String var1, Icon var2, boolean var3) {
      super(var1, var2, var3);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      throw new NotImplementedYetException();
   }

   @Override
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   @Override
   public String getText() {
      return this.toolbarButton ? "" : super.getText();
   }

   @Override
   public void setText(String var1) {
      if (this.toolbarButton) {
         super.setText("");
      } else {
         MnemonicString var2 = MNEMONIC_FACTORY.fromString(var1 != null ? var1 : "");
         super.setText(var2.getText());
         ButtonUtils.applyMnemonics(this, var2);
      }
   }

   public KongaToggleButton makeToolbarButton() {
      if (!this.toolbarButton) {
         this.toolbarButton = true;
         ButtonUtils.configureButtonForToolBar(this);
      }

      return this;
   }

   public KongaToggleButton setUseSmallActionIcon(boolean var1) {
      if (var1 != this.useSmallActionIcon) {
         this.useSmallActionIcon = var1;
         if (this.getAction() != null) {
            this.configurePropertiesFromAction(this.getAction());
         }
      }

      return this;
   }

   @Override
   protected void configurePropertiesFromAction(Action var1) {
      super.configurePropertiesFromAction(var1);
      if (this.useSmallActionIcon && var1 != null) {
         this.setIcon((Icon)var1.getValue("SmallIcon"));
      }
   }
}
