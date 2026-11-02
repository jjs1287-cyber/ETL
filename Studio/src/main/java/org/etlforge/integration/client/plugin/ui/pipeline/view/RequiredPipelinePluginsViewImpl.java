package org.etlforge.integration.client.plugin.ui.pipeline.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePluginStore;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePluginStoreListener;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;
import org.etlforge.integration.client.plugin.ui.pipeline.PipelinePluginInfoDialogViewer;
import org.etlforge.integration.client.plugin.ui.pipeline.tree.AppliedPipelinePluginTree;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.ComponentCustomizer;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.tree.KongaTree;

final class RequiredPipelinePluginsViewImpl extends AbstractWindowSectionContent implements RequiredPipelinePluginsView {
   private volatile ProjectPluginInfo projectPlugins;
   private final AppliedPipelinePluginTree tree;
   private final RequiredPipelinePluginsViewImpl.EntityListener entityListener = new RequiredPipelinePluginsViewImpl.EntityListener();
   private final SelectionAction viewDetailsAction = new RequiredPipelinePluginsViewImpl.ViewDetailsAction();
   private final RequiredPipelinePluginsViewImpl.CheckAvailabilityAction checkAvailabilityAction;
   private final KongaLayeredPane ui = new KongaLayeredPane();

   public RequiredPipelinePluginsViewImpl(WindowSection var1, PipelinePluginInfoProvider var2) {
      super(CONTENT_ID, var1);
      this.tree = this.createTree();
      this.checkAvailabilityAction = new RequiredPipelinePluginsViewImpl.CheckAvailabilityAction(var2);
      this.addResource(new AbstractWindowElementResource() {
         @Override
         public void dispose(WindowElement var1) {
            RequiredPipelinePluginsViewImpl.this.stopListeningToProject();
         }
      });
   }

   @Override
   public void setProjectPlugins(ProjectPluginInfo var1) {
      this.stopListeningToProject();
      this.projectPlugins = var1;
      this.tree.setProjectPlugins(var1);
      this.checkAvailabilityAction.update();
      this.listenToProject();
   }

   @Override
   public void refresh() {
      RequiredPipelinePluginsViewImpl.RefreshWorker var1 = new RequiredPipelinePluginsViewImpl.RefreshWorker();
      ApplicationWindow var2 = this.getWindow();
      KongaSwingWorker.runWhileWaiting(var1, var2);
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.getTitlePanel()).center(this.tree).withBackground(StyleSheet.GenericEditorPanelClass.getBackground());
      this.ui.setBaseComponent(var2.container());
      BorderLayoutBuilder.from(var1).center(this.ui);
      this.installKeyBindings(var1);
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.PLUGIN_16;
   }

   @Override
   public String getTitle() {
      return PluginUiResources.getString("RequiredPluginsView.Title");
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      var1.add(createToolBarButton(this.viewDetailsAction));
      var1.add(createToolBarButton(this.checkAvailabilityAction));
      super.populateToolBar(var1);
   }

   private void installKeyBindings(JComponent var1) {
      InputMap var2 = var1.getInputMap(1);
      var2.put(KeyUtils.REFRESH, "refresh");
      ActionMap var3 = var1.getActionMap();
      var3.put("refresh", new RequiredPipelinePluginsViewImpl.RefreshAction());
   }

   private void listenToProject() {
      if (this.projectPlugins != null) {
         this.projectPlugins.getProject().addProjectListener(this.entityListener);
         this.projectPlugins.getAppliedPlugins().addListener(this.entityListener);
      }
   }

   private void stopListeningToProject() {
      if (this.projectPlugins != null) {
         this.projectPlugins.getProject().removeProjectListener(this.entityListener);
         this.projectPlugins.getAppliedPlugins().removeListener(this.entityListener);
      }
   }

   private AppliedPipelinePluginTree createTree() {
      AppliedPipelinePluginTree var1 = new AppliedPipelinePluginTree(this.projectPlugins, PluginUiResources.getString("RequiredPluginsView.Tree.Root"));
      var1.asSelectionSource().setDefaultAction(this.viewDetailsAction);
      var1.customize(new ComponentCustomizer<KongaTree>() {
         public KongaTree customize(KongaTree var1) {
            var1.setToggleClickCount(-1);
            return var1;
         }
      });
      return var1;
   }

   private class CheckAvailabilityAction extends AbstractAction {
      private final PipelinePluginInfoProvider infoProvider;

      public CheckAvailabilityAction(PipelinePluginInfoProvider var2) {
         super(null, ClientIcons.VALIDATE_16);
         this.infoProvider = var2;
         this.putValue("ShortDescription", PluginUiResources.getString("RequiredPluginsView.Check.ToolTip"));
         this.update();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Set var1x = RequiredPipelinePluginsViewImpl.this.tree.getPlugins();
               if (!var1x.isEmpty()) {
                  CheckAvailabilityAction.this.runJob(var1x);
               }
            }
         });
      }

      public void update() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckAvailabilityAction.this.setEnabled(!RequiredPipelinePluginsViewImpl.this.tree.isEmpty());
            }
         });
      }

      private void runJob(Set<AppliedPipelinePlugin> var1) {
         CheckAvailabilityJob var2 = new CheckAvailabilityJob(this.infoProvider, var1);
         var2.setUseProgressDialog(0);
         var2.submit(RequiredPipelinePluginsViewImpl.this.ui, 0);
      }
   }

   private class EntityListener extends ProjectListener.Adapter implements AppliedPipelinePluginStoreListener {
      private EntityListener() {
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         RequiredPipelinePluginsViewImpl.this.refresh();
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         RequiredPipelinePluginsViewImpl.this.refresh();
      }

      @Override
      public void assignedPluginsChange(AppliedPipelinePluginStore var1, IntegrationEntity var2) {
         RequiredPipelinePluginsViewImpl.this.refresh();
      }
   }

   private class RefreshAction extends AbstractAction {
      private RefreshAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         RequiredPipelinePluginsViewImpl.this.refresh();
      }
   }

   private class RefreshWorker implements ApplicationWorker.SafeRunnable {
      private RefreshWorker() {
      }

      @Override
      public void run() {
         RequiredPipelinePluginsViewImpl.this.tree.refresh();
         RequiredPipelinePluginsViewImpl.this.checkAvailabilityAction.update();
      }
   }

   private class ViewDetailsAction extends AbstractSingleObjectSelectionAction<Object> {
      public ViewDetailsAction() {
         super(Object.class);
         ActionDecorators.VIEW_DETAILS.decorate(this);
         this.putValue("ShortDescription", PluginUiResources.getString("RequiredPluginsView.ViewDetails.ToolTip"));
      }

      @Override
      protected boolean isEnabledFor(Object var1) {
         return var1 instanceof PluginIdentifier || var1 instanceof IntegrationEntity;
      }

      @Override
      protected void run(Object var1, ActionEvent var2) {
         if (var1 instanceof IntegrationEntity) {
            UiCompanion.of((IntegrationEntity)var1).openPage();
         } else if (var1 instanceof PluginIdentifier) {
            final PluginIdentifier var3 = (PluginIdentifier)var1;
            BusyWorker var4 = new BusyWorker(RequiredPipelinePluginsViewImpl.this.getWindow()) {
               @Override
               protected void doWork() {
                  ViewDetailsAction.this.viewPluginInfo(var3);
               }
            };
            var4.setDelay(150);
            var4.run();
         }
      }

      private void viewPluginInfo(PluginIdentifier var1) {
         PipelinePluginInfoDialogViewer var2 = new PipelinePluginInfoDialogViewer();
         var2.setManifestCache(RequiredPipelinePluginsViewImpl.this.projectPlugins.getManifestCache());
         var2.view(var1);
      }
   }
}
