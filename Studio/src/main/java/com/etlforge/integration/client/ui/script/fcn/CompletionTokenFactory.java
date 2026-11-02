package com.etlforge.integration.client.ui.script.fcn;

import javax.swing.Icon;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.text.FunctionCompletionToken;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public abstract class CompletionTokenFactory {
   private final Function function;

   protected CompletionTokenFactory(Function var1) {
      this.function = var1;
   }

   protected abstract SimpleFunctionWizard createWizard();

   public final CompletionToken getCompletionToken(int var1, int var2, JTextComponent var3) {
      return new CompletionTokenFactory.CompletionTokenImpl(var1, var2, var3);
   }

   private class CompletionTokenImpl implements CompletionToken {
      private final int startPos;
      private final int cursorPos;
      private final String info;
      private final JTextComponent textComponent;

      public CompletionTokenImpl(int var2, int var3, JTextComponent var4) {
         this.startPos = var2;
         this.cursorPos = var3;
         this.textComponent = var4;
         this.info = FunctionRegistry.getRegistry().getFunctionInfo(CompletionTokenFactory.this.function.getName());
      }

      @Override
      public void complete(Document var1) {
         SimpleFunctionWizard var2 = CompletionTokenFactory.this.createWizard();
         if (var2 == null) {
            this.doDefaultCompletion(var1);
         } else {
            this.completeThroughWizard(var2);
         }
      }

      private void doDefaultCompletion(Document var1) {
         FunctionCompletionToken var2 = new FunctionCompletionToken(CompletionTokenFactory.this.function, this.startPos, this.cursorPos, this.textComponent);
         var2.complete(var1);
      }

      private void completeThroughWizard(SimpleFunctionWizard var1) {
         Expression var2 = var1.run();
         if (var2 != null) {
            var2.autoComplete(this.textComponent, this.startPos);
         }
      }

      @Override
      public Interval getInterval(Document var1) {
         return IntervalFactory.fromStartAndLength(this.cursorPos, 0);
      }

      @Override
      public String getDisplayValue() {
         return CompletionTokenFactory.this.function.getName();
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
