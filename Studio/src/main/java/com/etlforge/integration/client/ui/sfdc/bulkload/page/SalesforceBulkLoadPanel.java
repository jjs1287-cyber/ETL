package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.DateOptionsEditor;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPanel;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanel;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class SalesforceBulkLoadPanel extends AbstractSalesforceItemPanel<SalesforceBulkLoad> {
   private final MappingsEditor mappingsEditor;
   private final BulkSoqlEditor soqlEditor;
   private final OptionsPanel optionsPanel;

   public SalesforceBulkLoadPanel(
      ApplicationWindow var1,
      SalesforceBulkLoad var2,
      EntityExplorerSupport var3,
      MappingsEditor var4,
      BulkSoqlEditor var5,
      BatchParamsEditor var6,
      DateOptionsEditor var7
   ) {
      super(var1, var2, var3);
      this.mappingsEditor = var4;
      if (var4 != null) {
         var4.setConnectorSupplier(this.getConnectorSupplier());
      }

      this.soqlEditor = var5;
      if (var5 != null) {
         var5.registerAsChangeSource(this);
      }

      this.optionsPanel = this.createOptionsPanel(var2, var6, var7);
   }

   private OptionsPanel createOptionsPanel(SalesforceBulkLoad var1, BatchParamsEditor var2, DateOptionsEditor var3) {
      ArrayList var4 = Lists.newArrayList();
      if (var1.isLoad()) {
         SourceFileOptions var5 = new SourceFileOptions(var1, this, this.getUTF8Choice(), var3);
         var4.add(var5);
         BulkLoadOperationOptions var6 = new BulkLoadOperationOptions(var1, this, var2);
         var4.add(var6);
      } else {
         TargetFileOptions var7 = new TargetFileOptions(this.getUTF8Choice());
         var4.add(var7);
      }

      return new OptionsPanel(var4);
   }

   public void setShowHistoryAction(Action var1) {
      if (var1 != null) {
         this.addAdditionalAction(var1);
      }
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      this.applyFileEncoding();
      this.applyQueryString();
      this.optionsPanel.apply();
   }

   private void applyFileEncoding() {
      String var1 = this.getFileEncoding();
      this.getDisplayedObject().setFileEncodingName(var1);
   }

   private void applyQueryString() {
      if (this.soqlEditor != null) {
         SalesforceBulkLoad var1 = this.getDisplayedObject();
         String var2 = this.soqlEditor.getQueryString();
         var1.setQueryString(var2);
         String var3 = this.soqlEditor.getObjectName();
         var1.setObjectName(var3);
      }
   }

   @Override
   public void setSoapHeaderEditor(SoapHeaderEditor var1) {
   }

   @Override
   public void setRetryPolicyEditor(RetryPolicyEditor var1) {
   }

   @Override
   protected void layoutComponents(AbstractSalesforceItemPanel<SalesforceBulkLoad>.Layout var1) {
      this.layoutActionAndObjectInfo(var1);
      this.layoutMappingsEditor(var1);
      this.layoutSoqlEditor(var1);
      var1.layoutLinks();
      var1.layoutScheduleSelector();
      var1.layoutActions();
      var1.layoutOptionsPanel(this.optionsPanel);
   }

   private void layoutActionAndObjectInfo(AbstractSalesforceItemPanel<SalesforceBulkLoad>.Layout var1) {
      SalesforceBulkLoad var2 = this.getDisplayedObject();
      if (!var2.isQuery()) {
         ActionAndObjectInfo var3 = new ActionAndObjectInfo(var2);
         var3.layoutIn(var1);
      }
   }

   private void layoutMappingsEditor(AbstractSalesforceItemPanel<SalesforceBulkLoad>.Layout var1) {
      if (this.mappingsEditor != null) {
         KongaLabel var2 = KongaLabel.withMnemonic("&Mappings:");
         KongaButton var3 = new KongaButton(this.mappingsEditor.getLaunchAction());
         var2.setLabelForButton(var3);
         var1.addRow(var2, InvisiblePanel.of(var3), null);
      }
   }

   private void layoutSoqlEditor(AbstractSalesforceItemPanel<SalesforceBulkLoad>.Layout var1) {
      if (this.soqlEditor != null) {
         JComponent[] var2 = this.soqlEditor.getLayoutComponents();
         var1.addRow(var2);
      }
   }
}
