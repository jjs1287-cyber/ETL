package org.etlforge.ui.widget.combobox;

import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI.WindowsComboBoxEditor;
import javax.swing.ComboBoxEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

public class KongaWindowsComboBoxUI extends WindowsComboBoxUI {
   public static ComponentUI createUI(JComponent var0) {
      return new KongaWindowsComboBoxUI();
   }

   @Override
   protected ComboBoxEditor createEditor() {
      return new KongaWindowsComboBoxUI.UIResource();
   }

   public static class UIResource extends WindowsComboBoxEditor implements javax.swing.plaf.UIResource {
      @Override
      protected JTextField createEditorComponent() {
         KongaComboBoxEditorComponent var1 = new KongaComboBoxEditorComponent();
         Border var2 = (Border)UIManager.get("ComboBox.editorBorder");
         if (var2 != null) {
            var1.setBorder(var2);
         }

         var1.setOpaque(false);
         return var1;
      }

      @Override
      public void setItem(Object var1) {
         super.setItem(var1);
         if (this.editor.hasFocus()) {
            this.editor.selectAll();
         }
      }
   }
}
