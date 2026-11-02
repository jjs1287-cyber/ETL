package org.etlforge.ui.widget.combobox;

import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalComboBoxEditor.UIResource;
import org.etlforge.ui.widget.text.KongaTextField;

public class KongaComboBoxEditorComponent extends KongaTextField {
   public KongaComboBoxEditorComponent() {
      super("", 9);
   }

   @Override
   public void setBorder(Border var1) {
      if (!(var1 instanceof UIResource)) {
         super.setBorder(var1);
      }
   }
}
