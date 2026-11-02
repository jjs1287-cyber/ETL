package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Maps;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.serverapi.JdbcInfoProvider;
import org.etlforge.integration.client.database.serverapi.OdbcInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.data.structure.database.DbTables;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.data.structure.database.DbTranRelations;
import org.etlforge.integration.data.structure.database.SqlScript;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.DocumentContentChangeListener;

public class ManualSqlStatementPage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final DatabaseLocation dbLocation;
   private final SourceId sourceId;
   private final ManualSqlStatementPageUi ui;
   private ManualSqlStatement statementDetails;

   public ManualSqlStatementPage(Wizard var1, Source var2, DatabaseLocation var3) {
      super(var1, "Source Setup: Manual SQL Statement");
      this.dbLocation = var3;
      this.sourceId = var2.getID();
      this.ui = new ManualSqlStatementPageUi(var2, var3, this.getInfoProvider(var3), new ManualSqlStatementPage.TestCallback());
      this.ui.addChangeListener(new ManualSqlStatementPage.NextPageEnabler());
   }

   private DatabaseInfoProvider getInfoProvider(DatabaseLocation var1) {
      boolean var2 = var1.getDriver() instanceof JdbcDriverDescriptor;
      Class<? extends DatabaseInfoProvider> var3 = var2 ? JdbcInfoProvider.class : OdbcInfoProvider.class;
      IntegrationServer var4 = IntegrationServer.getInstance();
      return var4.getServerCall(var3);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   public WizardPage getNextPage() {
      return null;
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      if (this.statementDetails == null) {
         throw new RuntimeException("The SQL statement has not been tested. This is a bug.");
      }

      HashMap var2 = Maps.newHashMap();
      var2.put(this.statementDetails, 1);
      var1.setDatabaseObjectsUsage(var2);
      SqlScript var3 = new SqlScript();
      var3.setManualSqlStatement(this.statementDetails.getName());
      var1.setSqlScript(var3);
      var1.setDbTables(new DbTables());
      var1.setDbFields(new DbFields());
      var1.setHierarchicalFilters(new DbTranFilters());
      var1.setRelations(new DbTranRelations());
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      String var2 = var1.getStructure().getManualSqlStatement();
      this.ui.setSql(var2);
   }

   @Override
   public boolean prepareForForwardNavigation() {
      if (this.statementDetails == null) {
         this.statementDetails = this.downloadStatementDetails();
      }

      return this.statementDetails != null;
   }

   private ManualSqlStatement downloadStatementDetails() {
      String var1 = this.getSqlStatement();
      if (var1.isEmpty()) {
         return null;
      }

      ConnectionParams var2 = this.dbLocation.getConnectionParams();
      GetManualSqlStatementDetailsJob var3 = new GetManualSqlStatementDetailsJob(var1, var2, this.sourceId);
      var3.submit(this.getWaitService(), 0);
      return var3.getDetails();
   }

   public String getSqlStatement() {
      return this.ui.getSql();
   }

   public void setSqlStatement(String var1) {
      this.ui.setSql(var1);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("", "Enter the SQL statement that will be used as source in this Transformation");
      var1.setInsets(0, 0, 0, 0);
      var1.add(this.ui, true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      this.getUndoRedoInstaller().installIntoComponentTree(var2);
      return var2;
   }

   private final class NextPageEnabler extends DocumentContentChangeListener {
      private String previousSql = "";

      private NextPageEnabler() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         boolean var2 = !ManualSqlStatementPage.this.getSqlStatement().isEmpty();
         ManualSqlStatementPage.this.setCanContinue(var2);
         ManualSqlStatementPage.this.ui.setTestActionEnabled(var2);
         String var3 = ManualSqlStatementPage.this.getSqlStatement();
         if (!var3.equals(this.previousSql)) {
            ManualSqlStatementPage.this.statementDetails = null;
         }

         this.previousSql = var3;
      }
   }

   private final class TestCallback implements Receiver<ManualSqlStatement> {
      private TestCallback() {
      }

      public void handle(ManualSqlStatement var1) {
         ManualSqlStatementPage.this.statementDetails = var1;
      }
   }
}
