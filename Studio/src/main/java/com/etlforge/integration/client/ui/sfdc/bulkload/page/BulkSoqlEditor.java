package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.SoqlEditorFactory;
import com.etlforge.integration.client.ui.sfdc.page.soql.AutomaticPageSaver;
import com.etlforge.integration.client.ui.sfdc.page.soql.EditSoqlAction;
import com.etlforge.integration.client.ui.sfdc.page.soql.EditSoqlController;
import com.etlforge.integration.client.ui.sfdc.page.soql.ErrorAlert;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlEditor;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlViewerBinding;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTesterFactory;
import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlParser;
import org.etlforge.integration.salesforce.parser.SoqlParserFactory;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.salesforce.query.QueryRunner;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class BulkSoqlEditor implements Disposable {
   private final SoqlModel model;
   private final SoqlEditor editor;
   private final EditSoqlController controller;
   private final KongaTextField soqlViewer;

   public BulkSoqlEditor(SalesforceBulkLoad var1, FacadeFactory var2) {
      Preconditions.checkArgument(var1.isQuery(), "Bulk Query expected");
      this.model = this.createModel(var1);
      this.editor = SoqlEditorFactory.newEditor(var2.newFacade());
      this.controller = new EditSoqlController(this.model, this.editor, var2.newFacade());
      this.soqlViewer = this.createSoqlViewer();
   }

   private SoqlModel createModel(SalesforceBulkLoad var1) {
      String var2 = var1.getQueryString();
      SoqlParser var3 = SoqlParserFactory.newParser();
      ParsedQuery var4 = var3.parse(var2);
      return new SoqlModel(var1.getObjectName(), var2, var4);
   }

   private KongaTextField createSoqlViewer() {
      KongaTextField var1 = new KongaTextField(this.model.getQueryString(), 20);
      var1.setCaretPosition(0);
      var1.setEditable(false);
      SoqlViewerBinding.installAndSyncUi(this.model, var1);
      return var1;
   }

   @Override
   public void dispose() {
      this.editor.dispose();
   }

   public void setSupportedQueryPolicy(SupportedQueryPolicy var1) {
      this.model.setSupportedQueryPolicy(var1);
   }

   public JComponent[] getLayoutComponents() {
      KongaLabel var1 = KongaLabel.withMnemonic("&Query string");
      var1.setLabelFor(this.soqlViewer);
      EditSoqlAction var2 = new EditSoqlAction(this.controller);
      return new JComponent[]{var1, this.soqlViewer, new KongaButton(var2)};
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(new TextInputField("Query String", this.soqlViewer));
   }

   public void associateWithPage(SalesforceBulkLoadPage var1) {
      ErrorAlert var2 = new ErrorAlert(var1.getMainPanel());
      this.controller.withConnectorSupplier(var1.getConnectorSupplier()).withWaitService(var1.getWaitService()).withErrorReporter(var2);
      this.installQueryTester(var1);
      AutomaticPageSaver var3 = AutomaticPageSaver.install(this.model, var1);
      var3.setErrorReporter(var2);
   }

   private void installQueryTester(SalesforceBulkLoadPage var1) {
      SalesforceFacade var2 = this.editor.getSfdcFacade();
      QueryRunner var3 = new QueryRunner(var2.getConnectionFactory(), var1.getOrgIdSupplier());
      QueryTesterFactory var4 = new QueryTesterFactory(ApplicationUi.getAppWinFrame());
      QueryTester var5 = var4.newTester(var3, this.model.getSupportedQueryPolicy());
      this.editor.setQueryTester(var5);
   }

   public String getQueryString() {
      return this.model.getQueryString();
   }

   public String getObjectName() {
      return this.model.getObjectName();
   }
}
