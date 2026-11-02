package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.integration.client.ui.script.text.DefaultFunctionInserter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.DataElementAwareFunctionInserter;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.integration.data.script.ProjectItemFunctionInserter;

final class RegistryBasedFunctionInserterLookup implements FunctionInserterLookup {
   private final IntegrationProject project;
   private final FunctionArgumentSuggestions argSuggestions;
   private ScriptBuilderMode mode;
   private FunctionTester tester;
   private ExpressionParser parser;

   public RegistryBasedFunctionInserterLookup(IntegrationProject var1, FunctionArgumentSuggestions var2) {
      this.project = var1;
      this.argSuggestions = var2;
      this.mode = ScriptBuilderMode.DEVELOPER;
   }

   public void setFunctionTester(FunctionTester var1) {
      this.tester = var1;
   }

   public void setExpressionParser(ExpressionParser var1) {
      this.parser = var1;
   }

   public void setMode(ScriptBuilderMode var1) {
      this.mode = var1;
   }

   @Override
   public FunctionInserter getInserter(Function var1) {
      return this.mode == ScriptBuilderMode.DEVELOPER ? this.createDefaultInserter(var1) : this.createInserterForBusinessAnalyst(var1);
   }

   private FunctionInserter createDefaultInserter(Function var1) {
      DefaultFunctionInserter var2 = new DefaultFunctionInserter(var1);
      var2.setArgumentSuggestions(this.argSuggestions);
      return var2;
   }

   private FunctionInserter createInserterForBusinessAnalyst(Function var1) {
      FunctionRegistry var2 = FunctionRegistry.getRegistry();
      FunctionInserter var3 = var2.getFunctionInserter(var1);
      if (var3 == null) {
         return this.createDefaultInserter(var1);
      }

      if (var3 instanceof ProjectItemFunctionInserter) {
         ((ProjectItemFunctionInserter)var3).setProject(this.project);
      }

      if (var3 instanceof DataElementAwareFunctionInserter) {
         ((DataElementAwareFunctionInserter)var3).setDataElements(this.project.getGlobalDataElements());
      }

      if (var3 instanceof TestableFunctionInserter) {
         this.configureTesting((TestableFunctionInserter)var3);
      }

      return var3;
   }

   private void configureTesting(TestableFunctionInserter var1) {
      var1.setFunctionTester(this.tester);
      var1.setParser(this.parser);
   }
}
