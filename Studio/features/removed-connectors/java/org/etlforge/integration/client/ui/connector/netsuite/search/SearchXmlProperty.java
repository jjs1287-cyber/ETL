package org.etlforge.integration.client.ui.connector.netsuite.search;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryApi;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.function.ConnectorCallPage;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteCustomFieldsQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteSearchObjectSavedSearchLookup;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.PageSizeSelectorAndTestButtonUi;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.QueryBuilderModel;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.QueryBuilderUi;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.SavedSearchUi;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.TestQueryController;
import org.etlforge.integration.client.ui.interchange.component.page.entity.AbstractEntityPageSaveJob;
import org.etlforge.integration.data.connector.NetSuiteQuery;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class SearchXmlProperty implements FunctionProperty {
   private final TestNetSuiteQueryApi testQueryApi;
   private final SearchXmlProperty.EditAction editAction = new SearchXmlProperty.EditAction();
   private ConnectorCallPage page;
   private NetSuiteQuery.QueryParams currentParams;
   private final NetSuiteQueryHelper helper;

   public SearchXmlProperty(
      NetSuiteQuery var1,
      NetSuiteDefinitionQueryBuilderLookup var2,
      NetSuiteCustomFieldsQueryBuilderLookup var3,
      NetSuiteSearchObjectSavedSearchLookup var4,
      TestNetSuiteQueryApi var5
   ) {
      this.helper = new NetSuiteQueryHelper(var1, var2, var4, var3);
      this.testQueryApi = Preconditions.checkNotNull(var5);
      this.currentParams = this.helper.getQueryParams();
   }

   @Override
   public void associateWithPage(ConnectorCallPage var1) {
      this.page = Preconditions.checkNotNull(var1);
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      KongaLabel var2 = KongaLabel.withMnemonic("&Query");
      KongaButton var3 = new KongaButton(this.editAction);
      var2.setLabelForButton(var3);
      var1.addRow(var2, InvisiblePanel.of(var3));
   }

   private void applyNewXml(QueryBuilderModel var1) {
      NetSuiteQuery.QueryParams var2 = var1.getDefinedParams();
      if (!var2.equals(this.currentParams)) {
         SearchXmlProperty.ApplyNewXmlJob var3 = new SearchXmlProperty.ApplyNewXmlJob(this.currentParams, var2);
         this.page.addSaveJob(var3);
         this.currentParams = var2;
      }
   }

   private TestQueryController createController(QueryBuilderModel var1) {
      TestQueryController var2 = new TestQueryController(var1, this.testQueryApi);
      var2.setEndpointAndObject(this.helper.getNetSuiteQuery().getEndpoint(), new SimpleConnectorObject(this.helper.getNetSuiteQuery().getObjectName()));
      return var2;
   }

   private void handleImpl(Window var1, TestQueryController var2, UiProvider var3, String var4, QueryBuilderModel var5) {
      WizardStyleDialog var6 = this.createDialog(var1, var3, var4);
      var2.setWaitService(var6);
      var6.setVisible(true);
      if (var6.wasOkPressed()) {
         this.applyNewXml(var5);
      }

      var2.cancelRunningJobs();
   }

   private WizardStyleDialog createDialog(Window var1, UiProvider var2, String var3) {
      WizardStyleDialog var4 = new WizardStyleDialog(var1, var3, var2, KongaDialog.OK_CANCEL, true);
      var4.setOkButtonAsDefault();
      var4.manageLocation(SearchXmlProperty.class);
      return var4;
   }

   private class ApplyNewXmlJob extends AbstractEntityPageSaveJob<NetSuiteQuery.QueryParams> {
      public ApplyNewXmlJob(NetSuiteQuery.QueryParams var2, NetSuiteQuery.QueryParams var3) {
         super("edit-query", var2, var3);
         this.setDescription("Query XML Change");
         this.setUndoRedoStateReceiver(new Receiver<NetSuiteQuery.QueryParams>() {
            public void handle(NetSuiteQuery.QueryParams var1) {
               SearchXmlProperty.this.currentParams = var1;
            }
         });
      }

      protected void apply(NetSuiteQuery.QueryParams var1) {
         SearchXmlProperty.this.helper.setQueryParams(var1);
      }
   }

   private class EditAction extends AbstractAction {
      public EditAction() {
         super("Edit...");
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditAction.this.editQuery(var1);
            }
         });
      }

      private void editQuery(ActionEvent var1) {
         boolean var2 = SearchXmlProperty.this.currentParams.getAdvancedSearchProperty().getSearchType().isSavedSearch();
         SearchXmlProperty.ModelProducer var3 = new SearchXmlProperty.ModelProducer(SearchXmlProperty.this.helper);
         Window var4 = Objects.firstNonNull(UiUtils.getWindowAncestor(var1).orNull(), ApplicationUi.getAppWinFrame());
         Receiver var5 = var2 ? SearchXmlProperty.this.new SavedQueryBuilderLauncher(var4) : SearchXmlProperty.this.new QueryBuilderLauncher(var4);
         Computation.create(var3, var5)
            .swingWorkerBuilder()
            .withErrorLog(ErrorLevel.ATTENTION, SearchXmlProperty.class)
            .withWaitService(SearchXmlProperty.this.page.getWaitService())
            .buildAndExecute();
      }
   }

   private static class ModelProducer implements Callable<QueryBuilderModel> {
      private final NetSuiteQueryHelper helper;

      public ModelProducer(NetSuiteQueryHelper var1) {
         this.helper = var1;
      }

      public QueryBuilderModel call() throws Exception {
         return this.helper.getQueryBuilderModel();
      }
   }

   private class QueryBuilderLauncher implements Receiver<QueryBuilderModel> {
      private final Window dialogOwner;

      public QueryBuilderLauncher(Window var2) {
         this.dialogOwner = var2;
      }

      public void handle(QueryBuilderModel var1) {
         TestQueryController var2 = SearchXmlProperty.this.createController(var1);
         PageSizeSelectorAndTestButtonUi var3 = new PageSizeSelectorAndTestButtonUi(var1, var2.getTestAction());
         QueryBuilderUi var4 = new QueryBuilderUi(var1, var3);
         SearchXmlProperty.this.handleImpl(this.dialogOwner, var2, var4, "Edit NetSuite Query", var1);
      }
   }

   private class SavedQueryBuilderLauncher implements Receiver<QueryBuilderModel> {
      private final Window dialogOwner;

      public SavedQueryBuilderLauncher(Window var2) {
         this.dialogOwner = var2;
      }

      public void handle(QueryBuilderModel var1) {
         TestQueryController var2 = SearchXmlProperty.this.createController(var1);
         PageSizeSelectorAndTestButtonUi var3 = new PageSizeSelectorAndTestButtonUi(var1, var2.getTestAction());
         SavedSearchUi var4 = new SavedSearchUi(var1.getSaveSearchModel(), var3);
         SearchXmlProperty.this.handleImpl(this.dialogOwner, var2, var4, "Edit NetSuite Query", var1);
      }
   }
}
