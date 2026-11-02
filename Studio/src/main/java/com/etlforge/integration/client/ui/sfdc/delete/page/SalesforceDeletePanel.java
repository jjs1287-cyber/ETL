package com.etlforge.integration.client.ui.sfdc.delete.page;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPanel;
import com.etlforge.integration.client.ui.sfdc.page.ChunkingSettingsEditor;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanel;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import com.etlforge.integration.data.entity.SalesforceDelete;
import java.util.ArrayList;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.sfdc.WizardItemInfo;

public final class SalesforceDeletePanel extends AbstractSalesforceItemPanel<SalesforceDelete> {
   private final DeleteOperationOptions operationOptions;
   private final SourceFileOptions sourceFileOptions;

   public SalesforceDeletePanel(ApplicationWindow var1, SalesforceDelete var2, EntityExplorerSupport var3) {
      super(var1, var2, var3);
      this.operationOptions = new DeleteOperationOptions(var2, this);
      this.sourceFileOptions = this.createFileOptions(var2);
   }

   private SourceFileOptions createFileOptions(SalesforceDelete var1) {
      WizardItemInfo var2 = new WizardItemInfo(var1);
      return var2.readsFromFile() ? new SourceFileOptions(var1, this, this.getUTF8Choice()) : null;
   }

   @Override
   public void setSoapHeaderEditor(SoapHeaderEditor var1) {
      this.operationOptions.setSoapHeaderEditor(var1);
   }

   public void setChunkingEditor(ChunkingSettingsEditor var1) {
      this.operationOptions.setChunkingEditor(var1);
   }

   @Override
   public void setRetryPolicyEditor(RetryPolicyEditor var1) {
      this.operationOptions.setRetryPolicyEditor(var1);
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      String var1 = this.getFileEncoding();
      this.getDisplayedObject().setFileEncodingName(var1);
      this.applyOptions();
   }

   private void applyOptions() {
      this.operationOptions.apply();
      if (this.sourceFileOptions != null) {
         this.sourceFileOptions.apply();
      }
   }

   @Override
   protected void layoutComponents(AbstractSalesforceItemPanel<SalesforceDelete>.Layout var1) {
      var1.layoutLinks();
      var1.layoutScheduleSelector();
      var1.layoutActions();
      OptionsPanel var2 = this.createOptionsPanel();
      var1.layoutOptionsPanel(var2);
   }

   private OptionsPanel createOptionsPanel() {
      ArrayList var1 = Lists.newArrayList();
      if (this.sourceFileOptions != null) {
         var1.add(this.sourceFileOptions);
      }

      var1.add(this.operationOptions);
      return new OptionsPanel(var1);
   }
}
