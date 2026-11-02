package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.DateOptionsEditor;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPanel;
import com.etlforge.integration.client.ui.sfdc.page.ChunkingSettingsEditor;
import com.etlforge.integration.client.ui.sfdc.page.OptionsPanel;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import java.util.ArrayList;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.entity.sfdc.WizardItemInfo;

public final class SalesforceUcuPanel extends AbstractSalesforceItemPanel<AbstractSalesforceUCU> {
   private ExternalIdFieldSelector externalIdSelector;
   private final UcuOperationOptions operationOptions;
   private final SourceFileOptions sourceFileOptions;
   private SourceDatabaseDateOptions sourceDbDateOptions;

   public SalesforceUcuPanel(ApplicationWindow var1, AbstractSalesforceUCU var2, EntityExplorerSupport var3) {
      super(var1, var2, var3);
      this.operationOptions = new UcuOperationOptions(var2, this);
      this.sourceFileOptions = this.createSourceFileOptions();
   }

   private SourceFileOptions createSourceFileOptions() {
      WizardItemInfo var1 = new WizardItemInfo(this.getDisplayedObject());
      return var1.readsFromFile() ? new SourceFileOptions(this.getDisplayedObject(), this, this.getUTF8Choice()) : null;
   }

   public void setExternalIdSelector(ExternalIdFieldSelector var1) {
      if (var1 != null) {
         var1.setConnectorSupplier(this.getConnectorSupplier());
         var1.registerAsChangeSource(this);
      }

      this.externalIdSelector = var1;
   }

   @Override
   protected void handleConnectorSelection(SalesforceConnector var1) {
      if (this.externalIdSelector != null) {
         this.externalIdSelector.apply();
      }
   }

   @Override
   public void setSoapHeaderEditor(SoapHeaderEditor var1) {
      this.operationOptions.setSoapHeaderEditor(var1);
   }

   public void setChunkingEditor(ChunkingSettingsEditor var1) {
      this.operationOptions.setChunkingEditor(var1);
   }

   public void setDateOptionsEditor(DateOptionsEditor var1) {
      WizardItemInfo var2 = new WizardItemInfo(this.getDisplayedObject());
      if (var2.readsFromDatabase()) {
         this.sourceDbDateOptions = new SourceDatabaseDateOptions(this, this.getDisplayedObject(), var1);
      } else if (var2.hasSourceData()) {
         this.sourceFileOptions.setDateOptionsEditor(var1);
      }
   }

   @Override
   public void setRetryPolicyEditor(RetryPolicyEditor var1) {
      this.operationOptions.setRetryPolicyEditor(var1);
   }

   @Override
   protected void layoutComponents(AbstractSalesforceItemPanel<AbstractSalesforceUCU>.Layout var1) {
      if (this.externalIdSelector != null) {
         var1.addRow(this.externalIdSelector.getLayoutComponents());
      }

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
      } else if (this.sourceDbDateOptions != null) {
         var1.add(this.sourceDbDateOptions);
      }

      var1.add(this.operationOptions);
      return new OptionsPanel(var1);
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      if (this.externalIdSelector != null) {
         this.externalIdSelector.apply();
      }

      String var1 = this.getFileEncoding();
      this.getDisplayedObject().setFileEncodingName(var1);
      this.applyOptions();
   }

   private void applyOptions() {
      this.operationOptions.apply();
      if (this.sourceDbDateOptions != null) {
         this.sourceDbDateOptions.apply();
      } else if (this.sourceFileOptions != null) {
         this.sourceFileOptions.apply();
      }
   }
}
