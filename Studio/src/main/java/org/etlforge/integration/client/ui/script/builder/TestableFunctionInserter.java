package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.FunctionInserter;

public interface TestableFunctionInserter extends FunctionInserter {
   void setFunctionTester(FunctionTester var1);

   void setParser(ExpressionParser var1);
}
