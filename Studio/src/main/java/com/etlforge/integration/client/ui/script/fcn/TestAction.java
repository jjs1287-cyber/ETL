package com.etlforge.integration.client.ui.script.fcn;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.ui.debug.DebugIcons;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.ScriptStatus;

public final class TestAction extends AbstractAction {
   private final FunctionWizard wizard;
   private final TestController testController;
   private final ExpressionScanner scanner;
   private final ExpressionParser parser;
   private final boolean checkForVariablesWithoutValues;

   public TestAction(FunctionWizard var1, TestController var2, ExpressionScanner var3, ExpressionParser var4, boolean var5) {
      super("Test", DebugIcons.START_16);
      this.wizard = var1;
      this.testController = var2;
      this.scanner = var3;
      this.parser = var4;
      this.checkForVariablesWithoutValues = var5;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestAction.this.validateAndStartTest();
         }
      });
   }

   private void validateAndStartTest() {
      Expression var1 = this.wizard.buildExpression();
      if (this.validate(var1)) {
         if (this.checkForVariablesWithoutValues && !this.allVariablesHaveValues()) {
            return;
         }

         this.testController.startTest(var1);
      }
   }

   private boolean allVariablesHaveValues() {
      if (this.scanner.hasDataElementsWithoutValue()) {
         this.wizard.showTestSection();
         Alert.warning(
            "The expression uses one or more data elements that do not have values.\n\nPlease provide values in the table below.", "Variables without value"
         );
         return false;
      } else {
         return true;
      }
   }

   private boolean validate(Expression var1) {
      if (this.parser == null) {
         return true;
      }

      ExpressionParser.Result var2 = var1.parse(this.parser);
      ScriptStatus var3 = var2.getStatus();
      switch (var3.getState()) {
         case WARNING:
         case VALID:
            return true;
         case INVALID:
            NonBlockingAlert.error(var3.getMessage(), "Syntax error");
            return false;
         default:
            throw new RuntimeException("Unexpected validation status: " + var3.getState());
      }
   }
}
