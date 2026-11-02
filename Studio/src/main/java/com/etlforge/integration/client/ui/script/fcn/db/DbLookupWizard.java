package com.etlforge.integration.client.ui.script.fcn.db;

import com.etlforge.integration.client.ui.script.fcn.db.sql.Template;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.de.GlobalDataElements;

final class DbLookupWizard extends AbstractDbFunctionWizard {
   public DbLookupWizard(Function var1, List<Locatable> var2, GlobalDataElements var3, FormulaBuilderSourceTree var4) {
      super(var1, var2, var3, var4);
   }

   @Override
   protected DbWizardPageChain createPages(List<Locatable> var1, GlobalDataElements var2, FormulaBuilderSourceTree var3) {
      return new DbWizardPageChain(this, Template.SELECT, var1, var2, var3);
   }

   @Override
   protected String[] getSections() {
      return new String[]{"Database", "Build SQL"};
   }
}
