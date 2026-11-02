package com.etlforge.integration.client.ui.sfdc.connector;

import java.awt.Component;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.salesforce.ConnectionResult;
import org.etlforge.system.callback.Receiver;

public final class DefaultTestConnectionResultDisplayer implements Receiver<ConnectionResult> {
   private final Component windowOwner;

   public DefaultTestConnectionResultDisplayer(Component var1) {
      this.windowOwner = var1;
   }

   public void handle(ConnectionResult var1) {
      if (!var1.wasCanceled()) {
         String var2 = var1.getMessage();
         if (var1.isSuccess()) {
            Alert.info(this.windowOwner, var2, "Success");
         } else if (var1.isError()) {
            Alert.error(this.windowOwner, var2, "Error");
         }
      }
   }
}
