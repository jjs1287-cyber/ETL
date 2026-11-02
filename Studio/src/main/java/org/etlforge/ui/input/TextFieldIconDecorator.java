package org.etlforge.ui.input;

import java.awt.Component;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.PostRenderingComponentPainter;
import org.etlforge.ui.widget.text.KongaTextComponentWithIconSupport;

public class TextFieldIconDecorator implements InputFieldDecorator {
   private static final Icon icon = UiResourceBundle.getIcon("Invalid.16");
   private boolean active = true;
   private final ComponentPainter painter = new TextFieldIconDecorator.PainterImpl();

   @Override
   public void decorateField(InputField var1, boolean var2) {
      JComponent var3 = var1.getInputComponent();
      if (var3 instanceof KongaTextComponentWithIconSupport) {
         this.decorate((KongaTextComponentWithIconSupport)var3, var2);
      } else if (var3 instanceof KongaWidget) {
         this.decorate((KongaWidget)var3, var2);
      }
   }

   private void decorate(KongaTextComponentWithIconSupport var1, boolean var2) {
      if (this.active && !var2) {
         var1.setIcon(icon);
      } else {
         var1.setIcon(null);
      }
   }

   private void decorate(KongaWidget var1, boolean var2) {
      if (this.active && !var2) {
         var1.setPainter(this.painter);
      } else {
         var1.setPainter(null);
      }
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void setActive(boolean var1) {
      this.active = var1;
   }

   private static class PainterImpl extends PostRenderingComponentPainter {
      private static final int xInset = calculateXInset();

      private PainterImpl() {
      }

      private static int calculateXInset() {
         JTextField var0 = new JTextField();
         return var0.getInsets().right + 1;
      }

      @Override
      public void postRendering(Component var1, Graphics2D var2) {
         int var3 = var1.getWidth() - TextFieldIconDecorator.icon.getIconWidth() - xInset;
         int var4 = (var1.getHeight() - TextFieldIconDecorator.icon.getIconHeight()) / 2;
         TextFieldIconDecorator.icon.paintIcon(var1, var2, var3, var4);
      }
   }
}
