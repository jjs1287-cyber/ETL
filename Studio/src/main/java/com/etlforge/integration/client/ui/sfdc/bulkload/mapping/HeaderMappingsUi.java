package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.collections.Pair;
import org.etlforge.integration.salesforce.bulkload.HeaderMappings;
import org.etlforge.integration.salesforce.bulkload.Mapping;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.tree.ConnectedTrees;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.LeafDisplayNameFilterFunction;
import org.etlforge.ui.widget.tree.TreeUtils;

public final class HeaderMappingsUi implements UiProvider {
   private final HeaderMappingsModel model;
   private final HeaderMappingsController controller;
   private final ConnectedTrees widget;
   private final HeaderMappingsUi.ModelListener modelListener = new HeaderMappingsUi.ModelListener();
   private final HeaderMappingsUi.UiListener uiListener = new HeaderMappingsUi.UiListener();

   public HeaderMappingsUi(HeaderMappingsModel var1, HeaderMappingsController var2) {
      this.model = var1;
      this.controller = var2;
      this.widget = this.createWidget();
      var1.addPropertyChangeListener(this.modelListener);
      this.widget.addChangeListener(this.uiListener);
   }

   private ConnectedTrees createWidget() {
      KongaTreeModel var1 = this.getSourceTreeModel();
      KongaTreeModel var2 = this.getTargetTreeModel();
      ConnectedTrees var3 = new ConnectedTrees(var1, var2, ConnectedTrees.Mode.ONE_TO_ONE);
      var3.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      var3.setCellRenderers(null, new TargetFieldRenderer());
      var3.setLabels("Source File &Headers", "Salesforce &Fields");
      this.syncMappings(var3);
      return var3;
   }

   private KongaTreeModel getSourceTreeModel() {
      return TreeUtils.createFlatModel("root", this.model.getSourceHeader());
   }

   private void syncMappings(ConnectedTrees var1) {
      for (Mapping var3 : this.model.getMapList()) {
         if (var1.canConnect(var3.source, var3.target)) {
            var1.addConnection(var3.source, var3.target);
         }
      }
   }

   private KongaTreeModel getTargetTreeModel() {
      return TargetFieldsTreeModelBuilder.buildTreeModel(this.model);
   }

   public void setWaitService(WaitService var1) {
      this.controller.setWaitService(var1);
   }

   public void installSearchFields() {
      LeafDisplayNameFilterFunction var1 = new LeafDisplayNameFilterFunction(StringMatchers.CONTAINS_IGNORE_CASE, false);
      this.widget.installFilterFields(var1, var1);
   }

   public HeaderMappings getHeaderMappings() {
      return this.model.getHeaderMappings();
   }

   public boolean isTargetFieldSelected(String var1) {
      for (Pair var3 : this.widget.getConnections()) {
         SObjectSchema.Field var4 = (SObjectSchema.Field)var3.second;
         if (var4.getName().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void decorate(UiCanvas var1) {
      HeaderMappingsUiLayout var2 = new HeaderMappingsUiLayout(this.widget, this.controller);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.widget.requestFocus();
   }

   private class ModelListener implements PropertyChangeListener {
      public boolean blocked;

      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!this.blocked) {
            HeaderMappingsUi.this.uiListener.blocked = true;
            String var2 = var1.getPropertyName();
            if ("sourceAndTargetData".equals(var2)) {
               this.updateDisplayedSourceAndTargetFields();
            } else if ("mappings".equals(var2)) {
               this.updateDisplayedMappings();
            }

            HeaderMappingsUi.this.uiListener.blocked = false;
         }
      }

      private void updateDisplayedSourceAndTargetFields() {
         HeaderMappingsUi.this.widget.replaceModels(HeaderMappingsUi.this.getSourceTreeModel(), HeaderMappingsUi.this.getTargetTreeModel());
      }

      private void updateDisplayedMappings() {
         HeaderMappingsUi.this.widget.removeAllConnections();
         HeaderMappingsUi.this.syncMappings(HeaderMappingsUi.this.widget);
      }
   }

   private class UiListener implements ChangeListener {
      public boolean blocked;

      private UiListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         if (!this.blocked) {
            HeaderMappingsUi.this.modelListener.blocked = true;
            ArrayList var2 = Lists.newArrayList();

            for (Pair var4 : HeaderMappingsUi.this.widget.getConnections()) {
               var2.add(new Mapping((String)var4.first, (SObjectSchema.Field)var4.second));
            }

            HeaderMappingsUi.this.model.setMapList(var2);
            HeaderMappingsUi.this.modelListener.blocked = false;
         }
      }
   }
}
