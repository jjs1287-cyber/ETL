package org.etlforge.ui.widget.button;

import java.awt.Color;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.LabelDecorator;

public class KongaButton extends JButton implements KongaWidget {
   private boolean toolbarButton;
   private boolean useSmallActionIcon;
   private boolean useMediumActionIcon;
   private Color disabledForeground = Color.GRAY;

   public static KongaButton forToolbar(Action var0) {
      return new KongaButton(var0).makeToolbarButton();
   }

   public KongaButton() {
   }

   public KongaButton(LabelDecorator var1) {
      var1.decorate(ButtonUtils.asLabel(this));
   }

   public KongaButton(Icon var1) {
      super(var1);
   }

   public KongaButton(String var1) {
      super(var1);
   }

   public KongaButton(Action var1) {
      super(var1);
      String var2 = (String)var1.getValue("Name");
      this.setText(var2);
   }

   public KongaButton(String var1, Icon var2) {
      super(var1, var2);
   }

   @Override
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
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

   @Override
   public String getText() {
      return this.toolbarButton ? "" : super.getText();
   }

   public KongaButton makeToolbarButton() {
      if (!this.toolbarButton) {
         this.toolbarButton = true;
         ButtonUtils.configureButtonForToolBar(this);
      }

      return this;
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      throw new NotImplementedYetException();
   }

   public KongaButton setUseSmallActionIcon(boolean var1) {
      if (var1 != this.useSmallActionIcon) {
         this.useSmallActionIcon = var1;
         if (var1) {
            this.useMediumActionIcon = false;
         }

         if (this.getAction() != null) {
            this.configurePropertiesFromAction(this.getAction());
         }
      }

      return this;
   }

   public KongaButton setUseMediumActionIcon(boolean var1) {
      if (var1 != this.useMediumActionIcon) {
         this.useMediumActionIcon = var1;
         if (var1) {
            this.useSmallActionIcon = false;
         }

         if (this.getAction() != null) {
            this.configurePropertiesFromAction(this.getAction());
         }
      }

      return this;
   }

   public KongaButton withSmallActionIcon() {
      return this.setUseSmallActionIcon(true);
   }

   public KongaButton withMediumActionIcon() {
      return this.setUseMediumActionIcon(true);
   }

   @Override
   protected void actionPropertyChanged(Action var1, String var2) {
      if ("KongaMediumIconKey".equals(var2)) {
         this.mediumIconChanged(var1);
      } else {
         super.actionPropertyChanged(var1, var2);
      }
   }

   private void mediumIconChanged(Action var1) {
      if (this.useMediumActionIcon) {
         Icon var2 = (Icon)var1.getValue("KongaMediumIconKey");
         this.setIcon(var2);
      }
   }

   @Override
   protected void configurePropertiesFromAction(Action var1) {
      super.configurePropertiesFromAction(var1);
      if (var1 != null) {
         String var2 = null;
         if (this.useSmallActionIcon) {
            var2 = "SmallIcon";
         } else if (this.useMediumActionIcon) {
            var2 = "KongaMediumIconKey";
         }

         if (var2 != null) {
            this.setIcon((Icon)var1.getValue(var2));
         }
      }
   }

   public void makeDefaultButton() {
      JRootPane var1 = SwingUtilities.getRootPane(this);
      if (var1 != null) {
         var1.setDefaultButton(this);
      }
   }

   @Override
   public final Color getForeground() {
      return this.isEnabled() ? super.getForeground() : this.disabledForeground;
   }

   public final void setDisabledForeground(Color var1) {
      this.disabledForeground = var1;
   }
}
