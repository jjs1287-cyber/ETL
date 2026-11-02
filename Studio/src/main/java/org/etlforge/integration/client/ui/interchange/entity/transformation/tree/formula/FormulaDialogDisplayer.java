package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;

public final class FormulaDialogDisplayer extends AbstractFormulaDisplayer {
   public FormulaDialogDisplayer(FormulaBuilderUiFactory var1) {
      super(var1);
   }

   @Override
   protected AbstractFormulaBuilder createFormulaBuilder(TreeMapper var1, String var2, String var3, NodeMapping var4) {
      return new FormulaDialog(var1.getTreeMapperContainer().getProject(), var1, this.getUiFactory(), var2, var3, var4);
   }

   @Override
   protected boolean isBlocking() {
      return true;
   }
}
