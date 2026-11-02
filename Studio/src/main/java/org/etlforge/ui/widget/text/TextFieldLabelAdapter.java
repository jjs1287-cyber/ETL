package org.etlforge.ui.widget.text;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.widget.Label;

public class TextFieldLabelAdapter implements Label {
   private final JTextField field;

   public TextFieldLabelAdapter(JTextField var1) {
      this.field = var1;
      var1.setEditable(false);
   }

   @Override
   public void setBackgroundColor(Color var1) {
      this.field.setBackground(var1);
   }

   @Override
   public void setFont(Font var1) {
      this.field.setFont(var1);
   }

   @Override
   public void setIcon(Icon var1) {
      if (this.field instanceof KongaTextField) {
         ((KongaTextField)this.field).setIcon(var1);
      }
   }

   @Override
   public void setText(String var1) {
      this.field.setText(var1);
      this.field.setCaretPosition(0);
   }

   @Override
   public void setText(MnemonicString var1) {
      this.setText(var1.getText());
   }

   @Override
   public void setTextColor(Color var1) {
      this.field.setForeground(var1);
   }

   @Override
   public void setToolTip(String var1) {
      this.field.setToolTipText(var1);
   }

   @Override
   public void setBorder(Border var1) {
      this.field.setBorder(var1);
   }

   @Override
   public UiProvider asUiProvider() {
      UiProviderAdapter var1 = new UiProviderAdapter(this.field);
      var1.setSuppressScroll(true);
      return var1;
   }
}
