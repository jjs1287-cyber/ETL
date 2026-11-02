package com.etlforge.integration.client.ui.sfdc.ucu.page.v4;

import com.etlforge.integration.client.ui.sfdc.DefaultRetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.page.ChunkingOptionsAction;
import com.etlforge.integration.client.ui.sfdc.page.ChunkingSettingsEditor;
import com.etlforge.integration.client.ui.sfdc.page.ImplementingObjectsLinkPanel;
import com.etlforge.integration.client.ui.sfdc.page.v4.AbstractSalesforceItemPanelV4;
import com.etlforge.integration.client.ui.sfdc.ucu.RequestHeaderUi;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.SalesforceUpsert;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.entity.sfdc.UCURequestHeader;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.ui.layout.DialogLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.xml.ExpandedName;

public final class SalesforceUcuPanelV4 extends AbstractSalesforceItemPanelV4<AbstractSalesforceUCU> {
   private DefaultRetryPolicyEditor retryPolicyEditor;
   private final ChunkingOptionsAction chunkingOptionsAction = new ChunkingOptionsAction();
   private final ExternalIdFieldNameUi externalIdFieldSelector;
   private final RequestHeaderUi requestHeaderUi;

   public SalesforceUcuPanelV4(ApplicationWindow var1, AbstractSalesforceUCU var2, ImplementingObjectsLinkPanel var3) {
      super(var1, var2, var3);
      this.externalIdFieldSelector = this.createExternalIdSelector(var2);
      this.requestHeaderUi = this.createRequestHeaderUi(var2);
   }

   private ExternalIdFieldNameUi createExternalIdSelector(AbstractSalesforceUCU var1) {
      if (var1.hasExternalIdField()) {
         ExternalIdFieldNameUi var2 = new ExternalIdFieldNameUi(this, false);
         var2.setEnabled(false);
         return var2;
      } else {
         return null;
      }
   }

   private RequestHeaderUi createRequestHeaderUi(AbstractSalesforceUCU var1) {
      RequestHeaderUi var2 = new RequestHeaderUi(this);
      var2.setBorder(StyleSheet.getDefaultTitleBorder("Options"));
      var2.setRequestHeader(var1.getRequestHeader());
      return var2;
   }

   public void setRetryPolicyEditor(DefaultRetryPolicyEditor var1) {
      this.retryPolicyEditor = var1;
   }

   @Override
   public void display() {
      super.display();
      this.doLayout();
   }

   private void doLayout() {
      OneColumnPanel var1 = this.startDefaultLayout();
      if (this.externalIdFieldSelector != null) {
         JPanel var2 = InvisiblePanel.of(this.externalIdFieldSelector);
         var2.setBorder(StyleSheet.getDefaultTitleBorder("External ID Field"));
         var1.add(var2);
      }

      var1.add(this.requestHeaderUi);
      Grid var4 = Grid.oneRow(10);
      if (this.getCredentialsEditor() != null) {
         var4.add(this.getCredentialsEditor());
      }

      var4.add(this.chunkingOptionsAction);
      if (this.retryPolicyEditor != null) {
         DialogLayoutBuilder var3 = new DialogLayoutBuilder(5, 0);
         this.retryPolicyEditor.layoutIn(var3);
         var3.container().setOpaque(false);
         var4.add(var3.container());
      }

      var1.addAll(var4.nonOpaque(), this.getLinksLayout());
      this.getDefaultLayout().add(var1.nonOpaque().invisible(), true);
   }

   public void setChunkingEditor(ChunkingSettingsEditor var1) {
      this.chunkingOptionsAction.setEditor(var1);
   }

   public void setWsdlInfo(final SalesforceWsdlInfo var1) {
      if (this.getDisplayedObject().hasExternalIdField()) {
         this.updateSilently(new Runnable() {
            @Override
            public void run() {
               SalesforceUpsert var1x = (SalesforceUpsert)SalesforceUcuPanelV4.this.getDisplayedObject();
               ExpandedName var2 = var1x.getSalesforceObjectName();
               SObject var3 = var1.lookupObject(var2);
               SalesforceUcuPanelV4.this.externalIdFieldSelector.setObject(var3);
               if (var3 != null) {
                  SalesforceUcuPanelV4.this.externalIdFieldSelector.setEnabled(true);
                  SalesforceUcuPanelV4.this.externalIdFieldSelector.setSelectedFieldName(var1x.getExternalIdFieldName());
               } else {
                  SalesforceUcuPanelV4.this.externalIdFieldSelector.setEnabled(false);
               }
            }
         });
      }
   }

   @Override
   protected void validateOtherThingsThanLinks() throws IntegrationDataPanelException {
      if (this.externalIdFieldSelector != null && this.externalIdFieldSelector.isEnabled() && !this.externalIdFieldSelector.isInputValid()) {
         throw new IntegrationDataPanelException("Must select an external ID field.");
      }
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      AbstractSalesforceUCU var1 = this.getDisplayedObject();
      if (var1 instanceof SalesforceUpsert && this.externalIdFieldSelector.isEnabled()) {
         ((SalesforceUpsert)var1).setExternalIdFieldName(this.externalIdFieldSelector.getSelectedFieldName());
      }

      UCURequestHeader var2 = this.requestHeaderUi.getDefinedHeader();
      var1.setRequestHeader(var2);
   }

   public AbstractSalesforceUCU getDisplayedObject() {
      return (AbstractSalesforceUCU)super.getDisplayedObject();
   }
}
