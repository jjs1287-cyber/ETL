package org.etlforge.integration.client.ui.validation.indicator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.validation.EntityValidationResultDisplayerFactory;
import org.etlforge.integration.client.ui.validation.resources.ValidationUiResources;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.window.PopupWindow;

abstract class AbstractValidationResultTrigger implements ValidationResultTrigger {
   private final JComponent component = this.createTransientTrigger();
   private EntityCollectionValidationResult validationResult;
   private PopupWindow popup;

   protected AbstractValidationResultTrigger() {
   }

   @Override
   public final JComponent getComponent() {
      return this.component;
   }

   @Override
   public void update(EntityCollectionValidationResult var1) {
      this.validationResult = var1;
   }

   @Override
   public final void setEnabled(boolean var1) {
      if (!var1 && this.popup != null) {
         this.popup.dispose();
         this.popup = null;
      }
   }

   protected abstract JComponent createTransientTrigger();

   protected abstract JComponent createValidTrigger();

   protected abstract JComponent createInvalidTrigger();

   protected final boolean isValid() {
      return isValid(this.validationResult);
   }

   private static boolean isValid(EntityCollectionValidationResult var0) {
      return var0 == null || var0.isEverythingOk();
   }

   protected final void showValidationProblems(Component var1, Point var2) {
      this.popup = this.createPopupWindow(var1);
      Point var3 = getPopupWindowLocation(var1, var2);
      this.popup.show(var3);
   }

   private PopupWindow createPopupWindow(Component var1) {
      JComponent var2 = this.createPopupWindowContent();
      PopupWindow var3 = PopupWindow.newWindow(var1, var2, null);
      this.addPopupWindowTitle(var3);
      var3.setMaximumSize(new Dimension(600, 400));
      var3.setResizable(true);
      return var3;
   }

   private void addPopupWindowTitle(PopupWindow var1) {
      String var2 = ValidationUiResources.getString("Validator.Title");
      TextStyle var3 = TextStyles.DefaultErrorText;
      LabelDecorator var4 = StyleSheet.createPopupWindowTitle(var2, var3);
      var1.setTitle(var4, true);
   }

   private JComponent createPopupWindowContent() {
      UiProvider var1 = EntityValidationResultDisplayerFactory.newTreeDisplayer(this.validationResult);
      return BorderLayoutBuilder.fillWith(var1);
   }

   private static Point getPopupWindowLocation(Component var0, Point var1) {
      SwingUtilities.convertPointToScreen(var1, var0);
      return var1;
   }
}
