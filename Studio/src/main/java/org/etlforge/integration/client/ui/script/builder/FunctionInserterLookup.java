package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.integration.client.ui.script.text.DefaultFunctionInserter;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;

public interface FunctionInserterLookup {
   FunctionInserterLookup DEFAULT = new FunctionInserterLookup() {
      @Override
      public FunctionInserter getInserter(Function var1) {
         return new DefaultFunctionInserter(var1);
      }
   };

   FunctionInserter getInserter(Function var1);
}
