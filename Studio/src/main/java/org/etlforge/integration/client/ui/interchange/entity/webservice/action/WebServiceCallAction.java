package org.etlforge.integration.client.ui.interchange.entity.webservice.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.WebServiceCall;

public abstract class WebServiceCallAction extends AbstractSingleActiveObjectAction<WebServiceCall> {
   private final ApplicationWindow appWin;

   public WebServiceCallAction(ApplicationWindow var1, String var2) {
      super(var1.getActiveObjectService(), var2, WebServiceCallActionDictionary.DICTIONARY, WebServiceCall.class);
      this.appWin = var1;
   }

   protected final ApplicationWindow getWindow() {
      return this.appWin;
   }

   protected final void runOnObject(final WebServiceCall var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            boolean var1x = UiCompanion.of(var1).saveIfDirty();
            if (var1x) {
               WebServiceCallAction.this.performActionOn(var1);
            }
         }
      });
   }

   protected abstract void performActionOn(WebServiceCall var1);
}
