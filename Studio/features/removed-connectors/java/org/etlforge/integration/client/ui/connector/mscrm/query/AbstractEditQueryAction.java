package org.etlforge.integration.client.ui.connector.mscrm.query;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.connector.function.ConnectorCallPage;
import org.etlforge.system.callback.Receiver;

abstract class AbstractEditQueryAction extends AbstractAction {
   private final Receiver<String> resultReceiver;
   private ConnectorCallPage page;
   private String currentXml;

   protected AbstractEditQueryAction(String var1, Receiver<String> var2) {
      super(var1);
      this.resultReceiver = var2;
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AbstractEditQueryAction.this.edit();
         }
      });
   }

   protected abstract void edit();

   public final String getCurrentXml() {
      return this.currentXml;
   }

   public final void setCurrentXml(String var1) {
      this.currentXml = var1;
   }

   public final void associateWithPage(ConnectorCallPage var1) {
      this.page = var1;
   }

   public ConnectorCallPage getPage() {
      Preconditions.checkState(this.page != null, "This property has not been associated with a page");
      return this.page;
   }

   protected final void applyChange(String var1) {
      this.resultReceiver.handle(var1);
   }
}
