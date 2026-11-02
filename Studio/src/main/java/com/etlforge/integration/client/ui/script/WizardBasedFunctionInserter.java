package com.etlforge.integration.client.ui.script;

import com.etlforge.integration.client.ui.script.fcn.Expression;
import javax.swing.Icon;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardAdapter;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.text.FunctionCompletionToken;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public abstract class WizardBasedFunctionInserter implements FunctionInserter {
   protected final Function function;

   public WizardBasedFunctionInserter(Function var1) {
      this.function = var1;
   }

   @Override
   public final void insertFunction(final JTextComponent var1) {
      Wizard var2 = this.createWizard();
      if (var2 != null) {
         var2.addWizardListener(new WizardAdapter<Expression>() {
            public void wizardCompleted(Expression var1x) {
               var1x.insertInto(var1);
            }
         });
         var2.start();
      }
   }

   protected abstract Wizard<Expression> createWizard();

   @Override
   public final CompletionToken getCompletionToken(int var1, int var2, JTextComponent var3) {
      return new WizardBasedFunctionInserter.CompletionTokenImpl(var1, var2, var3);
   }

   private class CompletionTokenImpl implements CompletionToken {
      private final int startPos;
      private final int cursorPos;
      private final JTextComponent textArea;
      private CompletionToken delegate;
      private final String info;

      public CompletionTokenImpl(int var2, int var3, JTextComponent var4) {
         this.startPos = var2;
         this.cursorPos = var3;
         this.textArea = var4;
         this.info = FunctionRegistry.getRegistry().getFunctionInfo(WizardBasedFunctionInserter.this.function.getName());
      }

      @Override
      public void complete(Document var1) {
         Wizard var2 = WizardBasedFunctionInserter.this.createWizard();
         if (var2 == null) {
            this.delegate = new FunctionCompletionToken(WizardBasedFunctionInserter.this.function, this.startPos, this.cursorPos, this.textArea);
            this.delegate.complete(var1);
         } else {
            this.completeThroughWizard(var2);
         }
      }

      private void completeThroughWizard(Wizard<Expression> var1) {
         var1.addWizardListener(new WizardAdapter<Expression>() {
            public void wizardCompleted(Expression var1) {
               var1.autoComplete(CompletionTokenImpl.this.textArea, CompletionTokenImpl.this.startPos);
            }
         });
         var1.start();
      }

      @Override
      public Interval getInterval(Document var1) {
         return this.delegate != null ? this.delegate.getInterval(var1) : IntervalFactory.fromStartAndLength(this.cursorPos, 0);
      }

      @Override
      public String getDisplayValue() {
         return WizardBasedFunctionInserter.this.function.getName();
      }

      @Override
      public String getInfo() {
         return this.info;
      }

      @Override
      public Icon getIcon() {
         return Icons.FORMULA_ICON;
      }
   }
}
