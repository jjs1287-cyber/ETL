package com.etlforge.integration.client.ui.script.fcn.db;

import com.etlforge.integration.client.ui.script.fcn.Expression;
import com.etlforge.integration.client.ui.script.fcn.FunctionWizard;
import java.awt.Dimension;
import java.util.List;
import javax.swing.ImageIcon;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardDialogDisplayer;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.de.GlobalDataElements;

abstract class AbstractDbFunctionWizard extends Wizard<Expression> implements FunctionWizard {
   private final Function wizard;
   private final DbWizardPageChain pages;

   protected AbstractDbFunctionWizard(Function var1, List<Locatable> var2, GlobalDataElements var3, FormulaBuilderSourceTree var4) {
      this.wizard = var1;
      this.pages = this.createPages(var2, var3, var4);
      this.setPageChain(this.pages);
      WizardDialogDisplayer var5 = this.createDisplayer();
      this.setDisplayer(var5);
   }

   protected abstract DbWizardPageChain createPages(List<Locatable> var1, GlobalDataElements var2, FormulaBuilderSourceTree var3);

   private WizardDialogDisplayer createDisplayer() {
      WizardDialogDisplayer var1 = new WizardDialogDisplayer();
      var1.setSections(this.getSections());
      var1.setManageLocation(this.getClass());
      var1.setMinimumSize(new Dimension(600, 400));
      if (ClientIcons.FORMULA_16 instanceof ImageIcon) {
         var1.setDialogImage(((ImageIcon)ClientIcons.FORMULA_16).getImage());
      }

      var1.setModal(true);
      var1.setWizard(this);
      return var1;
   }

   protected abstract String[] getSections();

   @Override
   public final void start() {
      super.start();
      this.getDisplayer().setSection("Database");
   }

   protected final Expression finishWizard() {
      return this.buildExpression();
   }

   @Override
   public final Expression buildExpression() {
      ExpressionBuilder var1 = new ExpressionBuilder(this.wizard, this.pages);
      return var1.buildExpression();
   }

   @Override
   protected final boolean onWizardCancelled() {
      return true;
   }

   @Override
   public final void showTestSection() {
      WizardPage var1 = this.getCurrentPage();
      if (var1 instanceof BuildSqlStatementPage) {
         ((BuildSqlStatementPage)var1).showTestSection();
      }
   }

   public final void installTestingTools(FunctionTester var1, ExpressionParser var2) {
      ((DbWizardPageChain)this.getPageChain()).installTestingTools(var1, var2);
   }
}
