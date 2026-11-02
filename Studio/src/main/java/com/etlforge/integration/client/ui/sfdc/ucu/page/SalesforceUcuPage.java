package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.DateOptionsEditor;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPage;
import com.etlforge.integration.client.ui.sfdc.page.ChunkingSettingsEditor;
import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.system.callback.Receiver;

public final class SalesforceUcuPage extends AbstractSalesforceItemPage<AbstractSalesforceUCU> {
   private Optional<Action> viewHistoryAction = Optional.absent();
   private ChunkingSettingsEditor chunkingEditor;
   private ExternalIdFieldSelector externalIdSelector;
   private final DateOptionsEditor dateOptionsEditor;

   public SalesforceUcuPage(AbstractSalesforceUCU var1) {
      super(var1, var1.getMethod());
      this.dateOptionsEditor = new DateOptionsEditor(var1.getDateConversionParams());
   }

   public void setExternalIdSelector(ExternalIdFieldSelector var1) {
      this.externalIdSelector = var1;
   }

   public void setViewHistoryAction(Action var1) {
      this.viewHistoryAction = Optional.of(var1);
   }

   @Override
   protected void handleEntityPersistor(EntityPersistor var1, Receiver<EntityPageSaveJob> var2) {
      this.chunkingEditor = new ChunkingSettingsEditor(this.lookupOperation(), var1, var2);
   }

   protected SalesforceUcuPanel createDataPanel() {
      SalesforceUcuPanel var1 = new SalesforceUcuPanel(this.getWindow(), this.getObject(), this.getEntityExplorerSupport());
      this.doDefaultPanelConfiguration(var1);
      if (this.viewHistoryAction.isPresent()) {
         var1.addAdditionalAction(this.viewHistoryAction.get());
      }

      var1.setDateOptionsEditor(this.dateOptionsEditor);
      var1.setChunkingEditor(this.chunkingEditor);
      var1.setExternalIdSelector(this.externalIdSelector);
      return var1;
   }

   Optional<DateConversionParams> getDateConversionParams() {
      return this.dateOptionsEditor.getParams();
   }
}
