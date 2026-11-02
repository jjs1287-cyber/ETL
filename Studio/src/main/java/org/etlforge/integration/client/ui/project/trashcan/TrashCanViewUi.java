package org.etlforge.integration.client.ui.project.trashcan;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.entity.EntityPathPublisher;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.EntityRevivorCallback;
import org.etlforge.integration.data.project.TrashCan;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.ListUtils;

public final class TrashCanViewUi implements UiProvider, Disposable {
   private final TrashCan trashCan;
   private DependencyStore dependencies;
   private final IntegrationEntityMapLookup dataStore;
   private final FilteredListModel listModel;
   private final KongaList trashCanContent;
   private final TrashCanViewUi.ReviveAction reviveAction = new TrashCanViewUi.ReviveAction();
   private final ManagedProject project;
   private ProjectListener projectListener;
   private WaitService waitService = WaitService.NONE;

   public TrashCanViewUi(ManagedProject var1) {
      this.project = var1;
      this.trashCan = var1.getTrashCan();
      List var2 = this.trashCan.getContents();
      this.dataStore = createDataStore(var2);
      this.dependencies = new DependencyStore(this.dataStore);
      this.listModel = new FilteredListModel(ListUtils.createModel(var2));
      this.trashCanContent = this.createList();
      this.reviveAction.setSelectionSource(this.trashCanContent);
      this.startListening();
   }

   private static IntegrationEntityMapLookup createDataStore(Collection<IntegrationEntity> var0) {
      IntegrationEntityMapLookup var1 = new IntegrationEntityMapLookup();

      for (IntegrationEntity var3 : var0) {
         var1.addItem(var3);
      }

      return var1;
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(4);
      var1.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setCellRenderer(RendererUtils.getIntegrationEntityListCellRenderer());
      var1.setPopupMenuRequestHandler(PopupMenuRequestHandlers.with(this.reviveAction));
      KongaList var2 = var1.newList(this.listModel);
      var2.addSelectionListener(new TrashCanViewUi.SelectionListener());
      return var2;
   }

   private void startListening() {
      this.projectListener = new TrashCanViewUi.ProjectListenerImpl();
      this.project.getProject().addProjectListener(this.projectListener);
   }

   public void setEntityFilter(EntityFilter var1) {
      ListFilter var2 = ListUtils.filterFromPredicate(var1);
      this.listModel.setFilter(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.createDisplayer().decorate(var1);
   }

   private UiProvider createDisplayer() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 2);
      var1.withEmptyBorder(5, 10, 5, 10);
      var1.northToSouth(ProjectUiResources.getString("Trashcan.ViewUi.Caption"), this.trashCanContent, this.reviveAction);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.trashCanContent.requestFocus();
   }

   @Override
   public void dispose() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            TrashCanViewUi.this.dependencies.dispose();
            TrashCanViewUi.this.stopListening();
         }
      });
   }

   private void stopListening() {
      this.project.getProject().removeProjectListener(this.projectListener);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   Action getReviveAction() {
      return this.reviveAction;
   }

   private static class Callback implements EntityRevivorCallback {
      private Callback() {
      }

      @Override
      public void error(String var1, Exception var2) {
         ErrorLog.attention(TrashCanViewUi.class, var1, var2);
      }

      @Override
      public void warning(String var1) {
         ApplicationMessageEvent.postInformationMessage(TrashCanViewUi.class, var1);
      }
   }

   private class ProjectListenerImpl extends ProjectListener.Adapter {
      private ProjectListenerImpl() {
      }

      @Override
      public void entitiesDeleted(final IntegrationProjectEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               for (IntegrationEntity var2 : var1.getEntities()) {
                  if (!var2.isTransient()) {
                     ProjectListenerImpl.this.addToList(var2);
                     TrashCanViewUi.this.dataStore.addItem(var2);
                  }
               }

               ProjectListenerImpl.this.rebuildDependencies();
            }
         });
      }

      private void addToList(Object var1) {
         DefaultListModel var2 = (DefaultListModel)TrashCanViewUi.this.listModel.getUnfilteredModel();
         var2.addElement(var1);
      }

      @Override
      public void entitiesRevived(final IntegrationProjectEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               for (IntegrationEntity var2 : var1.getEntities()) {
                  ProjectListenerImpl.this.removeFromList(var2);
                  TrashCanViewUi.this.dataStore.removeItem(var2);
               }

               ProjectListenerImpl.this.rebuildDependencies();
            }
         });
      }

      private void removeFromList(IntegrationEntity var1) {
         DefaultListModel var2 = (DefaultListModel)TrashCanViewUi.this.listModel.getUnfilteredModel();
         var2.removeElement(var1);
         this.rebuildDependencies();
      }

      private void rebuildDependencies() {
         if (TrashCanViewUi.this.dependencies != null) {
            TrashCanViewUi.this.dependencies.dispose();
         }

         TrashCanViewUi.this.dependencies = new DependencyStore(TrashCanViewUi.this.dataStore);
      }
   }

   private class ReviveAction extends AbstractSingleTypeSelectionAction<IntegrationEntity> {
      public ReviveAction() {
         super(IntegrationEntity.class, ProjectUiResources.getString("Trashcan.ViewUi.Revive.Name"), ApplicationResources.getIcon("Trash.16"));
      }

      @Override
      protected void run(List<IntegrationEntity> var1) {
         List var2 = this.getItemsToRevive(var1);
         this.revive(var2);
      }

      private void revive(final List<IntegrationEntity> var1) {
         Runnable var2 = new Runnable() {
            @Override
            public void run() {
               TrashCanViewUi.this.trashCan.revive(var1, new TrashCanViewUi.Callback());
               TrashCanViewUi.this.trashCanContent.setSelection(OrderedSelection.EMPTY);
            }
         };
         KongaSwingWorker.runWhileWaiting(var2, TrashCanViewUi.this.waitService);
      }

      private List<IntegrationEntity> getItemsToRevive(List<IntegrationEntity> var1) {
         Object[] var2 = new RevivalInspector(var1, this.getRemainingEntities(var1), TrashCanViewUi.this.dependencies).inspect();
         ArrayList var3 = Lists.newArrayList();

         for (Object var7 : var2) {
            var3.add((IntegrationEntity)var7);
         }

         return var3;
      }

      private List<IntegrationEntity> getRemainingEntities(List<IntegrationEntity> var1) {
         ArrayList var2 = Lists.newArrayList();
         ListModel var3 = TrashCanViewUi.this.listModel.getUnfilteredModel();
         int var4 = 0;

         for (int var5 = var3.getSize(); var4 < var5; var4++) {
            var2.add((IntegrationEntity)var3.getElementAt(var4));
         }

         var2.removeAll(var1);
         return var2;
      }
   }

   private class SelectionListener implements org.etlforge.ui.selection.SelectionListener {
      private final EntityPathPublisher pathPublisher = new EntityPathPublisher(this);

      private SelectionListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         OrderedSelection var2 = (OrderedSelection)var1.getSelection();
         int var3 = var2.size();
         if (var3 == 1) {
            IntegrationEntity var4 = (IntegrationEntity)var2.head();
            this.pathPublisher.publish(var4);
         } else {
            this.pathPublisher.publish(null);
         }
      }
   }
}
