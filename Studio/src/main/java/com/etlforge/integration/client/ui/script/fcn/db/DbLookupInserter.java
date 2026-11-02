package com.etlforge.integration.client.ui.script.fcn.db;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.script.Function;

public final class DbLookupInserter extends AbstractDbFunctionInserter {
   public DbLookupInserter(Function var1) {
      super(var1);
      Preconditions.checkArgument(var1.getName().toLowerCase().equals("dblookup"), "Unexpected function: " + var1.getName());
   }

   protected DbLookupWizard createWizard(List<Locatable> var1, FormulaBuilderSourceTree var2) {
      return new DbLookupWizard(this.function, var1, this.deStore, var2);
   }
}
