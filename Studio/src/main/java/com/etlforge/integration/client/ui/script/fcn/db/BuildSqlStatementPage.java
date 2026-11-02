package com.etlforge.integration.client.ui.script.fcn.db;

import com.etlforge.integration.client.ui.script.fcn.ExpressionScanner;
import com.etlforge.integration.client.ui.script.fcn.FunctionWizard;
import com.etlforge.integration.client.ui.script.fcn.TestAction;
import com.etlforge.integration.client.ui.script.fcn.TestConsole;
import com.etlforge.integration.client.ui.script.fcn.TestController;
import com.etlforge.integration.client.ui.script.fcn.TestWaitIndicator;
import com.etlforge.integration.client.ui.script.fcn.db.sql.Template;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsController;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.SourceTreeDataElementContainer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.ui.widget.DocumentContentChangeListener;

final class BuildSqlStatementPage extends AbstractWizardPage {
   private final DbObjectsController controller;
   private final BuildSqlStatementPageUi ui;
   private final FormulaBuilderSourceTree sourceTree;
   private TestConsole testConsole;

   public BuildSqlStatementPage(
      AbstractDbFunctionWizard var1, Locatable var2, GlobalDataElements var3, FormulaBuilderSourceTree var4, DatabaseInfoProvider var5
   ) {
      super(var1, "Build the SQL Statement");
      this.sourceTree = var4;
      this.controller = new DbObjectsController(var5);
      this.ui = new BuildSqlStatementPageUi(var2, var3, var4, this.controller);
      this.ui.addChangeListener(new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            BuildSqlStatementPage.this.updateNextPageEnabled();
         }
      });
      this.updateNextPageEnabled();
   }

   private void updateNextPageEnabled() {
      boolean var1 = this.ui.isValid();
      this.setCanContinue(var1);
   }

   public void setTemplate(Template var1) {
      this.ui.setTemplate(var1);
   }

   public String getSql() {
      return this.ui.getSql();
   }

   public void installTestingTools(FunctionTester var1, ExpressionParser var2) {
      ScriptDataElementsModel var3 = new ScriptDataElementsModel();
      ExpressionScanner var4 = new ExpressionScanner((FunctionWizard)this.getWizard(), var3);
      var4.setParser(var2);
      var4.setTester(var1);
      TestController var5 = new TestController(var1, var3);
      TestWaitIndicator var6 = new TestWaitIndicator();
      var5.setWaitService(this.getWaitService());
      var5.setTestWaitIndicator(var6);
      if (this.sourceTree != null) {
         var2.setSourceDataElementContainer(new SourceTreeDataElementContainer(this.sourceTree));
      }

      TestAction var7 = new TestAction((AbstractDbFunctionWizard)this.getWizard(), var5, var4, var2, false);
      this.testConsole = new TestConsole(var3, var7);
      TestAction var8 = new TestAction((AbstractDbFunctionWizard)this.getWizard(), var5, var4, var2, true);
      this.ui.addChangeListener(var4);
      this.ui.installTestConsole(this.testConsole, var8);
   }

   @Override
   protected void onFirstDisplay() {
      this.controller.downloadTables(TableSearchFilter.TABLE_AND_VIEWS);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Build SQL Statement");
      var1.add(this.ui, true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      this.getUndoRedoInstaller().installIntoComponentTree(var2);
      return var2;
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected void dispose() {
      if (this.testConsole != null) {
         this.testConsole.dispose();
      }
   }

   public final void showTestSection() {
      this.ui.showTestSection();
   }
}
