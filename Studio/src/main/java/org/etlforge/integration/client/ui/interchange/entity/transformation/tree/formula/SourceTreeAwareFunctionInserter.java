package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import com.google.common.base.Supplier;
import org.etlforge.integration.data.script.FunctionInserter;

public interface SourceTreeAwareFunctionInserter extends FunctionInserter {
   void setSourceTree(Supplier<FormulaBuilderSourceTree> var1);
}
