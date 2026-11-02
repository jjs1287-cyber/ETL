package org.etlforge.ui.widget;

import java.awt.Color;
import java.awt.Font;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;

public class LabelAdapter implements Label {
   private final JComponent adaptee;

   public LabelAdapter(JComponent var1) {
      checkComponent(var1);
      this.adaptee = var1;
   }

   private static void checkComponent(JComponent var0) throws IllegalArgumentException {
      if (!(var0 instanceof JLabel) && !(var0 instanceof AbstractButton)) {
         throw new IllegalArgumentException("c must be either a JLabel or an AbstractButton.");
      }
   }

   @Override
   public void setFont(Font var1) {
      this.adaptee.setFont(var1);
   }

   @Override
   public void setIcon(Icon var1) {
      if (this.adaptee instanceof JLabel) {
         ((JLabel)this.adaptee).setIcon(var1);
      } else if (this.adaptee instanceof AbstractButton) {
         ((AbstractButton)this.adaptee).setIcon(var1);
      }
   }

   @Override
   public void setText(String var1) {
      if (this.adaptee instanceof JLabel) {
         ((JLabel)this.adaptee).setText(var1);
      } else if (this.adaptee instanceof AbstractButton) {
         ((AbstractButton)this.adaptee).setText(var1);
      }
   }

   @Override
   public void setText(MnemonicString var1) {
      if (this.adaptee instanceof JLabel) {
         this.setText((JLabel)this.adaptee, var1);
      } else if (this.adaptee instanceof AbstractButton) {
         this.setText((AbstractButton)this.adaptee, var1);
      }
   }

   @Override
   public void setBorder(Border var1) {
      this.adaptee.setBorder(var1);
   }

   @Override
   public UiProvider asUiProvider() {
      return UiProviderAdapter.of(this.adaptee);
   }

   private void setText(JLabel var1, MnemonicString var2) {
      var1.setText(var2.getText());
      char var3 = var2.getMnemonic();
      if (var3 != 0) {
         var1.setDisplayedMnemonic(var3);
         var1.setDisplayedMnemonicIndex(var2.getDisplayedMnemonicIndex());
      }
   }

   private void setText(AbstractButton var1, MnemonicString var2) {
      var1.setText(var2.getText());
      char var3 = var2.getMnemonic();
      if (var3 != 0) {
         var1.setMnemonic(var3);
         var1.setDisplayedMnemonicIndex(var2.getDisplayedMnemonicIndex());
      }
   }

   @Override
   public void setTextColor(Color var1) {
      this.adaptee.setForeground(var1);
   }

   @Override
   public void setBackgroundColor(Color var1) {
      this.adaptee.setBackground(var1);
   }

   @Override
   public void setToolTip(String var1) {
      this.adaptee.setToolTipText(var1);
   }
}
