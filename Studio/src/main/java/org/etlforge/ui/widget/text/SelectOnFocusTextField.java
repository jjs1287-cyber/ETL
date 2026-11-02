package org.etlforge.ui.widget.text;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SelectOnFocusTextField extends KongaTextField {
   private String selectString;

   public SelectOnFocusTextField() {
      this(null, 0);
   }

   public SelectOnFocusTextField(int var1) {
      this(null, var1);
   }

   public SelectOnFocusTextField(String var1) {
      this(var1, 0);
   }

   public SelectOnFocusTextField(String var1, int var2) {
      super(var1, var2);
      this.selectString = var1;
      this.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent var1) {
            SelectOnFocusTextField.this.handleFocusGained();
         }
      });
   }

   public void setSelectString(String var1) {
      this.selectString = var1;
      this.setText(var1);
   }

   public void focusLost(FocusEvent var1) {
   }

   private void handleFocusGained() {
      if (this.selectString != null && super.getText().equals(this.selectString)) {
         this.setSelectionStart(0);
         this.setSelectionEnd(this.selectString.length());
      }
   }
}
