package org.etlforge.integration.client.plugin.ui.pipeline;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSourceWithDefaultAction;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.StringListDecorator;

public final class PipelinePluginListDisplayer implements UiProvider {
   private final KongaList list = this.createList();
   private String noPluginsMessage;

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setVisibleRowCount(15);
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      return var1.newList();
   }

   public void setNoPluginsMessage(String var1) {
      this.noPluginsMessage = var1;
   }

   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   public void display(List<PipelinePluginManifest> var1) {
      final ListModel var2 = this.createListModel(var1);
      this.list.setModel(var2);
      if (var1.size() > 0) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               IndexedSelection var1x = SelectionFactory.newIndexedSelection(var2.getElementAt(0), 0);
               PipelinePluginListDisplayer.this.list.setSelection(var1x);
               PipelinePluginListDisplayer.this.list.requestFocus();
            }
         });
      }
   }

   private ListModel createListModel(List<PipelinePluginManifest> var1) {
      DefaultListModel var2 = new DefaultListModel();
      if (var1.isEmpty()) {
         this.addNoPluginsAvailableMessage(var2);
      } else {
         this.addPluginsToModel(var1, var2);
      }

      return var2;
   }

   private void addPluginsToModel(List<PipelinePluginManifest> var1, DefaultListModel var2) {
      for (PipelinePluginManifest var4 : var1) {
         var2.addElement(new PipelinePluginListItem(var4));
      }
   }

   private void addNoPluginsAvailableMessage(DefaultListModel var1) {
      if (this.noPluginsMessage != null) {
         StringListDecorator var2 = new StringListDecorator(this.noPluginsMessage);
         var1.addElement(var2);
      }
   }

   public PipelinePluginManifest[] getSelection() {
      IndexedSelection var1 = this.list.getSelection();
      if (var1.isEmpty()) {
         return new PipelinePluginManifest[0];
      }

      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.items()) {
         if (var4 instanceof PipelinePluginListItem) {
            var2.add(((PipelinePluginListItem)var4).getPlugin());
         }
      }

      return (PipelinePluginManifest[]) var2.toArray(new PipelinePluginManifest[var2.size()]);
   }

   public void addSelectionListener(SelectionListener var1) {
      this.list.addSelectionListener(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.list.removeSelectionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   public SelectionSourceWithDefaultAction asSelectionSource() {
      return this.list;
   }
}
