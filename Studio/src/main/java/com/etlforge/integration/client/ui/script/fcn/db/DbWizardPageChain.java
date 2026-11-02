package com.etlforge.integration.client.ui.script.fcn.db;

import com.google.common.collect.Maps;
import com.etlforge.integration.client.ui.script.fcn.db.sql.Template;
import java.util.List;
import java.util.Map;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.serverapi.JdbcInfoProvider;
import org.etlforge.integration.client.database.serverapi.OdbcInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;

final class DbWizardPageChain implements WizardPageChain {
   private final AbstractDbFunctionWizard wizard;
   private final DatabaseSelectionPage dbSelectionPage;
   private final StatementTypeSelectionPage templatePage;
   private final GlobalDataElements dataElements;
   private final FormulaBuilderSourceTree sourceTree;
   private BuildSqlStatementPage sqlStatementPage;
   private final Map<Locatable, BuildSqlStatementPage> sqlStatementPages;
   private final Template fixedTemplate;
   private FunctionTester tester;
   private ExpressionParser parser;

   public DbWizardPageChain(AbstractDbFunctionWizard var1, List<Locatable> var2, GlobalDataElements var3, FormulaBuilderSourceTree var4) {
      this(var1, null, var2, var3, var4);
   }

   public DbWizardPageChain(AbstractDbFunctionWizard var1, Template var2, List<Locatable> var3, GlobalDataElements var4, FormulaBuilderSourceTree var5) {
      this.wizard = var1;
      this.fixedTemplate = var2;
      this.dataElements = var4;
      this.sourceTree = var5;
      this.dbSelectionPage = new DatabaseSelectionPage(var1, var3);
      this.templatePage = var2 == null ? new StatementTypeSelectionPage(var1) : null;
      this.sqlStatementPages = Maps.newHashMap();
   }

   @Override
   public WizardPage getFirstPage() {
      return this.dbSelectionPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.sqlStatementPage;
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 != this.sqlStatementPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.dbSelectionPage) {
         return this.templatePage != null ? this.selectTemplate() : this.buildSql();
      } else {
         return var1 != null && var1 == this.templatePage ? this.buildSql() : null;
      }
   }

   private WizardPage selectTemplate() {
      this.wizard.getDisplayer().setSection("Type of Statement");
      return this.templatePage;
   }

   private WizardPage buildSql() {
      this.sqlStatementPage = this.getSqlStatementPage();
      Template var1 = this.getTemplate();
      this.sqlStatementPage.setTemplate(var1);
      this.wizard.getDisplayer().setSection("Build SQL");
      return this.sqlStatementPage;
   }

   private BuildSqlStatementPage getSqlStatementPage() {
      Locatable var1 = this.dbSelectionPage.getSelectedDatabase();
      BuildSqlStatementPage var2 = this.sqlStatementPages.get(var1);
      if (var2 == null) {
         DatabaseInfoProvider var3 = this.getInfoProvider(var1);
         var2 = new BuildSqlStatementPage(this.wizard, var1, this.dataElements, this.sourceTree, var3);
         Template var4 = this.getTemplate();
         var2.setTemplate(var4);
         if (this.tester != null) {
            var2.installTestingTools(this.tester, this.parser);
         }

         this.sqlStatementPages.put(var1, var2);
      }

      return var2;
   }

   private DatabaseInfoProvider getInfoProvider(Locatable var1) {
      Class<? extends DatabaseInfoProvider> var2 = this.isJdbc(var1) ? JdbcInfoProvider.class : OdbcInfoProvider.class;
      IntegrationServer var3 = IntegrationServer.getInstance();
      return var3.getServerCall(var2);
   }

   private boolean isJdbc(Locatable var1) {
      DatabaseLocation var2 = (DatabaseLocation)var1.getLocation();
      DriverDescriptor var3 = var2.getDriver();
      return var3 != null && var3.getType() == DriverType.JDBC;
   }

   private Template getTemplate() {
      return this.fixedTemplate != null ? this.fixedTemplate : this.templatePage.getSelectedTemplate();
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.dbSelectionPage;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.sqlStatementPage) {
         return this.templatePage != null ? this.backToTemplatePage() : this.backToDbSelection();
      } else {
         return var1 != null && var1 == this.templatePage ? this.backToDbSelection() : null;
      }
   }

   private WizardPage backToTemplatePage() {
      this.wizard.getDisplayer().setSection("Type of Statement");
      return this.templatePage;
   }

   private WizardPage backToDbSelection() {
      this.wizard.getDisplayer().setSection("Database");
      return this.dbSelectionPage;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.sqlStatementPage;
   }

   public Locatable getSelectedDatabase() {
      return this.dbSelectionPage.getSelectedDatabase();
   }

   public String getSql() {
      return this.sqlStatementPage.getSql();
   }

   public void installTestingTools(FunctionTester var1, ExpressionParser var2) {
      this.tester = var1;
      this.parser = var2;
   }
}
