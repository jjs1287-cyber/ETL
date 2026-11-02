package com.etlforge.integration.client.ui.sfdc.ucu.page.v4;

import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.ui.sfdc.DefaultRetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.SfdcWizardPageCategory;
import com.etlforge.integration.client.ui.sfdc.page.ChunkingSettingsEditor;
import com.etlforge.integration.client.ui.sfdc.page.v4.AbstractSalesforceItemPageV4;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.system.callback.Receiver;

public final class SalesforceUcuPageV4 extends AbstractSalesforceItemPageV4<AbstractSalesforceUCU> {
   private ChunkingSettingsEditor chunkingEditor;
   private final DefaultRetryPolicyEditor retryPolicyEditor;
   private SalesforceWsdlInfo wsdlInfo;

   public SalesforceUcuPageV4(AbstractSalesforceUCU var1) {
      super(var1, var1.getMethod());
      this.retryPolicyEditor = this.createRetryPolicyEditor(var1);
   }

   private DefaultRetryPolicyEditor createRetryPolicyEditor(AbstractSalesforceUCU var1) {
      DefaultRetryPolicyEditor var2 = new DefaultRetryPolicyEditor(var1);
      var2.addUndoableEditListener(new UndoableEditListener() {
         @Override
         public void undoableEditHappened(UndoableEditEvent var1) {
            SalesforceUcuPageV4.this.undoableEditHappened(var1);
            SalesforceUcuPageV4.this.setDirty();
         }
      });
      return var2;
   }

   @Override
   protected EditorCategory createCategory() {
      return SfdcWizardPageCategory.CATEGORY;
   }

   @Override
   protected void handleEntityPersistor(EntityPersistor var1, Receiver<EntityPageSaveJob> var2) {
      this.chunkingEditor = new ChunkingSettingsEditor(this.lookupOperation(), var1, var2);
      this.chunkingEditor.setOverlayContainer(this.getOverlayContainer());
   }

   public void setWsdlInfo(SalesforceWsdlInfo var1) {
      SalesforceUcuPanelV4 var2 = this.getDetailsPanel();
      if (var2 == null) {
         this.wsdlInfo = var1;
      } else {
         var2.setWsdlInfo(var1);
      }
   }

   protected SalesforceUcuPanelV4 getDetailsPanel() {
      return (SalesforceUcuPanelV4)super.getDetailsPanel();
   }

   protected SalesforceUcuPanelV4 createDataPanel() {
      SalesforceUcuPanelV4 var1 = new SalesforceUcuPanelV4(this.getWindow(), this.getObject(), this.getLinks());
      this.doDefaultPanelConfiguration(var1);
      var1.setChunkingEditor(this.chunkingEditor);
      var1.setRetryPolicyEditor(this.retryPolicyEditor);
      if (this.wsdlInfo != null) {
         var1.setWsdlInfo(this.wsdlInfo);
      }

      return var1;
   }

   RetryPolicy getRetryPolicy() {
      return this.retryPolicyEditor.getPolicy();
   }
}
