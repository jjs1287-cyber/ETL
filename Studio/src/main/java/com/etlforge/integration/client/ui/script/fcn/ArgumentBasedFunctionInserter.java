package com.etlforge.integration.client.ui.script.fcn;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.script.fcn.arg.ArgumentBasedFunctionWizard;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.SourceTreeAwareFunctionInserter;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.client.ui.script.builder.TestableFunctionInserter;
import org.etlforge.integration.data.script.ArgumentDescriptor;
import org.etlforge.integration.data.script.DataElementAwareFunctionInserter;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.ui.text.contentassist.CompletionToken;

public final class ArgumentBasedFunctionInserter
   implements FunctionInserter,
   DataElementAwareFunctionInserter,
   SourceTreeAwareFunctionInserter,
   TestableFunctionInserter {
   private final Function function;
   private final ImmutableList<ArgumentDescriptor> argDescriptors;
   private Supplier<FormulaBuilderSourceTree> sourceTreeFactory;
   private GlobalDataElements globalVariables;
   private FunctionTester tester;
   private ExpressionParser parser;
   private boolean testable = true;
   private boolean questionLayout;

   public ArgumentBasedFunctionInserter(Function var1, List<ArgumentDescriptor> var2) {
      this.function = var1;
      this.argDescriptors = ImmutableList.copyOf(var2);
   }

   @Override
   public void setSourceTree(Supplier<FormulaBuilderSourceTree> var1) {
      this.sourceTreeFactory = var1;
   }

   @Override
   public void setDataElements(GlobalDataElements var1) {
      this.globalVariables = var1;
   }

   @Override
   public void setFunctionTester(FunctionTester var1) {
      this.tester = var1;
   }

   @Override
   public void setParser(ExpressionParser var1) {
      this.parser = var1;
   }

   public boolean isTestable() {
      return this.testable;
   }

   public void setTestable(boolean var1) {
      this.testable = var1;
   }

   public boolean isQuestionLayout() {
      return this.questionLayout;
   }

   public void setQuestionLayout(boolean var1) {
      this.questionLayout = var1;
   }

   @Override
   public void insertFunction(JTextComponent var1) {
      ArgumentBasedFunctionWizard var2 = this.createWizard();
      Expression var3 = var2.run();
      if (var3 != null) {
         var3.insertInto(var1);
      }
   }

   private ArgumentBasedFunctionWizard createWizard() {
      ArgumentBasedFunctionWizard var1 = new ArgumentBasedFunctionWizard(this.function, this.argDescriptors);
      var1.setQuestionLayout(this.questionLayout);
      if (this.sourceTreeFactory != null) {
         var1.setSourceTree(this.sourceTreeFactory.get());
      }

      if (this.globalVariables != null) {
         var1.setGlobalVariables(this.globalVariables);
      }

      if (this.testable) {
         var1.setExpressionParser(this.parser);
         var1.setFunctionTester(this.tester);
      }

      return var1;
   }

   @Override
   public CompletionToken getCompletionToken(int var1, int var2, JTextComponent var3) {
      ArgumentBasedFunctionInserter.TokenFactoryImpl var4 = new ArgumentBasedFunctionInserter.TokenFactoryImpl();
      return var4.getCompletionToken(var1, var2, var3);
   }

   private class TokenFactoryImpl extends CompletionTokenFactory {
      public TokenFactoryImpl() {
         super(ArgumentBasedFunctionInserter.this.function);
      }

      @Override
      protected SimpleFunctionWizard createWizard() {
         return ArgumentBasedFunctionInserter.this.createWizard();
      }
   }
}
