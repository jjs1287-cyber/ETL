package org.etlforge.integration.client.server;

import org.etlforge.application.errorlog.ErrorLog;

public abstract class AbstractServerCallback implements ServerCallback {
   private String errorTitle;

   @Override
   public void caught(Throwable var1) {
      String var2 = this.errorTitle != null && this.errorTitle.length() > 0 ? this.errorTitle : "An error occurred";
      ErrorLog.attention(this.getClass(), var2, var1);
   }

   @Override
   public void cancelled() {
   }

   public final void setErrorTitle(String var1) {
      this.errorTitle = var1;
   }
}
