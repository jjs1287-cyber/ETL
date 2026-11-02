package org.etlforge.application.ui.wizard;

public class WizardPageException extends Exception {
   private final WizardPage page;

   public WizardPageException(WizardPage var1) {
      this.page = var1;
   }

   public WizardPageException(WizardPage var1, String var2, Throwable var3) {
      super(var2, var3);
      this.page = var1;
   }

   public WizardPageException(WizardPage var1, String var2) {
      super(var2);
      this.page = var1;
   }

   public WizardPageException(WizardPage var1, Throwable var2) {
      super(var2);
      this.page = var1;
   }

   public WizardPage getPage() {
      return this.page;
   }
}
