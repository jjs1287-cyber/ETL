package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import com.etlforge.integration.client.sfdc.data.SoqlObject;
import com.etlforge.integration.client.ui.sfdc.DefaultRetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.page.ImplementingObjectsLinkPanel;
import com.etlforge.integration.client.ui.sfdc.page.v4.AbstractSalesforceItemPanelV4;
import com.etlforge.integration.client.ui.sfdc.soql.v4.QueryValidator;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.xml.ExpandedName;

public final class SalesforceQueryPanelV4 extends AbstractSalesforceItemPanelV4<SalesforceQuery> {
   private final QueryArea queryArea;
   private BatchSizeEditor batchSizeEditor;
   private DefaultRetryPolicyEditor retryPolicyEditor;

   public SalesforceQueryPanelV4(ApplicationWindow var1, SalesforceQuery var2, ImplementingObjectsLinkPanel var3) {
      super(var1, var2, var3);
      this.queryArea = new QueryArea(this, var2.getQueryString());
   }

   public void setBatchSizeEditor(BatchSizeEditor var1) {
      this.batchSizeEditor = var1;
      var1.registerWithInputPanel(this);
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
      var1.addAll(this.layoutQueryAndOptionsArea(), this.getLinksLayout());
      this.getDefaultLayout().add(var1.nonOpaque().invisible(), true);
   }

   private JComponent layoutQueryAndOptionsArea() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      var1.center(this.queryArea);
      BoxBuilder var2 = Horizontal.box();
      var2.addAllWithSpace(12, this.batchSizeEditor, this.getCredentialsEditor());
      if (this.retryPolicyEditor != null) {
         TwoColumnPanel var3 = new TwoColumnPanel();
         this.retryPolicyEditor.layoutIn(var3);
         var2.rigid(12).add(var3);
      }

      var1.south(InvisiblePanel.of(var2));
      var1.withBorder(StyleSheet.getDefaultTitleBorder("Query & Options"));
      return var1.deeplyNonOpaque().container();
   }

   public void setQueryValidator(QueryValidator var1) {
      this.queryArea.setValidator(var1);
      if (var1 instanceof WsdlBasedQueryValidator) {
         SObject var2 = ((WsdlBasedQueryValidator)var1).getFixedObject();
         this.queryArea.setSObject(var2);
      }
   }

   public void setCompletionContext(CompletionContext var1) {
   }

   @Override
   protected void validateOtherThingsThanLinks() throws IntegrationDataPanelException {
      try {
         this.queryArea.validate();
         if (this.batchSizeEditor != null) {
            this.batchSizeEditor.validate();
         }
      } catch (InputFieldException var2) {
         throw new IntegrationDataPanelException(var2.getMessage());
      }
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      SalesforceQuery var1 = this.getDisplayedObject();
      ExpandedName var2 = var1.getQueryObject().getObjectName();
      SoqlObject var3 = new SoqlObject(this.getQueryString(), var2, false);
      var1.setQueryObject(var3);
      if (this.batchSizeEditor != null) {
         this.batchSizeEditor.apply();
      }
   }

   public SalesforceQuery getDisplayedObject() {
      return (SalesforceQuery)super.getDisplayedObject();
   }

   @Override
   public void disposeAdditionalResources() {
      this.closeFieldSelector();
   }

   public void closeFieldSelector() {
      this.queryArea.closeFieldSelector();
   }

   public void deactive() {
      this.closeFieldSelector();
   }

   public String getQueryString() {
      return this.queryArea.getQueryString();
   }

   public int getBatchSize() {
      return this.batchSizeEditor != null ? this.batchSizeEditor.getBatchSize() : 2000;
   }
}
