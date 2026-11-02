package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import com.etlforge.integration.client.sfdc.data.SoqlObject;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPanel;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanel;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import com.etlforge.integration.client.ui.sfdc.page.soql.EditSoqlAction;
import com.etlforge.integration.client.ui.sfdc.page.soql.EditSoqlController;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlEditor;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlViewerBinding;
import java.util.ArrayList;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.sfdc.WizardItemInfo;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.xml.ExpandedName;

final class SalesforceQueryPanel extends AbstractSalesforceItemPanel<SalesforceQuery> {
   private SoqlEditor soqlEditor;
   private EditSoqlController editSoqlController;
   private final KongaTextField soqlViewer;
   private final QueryOperationOptions operationOptions;
   private final TargetFileOptions targetFileOptions;
   private final SoqlModel soqlModel;

   public SalesforceQueryPanel(ApplicationWindow var1, SalesforceQuery var2, SoqlModel var3, EntityExplorerSupport var4) {
      super(var1, var2, var4);
      this.soqlModel = var3;
      this.soqlViewer = this.createSoqlViewer();
      this.operationOptions = new QueryOperationOptions(var2, this);
      this.targetFileOptions = new TargetFileOptions(var2, this.getUTF8Choice(), this);
      this.bindToSoqlModel();
      this.addInputField(new TextInputField("Query String", this.soqlViewer));
   }

   private KongaTextField createSoqlViewer() {
      KongaTextField var1 = new KongaTextField(20);
      var1.setCaretPosition(0);
      var1.setEditable(false);
      return var1;
   }

   private void bindToSoqlModel() {
      SoqlViewerBinding.installAndSyncUi(this.soqlModel, this.soqlViewer);
      this.targetFileOptions.bindToSoqlModel(this.soqlModel);
   }

   public void setSoqlEditor(SoqlEditor var1, EditSoqlController var2) {
      this.soqlEditor = var1;
      this.editSoqlController = var2;
   }

   @Override
   public void setSoapHeaderEditor(SoapHeaderEditor var1) {
      this.operationOptions.setSoapHeaderEditor(var1);
   }

   @Override
   public void setRetryPolicyEditor(RetryPolicyEditor var1) {
      this.operationOptions.setRetryPolicyEditor(var1);
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      this.applyTargetFileOptions();
      this.applyQueryString();
   }

   private void applyTargetFileOptions() {
      this.applyEncoding();
      this.targetFileOptions.apply();
   }

   private void applyEncoding() {
      String var1 = this.getFileEncoding();
      this.getDisplayedObject().setFileEncodingName(var1);
   }

   private void applyQueryString() {
      SalesforceQuery var1 = this.getDisplayedObject();
      String var2 = this.soqlModel.getQueryString();
      ExpandedName var3 = var1.getObject();
      SoqlObject var4 = new SoqlObject(var2, var3, this.isQueryAll());
      var1.setQueryObject(var4);
   }

   @Override
   protected void layoutComponents(AbstractSalesforceItemPanel<SalesforceQuery>.Layout var1) {
      this.layoutSoqlEditor(var1);
      var1.layoutLinks();
      var1.layoutScheduleSelector();
      var1.layoutActions();
      OptionsPanel var2 = this.createOptionsPanel();
      var1.layoutOptionsPanel(var2);
   }

   private OptionsPanel createOptionsPanel() {
      ArrayList var1 = Lists.newArrayList();
      SalesforceQuery var2 = this.getDisplayedObject();
      WizardItemInfo var3 = new WizardItemInfo(var2);
      if (var3.writesToFile()) {
         var1.add(this.targetFileOptions);
      }

      var1.add(this.operationOptions);
      return new OptionsPanel(var1);
   }

   private void layoutSoqlEditor(AbstractSalesforceItemPanel<SalesforceQuery>.Layout var1) {
      if (this.soqlEditor != null) {
         KongaButton var2 = new KongaButton(new EditSoqlAction(this.editSoqlController));
         KongaLabel var3 = KongaLabel.withMnemonic("Salesforce &Query:");
         var3.setIcon(EntityIcons.forType(EntityType.SalesforceQuery).getDefaultIcon(EntityIcons.Size.SMALL));
         var3.setLabelFor(this.soqlViewer);
         var1.addRow(var3, this.soqlViewer, var2);
      }
   }

   public boolean isQueryAll() {
      return this.operationOptions.isQueryAll();
   }

   public Iterable<BoundProperty> getBoundProperties() {
      return new Builder<BoundProperty>().addAll(this.operationOptions.getBoundProperties()).addAll(this.targetFileOptions.getBoundProperties()).build();
   }
}
