package org.etlforge.integration.client.ui.connector.mscrm.query;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import javax.swing.Action;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.function.ConnectorCallPage;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.AbstractEntityPageSaveJob;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.data.connector.MSCrmQuery;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.PopupMenuButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class QueryXmlProperty implements FunctionProperty {
   private final MSCrmQuery queryEntity;
   private String currentQueryXml;
   private ConnectorCallPage page;
   private final RefreshFunctionJobFactory refreshJobFactory;
   private final EditInQueryBuilderAction editInQueryBuilderAction;
   private final EditAsTextAction editAsTextAction;

   public QueryXmlProperty(MSCrmQuery var1, MSCrmObjectLookupFactory var2, RefreshFunctionJobFactory var3) {
      this.queryEntity = var1;
      this.refreshJobFactory = var3;
      Receiver var4 = new Receiver<String>() {
         public void handle(String var1) {
            QueryXmlProperty.this.applyNewQueryXml(var1);
         }
      };
      this.editInQueryBuilderAction = new EditInQueryBuilderAction(new QueryXmlProperty.ObjectLookupSupplier(var2), var4);
      this.editAsTextAction = new EditAsTextAction(var1.getObjectName(), var4);
      this.updateCurrentXml(var1.getQueryXml());
   }

   @Override
   public void associateWithPage(ConnectorCallPage var1) {
      this.page = Preconditions.checkNotNull(var1, "page");
      this.editAsTextAction.associateWithPage(var1);
      this.editInQueryBuilderAction.associateWithPage(var1);
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      KongaLabel var2 = KongaLabel.withMnemonic("&Query XML");
      PopupMenuButton var3 = new PopupMenuButton("Edit...", new Action[]{this.editInQueryBuilderAction, this.editAsTextAction});
      var2.setLabelForButton(var3);
      var1.addRow(var2, InvisiblePanel.of(var3));
   }

   private void applyNewQueryXml(String var1) {
      QueryXmlProperty.ApplyNewXmlJob var2 = new QueryXmlProperty.ApplyNewXmlJob(this.currentQueryXml, var1);
      this.page.addSaveJob(var2);
      this.updateCurrentXml(var1);
   }

   private void updateCurrentXml(String var1) {
      this.currentQueryXml = var1;
      this.editAsTextAction.setCurrentXml(var1);
      this.editInQueryBuilderAction.setCurrentXml(var1);
   }

   private class ApplyNewXmlJob extends AbstractEntityPageSaveJob<String> {
      public ApplyNewXmlJob(String var2, String var3) {
         super("edit-query", var2, var3);
         this.setDescription("Query XML Change");
         this.setUndoRedoStateReceiver(new Receiver<String>() {
            public void handle(String var1) {
               QueryXmlProperty.this.updateCurrentXml(var1);
            }
         });
      }

      protected void apply(String var1) {
         QueryXmlProperty.this.queryEntity.setQueryXml(var1);
         this.refreshStructures();
      }

      private void refreshStructures() {
         Runnable var1 = QueryXmlProperty.this.refreshJobFactory.createRefreshJob(QueryXmlProperty.this.queryEntity);
         var1.run();
      }
   }

   private class ObjectLookupSupplier implements Supplier<MSCrmObjectLookup> {
      private final MSCrmObjectLookupFactory factory;

      public ObjectLookupSupplier(MSCrmObjectLookupFactory var2) {
         this.factory = var2;
      }

      public MSCrmObjectLookup get() {
         MSCrmEndpoint var1 = (MSCrmEndpoint)QueryXmlProperty.this.page.getEndpointSupplier().get();
         return this.factory.newLookup(var1);
      }
   }
}
