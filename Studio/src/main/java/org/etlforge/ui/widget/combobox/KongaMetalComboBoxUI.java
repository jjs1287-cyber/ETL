package org.etlforge.ui.widget.combobox;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.ComboBoxEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalComboBoxEditor;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class KongaMetalComboBoxUI extends MetalComboBoxUI {
   public static ComponentUI createUI(JComponent var0) {
      return new KongaMetalComboBoxUI();
   }

   @Override
   protected ComboBoxEditor createEditor() {
      return new KongaMetalComboBoxUI.UIResource();
   }

   public static class UIResource extends MetalComboBoxEditor implements javax.swing.plaf.UIResource {
      public UIResource() {
         this.editor = this.createEditorComponent();
      }

      @Override
      protected JTextField createEditorComponent() {
         KongaComboBoxEditorComponent var1 = new KongaComboBoxEditorComponent() {
            @Override
            public Dimension getPreferredSize() {
               Dimension var1x = super.getPreferredSize();
               var1x.height += 4;
               return var1x;
            }

            @Override
            public Dimension getMinimumSize() {
               Dimension var1x = super.getMinimumSize();
               var1x.height += 4;
               return var1x;
            }
         };
         var1.setBorder(new KongaMetalComboBoxUI.UIResource.EditorBorder());
         return var1;
      }

      private static class EditorBorder extends AbstractBorder {
         private EditorBorder() {
         }

         @Override
         public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
            var2.translate(var3, var4);
            var2.setColor(MetalLookAndFeel.getControlDarkShadow());
            var2.drawRect(0, 0, var5, var6 - 1);
            var2.setColor(MetalLookAndFeel.getControlShadow());
            var2.drawRect(1, 1, var5 - 2, var6 - 3);
            var2.translate(-var3, -var4);
         }

         @Override
         public Insets getBorderInsets(Component var1) {
            return new Insets(2, 4, 2, 0);
         }
      }
   }
}
