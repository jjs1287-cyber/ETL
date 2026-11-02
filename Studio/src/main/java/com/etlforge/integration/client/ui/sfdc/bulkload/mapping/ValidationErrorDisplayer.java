package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.awt.Component;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.system.callback.Receiver;

public final class ValidationErrorDisplayer implements Receiver<MappingsValidator.Error> {
   private final Component dialogOwner;

   public ValidationErrorDisplayer(Component var1) {
      this.dialogOwner = var1;
   }

   public void handle(MappingsValidator.Error var1) {
      NonBlockingAlert.error(this.dialogOwner, var1.message, var1.title);
   }
}
