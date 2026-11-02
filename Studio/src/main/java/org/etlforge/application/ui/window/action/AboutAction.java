package org.etlforge.application.ui.window.action;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.AboutDialog;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.ui.UiProvider;

public final class AboutAction extends AbstractApplicationAction {
   public static final String ID = "action.About";
   private final Supplier<UiProvider> uiFactory;

   public AboutAction(Supplier<UiProvider> var1) {
      super("action.About", WindowActionDictionary.DICTIONARY);
      this.uiFactory = var1;
   }

   @Override
   public Object getValue(String var1) {
      return super.getValue(var1);
   }

   @Override
   public void putValue(String var1, Object var2) {
      super.putValue(var1, var2);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AboutAction.this.openDialog();
         }
      });
   }

   private void openDialog() {
      UiProvider var1 = this.uiFactory.get();
      AboutDialog var2 = new AboutDialog(var1);
      var2.display();
   }
}
