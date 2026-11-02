package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import org.etlforge.integration.client.ui.script.builder.FunctionInserterLookup;
import org.etlforge.integration.client.ui.script.builder.TestableFunctionInserter;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;

final class FormulaBuilderFunctionInserterLookup implements FunctionInserterLookup {
   private final FunctionInserterLookup lookup;
   private final SourceTreeFactory sourceTreeFactory;
   private ExpressionParser parser;

   public FormulaBuilderFunctionInserterLookup(FunctionInserterLookup var1, SourceTreeFactory var2) {
      this.lookup = var1;
      this.sourceTreeFactory = var2;
   }

   public void setParser(ExpressionParser var1) {
      this.parser = var1;
   }

   @Override
   public FunctionInserter getInserter(Function var1) {
      FunctionInserter var2 = this.lookup.getInserter(var1);
      if (var2 instanceof SourceTreeAwareFunctionInserter) {
         ((SourceTreeAwareFunctionInserter)var2).setSourceTree(this.sourceTreeFactory);
      }

      if (var2 instanceof TestableFunctionInserter && this.parser != null) {
         ((TestableFunctionInserter)var2).setParser(this.parser);
      }

      return var2;
   }
}
