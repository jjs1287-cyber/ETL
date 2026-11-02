package org.etlforge.integration.client.ui.de.global;

import javax.swing.JComponent;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.selection.SelectFirstAction;
import org.etlforge.ui.widget.text.KongaTextField;

public final class BrowserFilterField implements UiProvider {
   private final GlobalDataElementsBrowser browser;
   private final KongaTextField field;
   private final StringMatcher matcher;

   public BrowserFilterField(GlobalDataElementsBrowser var1, StringMatcher var2, String var3) {
      this.browser = var1;
      this.matcher = var2;
      this.field = new KongaTextField(20);
      this.field.setToolTipText(var3);
      SelectFirstAction.installInField(var1, this.field);
      this.listenToField();
   }

   private void listenToField() {
      Runnable var1 = new Runnable() {
         @Override
         public void run() {
            String var1x = BrowserFilterField.this.field.getText().trim();
            StringMatcherPredicate var2 = new StringMatcherPredicate(BrowserFilterField.this.matcher, var1x);
            BrowserFilterField.this.browser.setNameFilter(var2);
         }
      };
      DelayedDocumentContentChangeReaction var2 = new DelayedDocumentContentChangeReaction(var1);
      var2.install(this.field);
   }

   public JComponent getUi() {
      return Horizontal.box().nonOpaque().of("Filter: ", this.field).container();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.getUi());
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }
}
