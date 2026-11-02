package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.Action;
import org.etlforge.application.Application;
import org.etlforge.application.ExitListener;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.application.ui.action.popup.SectionFactories;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuExtension;
import org.etlforge.application.ui.view.DefaultEditorViewBase;
import org.etlforge.application.ui.view.ViewToolBarManager;
import org.etlforge.application.ui.window.AddContentWaitIndicator;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowFileDropHandler;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.EditorServiceListener;
import org.etlforge.cli.CommandCollection;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.project.CloseProjectRequestListener;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.project.event.ProjectManagerEvent;
import org.etlforge.integration.client.project.event.ProjectManagerListener;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.entity.ProjectPageGroup;
import org.etlforge.integration.client.ui.interchange.component.menu.CreateNewEntityMenuShortCut;
import org.etlforge.integration.client.ui.interchange.component.menu.InterchangeMenuManager;
import org.etlforge.integration.client.ui.interchange.component.menu.NewEntityMenuFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.content.DefaultProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.Disposable;
import org.etlforge.system.thread.Condition;
import org.etlforge.system.thread.CountDownLatchCondition;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.UiUtils;

public final class InterchangeView extends DefaultEditorViewBase implements EntityConfigLookup {
   public static final String VIEW_ID = "interchange";
   private IntegrationProject project;
   private PopupMenuManager popupMenuManager;
   private final EntityConfigRegistry entityConfigRegistry;
   private final InterchangeMenuManager menuManager;
   private ViewToolBarManager toolBarManager;
   private final ProjectContentViewerManager contentViewerManager;
   private final ProjectPersistor persistor;
   private ProjectListenerImpl projectListener;
   private final List<ExitListener> exitListeners = new CopyOnWriteArrayList<>();
   private final Lock lock = new ReentrantLock();
   private final InterchangeView.ProjectManagerListenerImpl projectManagerListener;
   private final CloseProjectRequestListener closeProjectRequestListener;
   private final EditorServiceListener activeEditorListener;
   private Disposable createNewEntityShortCut;
   private final ProjectManager projectManager;
   private final ServerManager serverManager;
   private CommandCollection commands;
   private DbQuoteSaver dbQuoteSaver;
   private EntityPersistorExtensionFactory persistorFactory;
   private final EntityPageDisplayer entityPageDisplayer;
   private final CountDownLatch firstProjectOpenedLatch = new CountDownLatch(1);
   private EntityToolTipExtensionFactory tooltipFactory;

   public InterchangeView(
      ApplicationWindow var1,
      EntityConfigRegistry var2,
      ProjectContentViewerManager var3,
      ProjectManager var4,
      ServerManager var5,
      EntityPageDisplayer var6,
      ProjectPersistor var7
   ) {
      super(var1);
      this.entityConfigRegistry = var2;
      this.contentViewerManager = var3;
      this.projectManager = var4;
      this.serverManager = var5;
      this.entityPageDisplayer = var6;
      this.persistor = var7;
      this.projectManagerListener = new InterchangeView.ProjectManagerListenerImpl();
      this.menuManager = new InterchangeMenuManager(var1);
      this.closeProjectRequestListener = new InterchangeView.CloseProjectRequestHandler();
      this.exitListeners.add(new InterchangeView.ExitListenerImpl());
      this.activeEditorListener = new EditorServiceAdapter() {
         @Override
         public void activeEditorChanged(EditorServiceEvent var1) {
            if (TreeAndPageSelectionSynchronization.isEnabled()) {
               InterchangeView.this.selectInTree(var1);
            }
         }
      };
      this.listenToProjectManager();
      this.registerFileDropHandlers(var1);
      this.lock.lock();
   }

   private void selectInTree(EditorServiceEvent var1) {
      Editor var2 = var1.getEditor();
      if (var2 instanceof IntegrationEntityPage) {
         IntegrationEntity var3 = ((IntegrationEntityPage)var2).getObject();
         this.displayInTree(var3);
      }
   }

   private void listenToProjectManager() {
      this.projectManager.addProjectManagerListener(this.projectManagerListener);
      this.projectManager.addCloseProjectRequestListener(this.closeProjectRequestListener);
   }

   private void registerFileDropHandlers(ApplicationWindow var1) {
      for (WindowFileDropHandler var3 : Application.getBeansOfType(WindowFileDropHandler.class)) {
         var1.addFileDropHandler(var3);
      }
   }

   public void setGlobalShortcuts(List<? extends GlobalShortcut> var1) {
      for (GlobalShortcut var3 : var1) {
         var3.install(this.getWindow());
      }
   }

   public void setToolBarManager(ViewToolBarManager var1) {
      this.toolBarManager = var1;
   }

   public void setTopLevelMenus(Map<Integer, ? extends Menu> var1) {
      this.menuManager.setTopLevelMenus(var1);
   }

   public void setSubMenus(List<? extends Menu> var1) {
      this.menuManager.setSubMenus(var1);
   }

   public void setMenuExtensions(List<? extends MenuExtension> var1) {
      this.menuManager.setExtensions(var1);
   }

   public void setCommandCollection(CommandCollection var1) {
      this.installWindowCommands();
      this.commands = var1;
   }

   private void installWindowCommands() {
      if (this.commands != null) {
         this.getWindow().getCommandConsole().addCommand(this.commands);
         this.commands = null;
      }
   }

   public void setNewEntityMenuFactory(NewEntityMenuFactory var1) {
      this.disposeNewEntityShortCut();
      this.createNewEntityShortCut = new CreateNewEntityMenuShortCut(this, var1, this.getWindow());
   }

   private void disposeNewEntityShortCut() {
      if (this.createNewEntityShortCut != null) {
         this.createNewEntityShortCut.dispose();
      }
   }

   @Override
   public String getID() {
      return "interchange";
   }

   @Override
   public EntityConfig getEntityConfig(EntityType var1) {
      return this.entityConfigRegistry.getEntityConfig(var1);
   }

   @Override
   public void setActive(boolean var1) {
      this.lock.lock();

      try {
         if (var1 != this.isActive()) {
            super.setActive(var1);
            ApplicationWindow var2 = this.getWindow();
            if (var1) {
               this.updateTitleBar(var2);
               WindowSection var3 = var2.getWindowSection(ApplicationWindow.WEST);
               var3.setActiveContent(this.contentViewerManager.getWindowContent());
            }

            return;
         }
      } finally {
         this.lock.unlock();
      }
   }

   private void updateTitleBar(ApplicationWindow var1) {
      if (!ClientInfo.isDataLoader()) {
         String var2 = this.project == null ? PackageResources.NO_MODEL_WINDOW_TITLE_APPENDIX : this.getProjectNameForTitleBar();
         var1.setTitleAppendix(var2);
      }
   }

   private String getProjectNameForTitleBar() {
      String var1 = this.project.getName();
      if (!this.project.isDeployable()) {
         var1 = var1 + " [Read Only]";
      }

      return var1;
   }

   @Override
   public boolean prepareForDisplay() {
      this.lock.lock();

      try {
         this.prepareMenuManager();
         this.prepareToolBarManager();
         this.getEditorService().addEditorServiceListener(this.activeEditorListener);
         this.installWindowCommands();
         this.addContentViewerToWindow();

         for (ExitListener var2 : this.exitListeners) {
            Application.addExitListener(var2);
         }

         this.checkStartupProject();
         return true;
      } finally {
         this.lock.unlock();
      }
   }

   private void prepareMenuManager() {
      this.menuManager.addTopLevelExtensionsToWindow();
      this.menuManager.addMenuLevelExtensionsToWindow();
   }

   private void prepareToolBarManager() {
      this.toolBarManager.addExtensions();
   }

   private void addContentViewerToWindow() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AddContentWaitIndicator var1 = new AddContentWaitIndicator(InterchangeView.this.getWindow());
            WindowSection var2 = InterchangeView.this.getWindow().getWindowSection(ApplicationWindow.WEST);
            var2.addContent(InterchangeView.this.contentViewerManager.getWindowContent(), var1);
         }
      });
   }

   private void checkStartupProject() {
      ManagedProject var1 = this.projectManager.getCurrentProject();
      if (var1 != null) {
         this.onProjectOpened(var1);
      }
   }

   private void handleProjectOpened(ManagedProject var1) {
      this.lock.lock();

      label32: {
         try {
            if (var1.getProject() != this.project) {
               this.loadProject(var1);
               this.dbQuoteSaver = new DbQuoteSaver(var1.getProject(), this.entityPageDisplayer);
               break label32;
            }
         } finally {
            this.lock.unlock();
         }

         return;
      }

      this.restoreUiState(var1.getProject());
      this.lookForMissingPluginManifests(var1.getProject());
   }

   private void loadProject(ManagedProject var1) {
      assert this.project == null : "The previous project must be removed first!";
      IntegrationProject var2 = var1.getProject();
      this.project = var2;
      this.updateTitleBar(this.getWindow());
      this.contentViewerManager.newProjectOpened(var2);
      this.registerListenersWithNewProject(var2);
      this.installToolTipFactory(var1);
      this.updateValueCaches(var2);
   }

   private void installToolTipFactory(ManagedProject var1) {
      assert this.tooltipFactory == null;
      this.tooltipFactory = new EntityToolTipExtensionFactory(this.entityConfigRegistry, var1);
      ExtensionObjectService.getService().registerFactory(IntegrationEntity.class, this.tooltipFactory);
   }

   private void restoreUiState(final IntegrationProject var1) {
      EventQueue.invokeLater(
         new Runnable() {
            @Override
            public void run() {
               InterchangeView.this.lock.lock();

               try {
                  Runnable var1x = new Runnable() {
                     @Override
                     public void run() {
                        InterchangeView.this.firstProjectOpenedLatch.countDown();
                     }
                  };
                  InterchangeUiState.restoreState(
                     InterchangeView.this.projectManager.getProject(var1),
                     InterchangeView.this.contentViewerManager.getViewer(),
                     InterchangeView.this.getEditorService(),
                     InterchangeView.this.getWindow().getActiveObjectService(),
                     InterchangeView.this.entityPageDisplayer,
                     var1x
                  );
               } finally {
                  InterchangeView.this.lock.unlock();
               }
            }
         }
      );
   }

   private void lookForMissingPluginManifests(IntegrationProject var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      PipelinePluginManifestServerCache var3 = Application.getCacheService().getCache(PipelinePluginManifestServerCache.class);
      MissingPluginManifestFinder var4 = new MissingPluginManifestFinder(var1, var2.getPipelinePlugins(), this.serverManager, var3);
      var4.setLogger(ApplicationLog.getMainLogger());
      var4.run();
   }

   private void removeProject() {
      this.contentViewerManager.projectClosed();
      this.projectListener.dispose();
      this.projectListener = null;
      this.project = null;
      this.updateTitleBar(this.getWindow());
   }

   private void registerListenersWithNewProject(IntegrationProject var1) {
      this.projectListener = new ProjectListenerImpl(this.projectManager.getRecentProjects(), this.getEditorService(), this.entityPageDisplayer, var1);
   }

   private void updateValueCaches(IntegrationProject var1) {
      for (EntityConfig var3 : this.entityConfigRegistry.allConfigs()) {
         var3.updateValueCache(var1);
      }

      this.updateDataLocationCache(var1);
   }

   private void updateDataLocationCache(IntegrationProject var1) {
      ValueCacheUpdater var2 = new ValueCacheUpdater(this.entityConfigRegistry);
      var2.update(var1);
   }

   public PopupMenuManager getPopupMenuManager() {
      return this.popupMenuManager;
   }

   public void setPopupMenuManager(PopupMenuManager var1) {
      this.popupMenuManager = var1;
   }


   public void setExitListeners(Collection<ExitListener> var1) {
      this.exitListeners.addAll(var1);
   }

   public void displayInTree(IntegrationEntity var1) {
      this.contentViewerManager.getViewer().selectEntity(var1);
   }

   private boolean modelAllowsRemoval() {
      UiThreadSupplier var1 = new UiThreadSupplier<>(new InterchangeView.ModelRemovalChecker());
      return (Boolean)var1.get();
   }

   @Override
   public boolean allowsRemoval() {
      this.lock.lock();

      try {
         return this.modelAllowsRemoval();
      } finally {
         this.lock.unlock();
      }
   }

   @Override
   public void prepareForRemoval() {
      this.lock.lock();

      try {
         this.stopListeningToProjectManager();
         if (this.project != null) {
            this.removeProject();
         }

         this.getEditorService().removeEditorServiceListener(this.activeEditorListener);
         this.menuManager.removeTopLevelExtensionsFromWindow();
         this.menuManager.removeMenuLevelExtensionsFromWindow();
         this.toolBarManager.removeExtensions();
         this.contentViewerManager.dispose();
         this.disposeNewEntityShortCut();
         if (this.commands != null) {
            this.getWindow().getCommandConsole().removeCommand(this.commands);
         }

         for (ExitListener var2 : this.exitListeners) {
            Application.removeExitListener(var2);
         }
      } finally {
         this.lock.unlock();
      }
   }

   private void stopListeningToProjectManager() {
      this.projectManager.removeProjectManagerListener(this.projectManagerListener);
      this.projectManager.removeCloseProjectRequestListener(this.closeProjectRequestListener);
   }

   public IntegrationProject getProject() {
      this.lock.lock();

      try {
         return this.project;
      } finally {
         this.lock.unlock();
      }
   }

   private void onProjectOpened(final ManagedProject var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            InterchangeView.this.handleProjectOpened(var1);
         }
      });
      this.installPersistorFactory(var1);
   }

   private void installPersistorFactory(ManagedProject var1) {
      this.lock.lock();

      try {
         this.persistorFactory = new EntityPersistorExtensionFactory(var1.getProject(), this.persistor);
         ExtensionObjectService.getService().registerFactory(IntegrationEntity.class, this.persistorFactory);
      } finally {
         this.lock.unlock();
      }
   }

   public void afterPropertiesSet() {
      this.loadAdditionalEntityConfigs();
      this.installUiCompanionFactory();
      this.installContentViewer();
      this.lock.unlock();
   }

   private void loadAdditionalEntityConfigs() {
      this.entityConfigRegistry.loadAdditionalEntityConfigs();

      for (SectionFactories var2 : Application.getBeansOfType(SectionFactories.class)) {
         this.popupMenuManager.addFactories(var2);
      }
   }

   private void installUiCompanionFactory() {
      UiCompanionExtensionFactory var1 = new UiCompanionExtensionFactory(this.entityConfigRegistry, this.contentViewerManager, this.entityPageDisplayer);
      ExtensionObjectService.getService().registerFactory(IntegrationEntity.class, var1);
   }

   private void installContentViewer() {
      if (this.contentViewerManager instanceof DefaultProjectContentViewerManager) {
         ((DefaultProjectContentViewerManager)this.contentViewerManager).install(this);
      }
   }

   public Condition getFirstProjectDisplayedCondition() {
      return new CountDownLatchCondition(this.firstProjectOpenedLatch);
   }

   private class CloseProjectRequestHandler implements CloseProjectRequestListener {
      private CloseProjectRequestHandler() {
      }

      @Override
      public void closeProjectRequested(IntegrationProject var1) throws CloseProjectVetoException {
         boolean var2 = InterchangeView.this.modelAllowsRemoval();
         if (!var2) {
            throw new CloseProjectVetoException("The user declined the close request.");
         }
      }
   }

   private class ExitListenerImpl implements ExitListener {
      private ExitListenerImpl() {
      }

      @Override
      public boolean isExitAllowed() {
         InterchangeView.this.lock.lock();

         try {
            InterchangeView.this.projectManager.closeCurrentProject();
            return true;
         } catch (CloseProjectVetoException var6) {
            return false;
         } finally {
            InterchangeView.this.lock.unlock();
         }
      }

      @Override
      public void performExitWork() {
      }
   }

   private class ModelRemovalChecker implements Supplier<Boolean> {
      private ModelRemovalChecker() {
      }

      public Boolean get() {
         if (InterchangeView.this.project == null) {
            return true;
         }

         this.saveUiState();
         String var1 = ProjectPageGroup.get(InterchangeView.this.project);
         return InterchangeView.this.getEditorService().closeEditors(var1);
      }

      private void saveUiState() {
         InterchangeView.this.lock.lock();

         try {
            if (InterchangeView.this.project != null) {
               ManagedProject var1 = InterchangeView.this.projectManager.getProject(InterchangeView.this.project);
               InterchangeUiState.saveState(var1, InterchangeView.this.contentViewerManager.getViewer(), InterchangeView.this.getEditorService());
            }
         } finally {
            InterchangeView.this.lock.unlock();
         }
      }
   }

   private class ProjectManagerListenerImpl implements ProjectManagerListener {
      private ProjectManagerListenerImpl() {
      }

      @Override
      public void projectOpened(ProjectManagerEvent var1) {
         ManagedProject var2 = var1.getManagedProject();
         InterchangeView.this.onProjectOpened(var2);
      }

      @Override
      public void projectClosed(ProjectManagerEvent var1) {
         InterchangeView.this.lock.lock();

         try {
            if (InterchangeView.this.project != null) {
               InterchangeView.this.removeProject();
               this.removePersistorFactory();
               this.removeToolTipFactory();
            }

            if (InterchangeView.this.popupMenuManager != null) {
               InterchangeView.this.popupMenuManager.reset();
            }

            if (InterchangeView.this.dbQuoteSaver != null) {
               InterchangeView.this.dbQuoteSaver.dispose();
            }
         } finally {
            InterchangeView.this.lock.unlock();
         }
      }

      private void removePersistorFactory() {
         ExtensionObjectService.getService().removeFactory(IntegrationEntity.class, InterchangeView.this.persistorFactory);
         InterchangeView.this.persistorFactory = null;
      }

      private void removeToolTipFactory() {
         InterchangeView.this.tooltipFactory.dispose();
         ExtensionObjectService.getService().removeFactory(IntegrationEntity.class, InterchangeView.this.tooltipFactory);
         InterchangeView.this.tooltipFactory = null;
      }
   }
}
