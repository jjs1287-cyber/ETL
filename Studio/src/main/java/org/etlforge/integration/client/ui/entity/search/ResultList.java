package org.etlforge.integration.client.ui.entity.search;

import java.awt.EventQueue;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.entity.EntityPathPublisher;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.ListUtils;

final class ResultList implements ResultDisplayer {
   private final KongaList list = this.createList();

   public ResultList(EntitySearchModel var1) {
      ResultList.ResultBinding var2 = new ResultList.ResultBinding(var1);
      var2.syncUi();
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(15);
      var1.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setCellRenderer(RendererUtils.getIntegrationEntityListCellRenderer());
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      KongaList var2 = var1.newList();
      var2.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            IntegrationEntity var2x = ResultList.this.getSelectedEntity();
            ResultList.publishPath(ResultList.this, var2x);
         }
      });
      return var2;
   }

   private static void publishPath(Object var0, IntegrationEntity var1) {
      EntityPathPublisher var2 = new EntityPathPublisher(var0);
      var2.setIncludeProject(true);
      var2.publish(var1);
   }

   @Override
   public JComponent getDisplayer() {
      return BorderLayoutBuilder.fillWith(this.list);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   @Override
   public IntegrationEntity getSelectedEntity() {
      Object var1 = this.list.getSelection().head();
      return var1 instanceof IntegrationEntity ? (IntegrationEntity)var1 : null;
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   private class ResultBinding extends AbstractBinding<List<IntegrationEntity>> {
      private final EntitySearchModel model;

      public ResultBinding(EntitySearchModel var2) {
         super(var2, "matches");
         this.model = var2;
      }

      protected void updateUiWithValue(List<IntegrationEntity> var1) {
         DefaultListModel var2;
         if (var1.isEmpty() && this.model.hasSearchString()) {
            var2 = ListUtils.createModel(EntityResources.getString("Search.NoMatch"));
         } else {
            var2 = ListUtils.createModel(var1);
         }

         this.setModel(var2);
      }

      private void setModel(ListModel var1) {
         assert EventQueue.isDispatchThread() : "setModel must be called from the EDT";
         ResultList.this.list.setModel(var1);
         ResultList.this.list.selectFirst();
      }
   }
}
