package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Predicate;
import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import com.etlforge.integration.salesforce.ui.renderer.Renderers;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.property.SingleObjectListSelectionBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.ListUtils;
import org.etlforge.util.Property;

public final class ObjectSelectionListWidget implements ObjectSelectionWidget {
   private final KongaList objectsList;

   public ObjectSelectionListWidget(ObjectSelectionModel var1) {
      this.objectsList = this.createObjectsList(var1);
      this.bindToModel(var1);
   }

   private KongaList createObjectsList(ObjectSelectionModel var1) {
      KongaListFactory var2 = ComponentFactories.newKongaListFactory();
      var2.setVisibleRowCount(4);
      ObjectSelectionListWidget.Renderer var3 = new ObjectSelectionListWidget.Renderer(var1.getCommonlyUsedObjects(), var1.getObjectNameDisplayMode());
      var2.setCellRenderer(var3);
      var2.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      return var2.newList();
   }

   private void bindToModel(ObjectSelectionModel var1) {
      Property var2 = var1.getProperty("selectedObject");
      ListBinding var3 = new ListBinding(var1.getProperty("availableObjects"), this.objectsList);
      var3.setUseFilteredModel(true);
      var3.setListOrder(var1.objectNameOrdering());
      Bindings.of(var3, new SingleObjectListSelectionBinding(var2, this.objectsList)).syncUi();
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.objectsList.setDefaultAction(var1);
   }

   @Override
   public void setNameFilter(Predicate<? super SObjectSchema> var1) {
      ListFilter var2 = ListUtils.filterFromPredicate(var1);
      FilteredListModel var3 = (FilteredListModel)this.objectsList.getModel();
      var3.setFilter(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.objectsList.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.objectsList.requestFocus();
   }

   @Override
   public boolean isEmpty() {
      return this.objectsList.isEmpty();
   }

   @Override
   public void selectFirst() {
      this.objectsList.selectFirst();
   }

   private static class Renderer implements ListCellRenderer {
      private final CommonlyUsedObjects commonlyUsed;
      private final ListCellRenderer impl;

      public Renderer(CommonlyUsedObjects var1, NameDisplayMode var2) {
         this.commonlyUsed = var1;
         this.impl = Renderers.forList(var2);
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         Component var6 = this.impl.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof SObjectSchema) {
            this.setFont(var1, (SObjectSchema)var2, var6);
         }

         return var6;
      }

      private void setFont(JList var1, SObjectSchema var2, Component var3) {
         Font var4 = var1.getFont();
         if (this.commonlyUsed.contains(var2)) {
            var4 = Fonts.bold(var4);
         }

         var3.setFont(var4);
      }
   }
}
