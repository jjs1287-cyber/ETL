package com.etlforge.integration.client.ui.script.fcn.runop;

import com.etlforge.integration.client.ui.script.fcn.Expression;
import java.util.List;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardDialogDisplayer;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.script.Function;

final class RunOperationWizard extends Wizard<Expression> {
   public static final String OPERATION = "Operation";
   public static final String OPTIONS = "Options";
   private final Function runOpFunction;
   private final RunOperationPageChain pages;

   public RunOperationWizard(Function var1, List<Operation> var2) {
      this.runOpFunction = var1;
      WizardDialogDisplayer var3 = this.createDisplayer();
      this.setDisplayer(var3);
      this.pages = new RunOperationPageChain(this, var2);
      this.setPageChain(this.pages);
   }

   @Override
   public boolean canFinish() {
      WizardPage var1 = this.getCurrentPage();
      return var1.canContinue() && this.pages.canFinish(var1);
   }

   protected Expression finishWizard() {
      ExpressionBuilder var1 = new ExpressionBuilder(this.runOpFunction, this.pages);
      return new Expression(var1.buildExpression());
   }

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }

   private WizardDialogDisplayer createDisplayer() {
      WizardDialogDisplayer var1 = new WizardDialogDisplayer();
      var1.setSections(new String[]{"Operation", "Options"});
      var1.setManageLocation(this.getClass());
      var1.setModal(true);
      var1.setWizard(this);
      return var1;
   }

   @Override
   public void start() {
      super.start();
      this.getDisplayer().setSection("Operation");
   }
}
