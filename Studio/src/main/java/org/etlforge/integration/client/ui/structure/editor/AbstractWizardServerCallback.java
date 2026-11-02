package org.etlforge.integration.client.ui.structure.editor;

import com.google.common.base.Preconditions;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.client.server.ServerCallback;

public abstract class AbstractWizardServerCallback implements ServerCallback {
   private final WizardPage page;
   private final CountDownLatch latch;
   private boolean success = true;
   private String errorMessage;

   public AbstractWizardServerCallback(WizardPage var1, CountDownLatch var2) {
      Preconditions.checkNotNull(var1, "page");
      this.page = var1;
      this.latch = var2;
   }

   @Override
   public void cancelled() {
      this.success = false;
      this.countDown();
   }

   @Override
   public void caught(Throwable var1) {
      this.success = false;
      this.page.getWizard().handlePageException(this.errorMessage, var1);
      this.countDown();
   }

   protected final void setErrorMessage(String var1) {
      this.errorMessage = var1;
   }

   protected final void countDown() {
      if (this.latch != null) {
         this.latch.countDown();
      }
   }

   public final boolean succeeded() {
      return this.success;
   }
}
