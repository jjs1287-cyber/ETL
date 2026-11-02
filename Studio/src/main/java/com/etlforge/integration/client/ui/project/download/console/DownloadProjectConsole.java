package com.etlforge.integration.client.ui.project.download.console;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.etlforge.integration.client.admin.project.ProjectDownloaderCallback;
import com.etlforge.integration.client.ui.project.download.ProjectDownloadParams;
import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JComponent;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.util.SingleLockWaitServiceBinding;
import org.etlforge.application.ui.widget.FolderSelectorPanelBuilder;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.ui.project.info.FixedEntityTypeFilter;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;
import org.etlforge.integration.client.ui.project.info.ServerProjectsTree;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.file.FileSelectionListener;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.preference.PreferencesKey;

public final class DownloadProjectConsole implements UiProvider, Disposable {
   private DownloadProjectController controller;
   private final SingleLockWaitServiceBinding waitHandler;
   private final ServerProjectsTree projectTree;
   private final FileSelectorPanel destinationSelector;
   private final SelectServerAction selectServerAction;
   private final DownloadProjectAction downloadProjectAction;
   private final DownloadProjectConsole.SelectionSourceImpl selectionSource;
   private WaitService waitService = WaitService.NONE;
   private File defaultDownloadLocation;
   private boolean downloadAutomatically = true;

   public DownloadProjectConsole(ProjectConsoleModel var1) {
      this.waitHandler = new SingleLockWaitServiceBinding(var1, "talkingToServer");
      this.defaultDownloadLocation = SystemUtils.getUserHome();
      this.projectTree = this.createProjectsTree(var1);
      this.destinationSelector = this.createDestinationPanel();
      this.selectServerAction = new SelectServerAction();
      this.selectionSource = new DownloadProjectConsole.SelectionSourceImpl();
      this.selectionSource.updateSelection();
      this.downloadProjectAction = this.createDownloadProjectAction(this.selectionSource);
      this.projectTree.setDefaultAction(new DownloadProjectConsole.DefaultAction());
      this.listenToComponents();
   }

   private ServerProjectsTree createProjectsTree(ProjectConsoleModel var1) {
      ServerProjectsTree var2 = new ServerProjectsTree(var1);
      var2.setTitle(ProjectDownloadResources.getString("Console.Tree.Title"));
      var2.setFilterControl(new FixedEntityTypeFilter(ImmutableSet.of(EntityType.Project, EntityType.Operation)));
      var2.setToggleClickCount(-1);
      return var2;
   }

   private FileSelectorPanel createDestinationPanel() {
      FolderSelectorPanelBuilder var1 = new FolderSelectorPanelBuilder();
      var1.setPreferenceKey(new PreferencesKey(DownloadProjectConsole.class, "Destination"));
      var1.setDefaultFolder(this.defaultDownloadLocation);
      return var1.buildPanel(null);
   }

   private DownloadProjectAction createDownloadProjectAction(SelectionSource var1) {
      DownloadProjectAction var2 = new DownloadProjectAction();
      var2.setSelectionSource(var1);
      return var2;
   }

   private void listenToComponents() {
      DownloadProjectConsole.Listener var1 = new DownloadProjectConsole.Listener();
      this.projectTree.getSelectionSource().addSelectionListener(var1);
      this.destinationSelector.addFileSelectionListener(var1);
   }

   public void setController(DownloadProjectController var1) {
      this.controller = var1;
      this.selectServerAction.setController(var1);
      this.downloadProjectAction.setController(var1);
      var1.addCallback(new ProjectDownloaderCallback.Adapter() {
         @Override
         public void downloadCompleted(ProjectLocation var1) {
            DownloadProjectConsole.this.destinationSelector.getSelectedFile();
         }
      });
   }

   @Override
   public void dispose() {
      if (this.controller != null) {
         this.controller.dispose();
      }

      this.waitHandler.dispose();
   }

   public void setDownloadAutomatically(boolean var1) {
      this.downloadAutomatically = var1;
   }

   public void prepareForDisplay() {
      if (this.downloadAutomatically) {
         this.downloadProjectList();
      }
   }

   public void downloadProjectList() {
      this.controller.downloadProjectList();
   }

   @Override
   public void decorate(UiCanvas var1) {
      DownloadProjectConsole.Layout var2 = new DownloadProjectConsole.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.projectTree.requestFocus();
   }

   public void setDefaultDownloadLocation(File var1) {
      this.defaultDownloadLocation = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
      this.waitHandler.setWaitService(var1);
   }

   public WaitService getWaitService() {
      return this.waitService;
   }

   public void setDownloadAvailable(boolean var1) {
      this.downloadProjectAction.setDownloadAvailable(var1);
   }

   public void setSelectServerActionEnabled(boolean var1) {
      this.selectServerAction.setEnabled(var1);
   }

   public void setDownloadProjectActionName(String var1) {
      this.downloadProjectAction.putValue("Name", var1);
   }

   private final class DefaultAction extends AbstractSelectionAction {
      private DefaultAction() {
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (DownloadProjectConsole.this.downloadProjectAction.isEnabled()) {
                  DownloadProjectConsole.this.downloadProjectAction.actionPerformed(var1);
               }
            }
         });
      }
   }

   private final class Layout {
      private Layout() {
      }

      public void decorate(UiCanvas var1) {
         new BorderLayoutBuilder()
            .northToSouth(this.controlPanel(), DownloadProjectConsole.this.projectTree, this.southPanel())
            .deeplyNonOpaque()
            .decorate(var1);
      }

      private Object controlPanel() {
         return Horizontal.box()
            .addAllWithSpace(20, DownloadProjectConsole.this.selectServerAction, this.destinationPanel())
            .withBorder(Empty.border(0, 0, 10, 0));
      }

      private Object destinationPanel() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.addAllWithSpace(5, "Save to:", DownloadProjectConsole.this.destinationSelector);
         return var1;
      }

      private JComponent southPanel() {
         KongaButton var1 = new KongaButton(DownloadProjectConsole.this.downloadProjectAction);
         WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var1);
         var1.setUseMediumActionIcon(true);
         return InvisiblePanel.of(var1);
      }
   }

   private final class Listener implements SelectionListener, FileSelectionListener {
      private Listener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         DownloadProjectConsole.this.selectionSource.updateSelection();
      }

      @Override
      public void selectionChanged(File var1) {
         DownloadProjectConsole.this.selectionSource.updateSelection();
      }
   }

   private final class SelectionSourceImpl implements SelectionSource {
      private final SelectionListeners listeners = new SelectionListeners();
      private Selection selection = OrderedSelection.EMPTY;

      private SelectionSourceImpl() {
      }

      @Override
      public Selection getSelection() {
         return this.selection;
      }

      @Override
      public void addSelectionListener(SelectionListener var1) {
         this.listeners.add(var1);
      }

      @Override
      public void removeSelectionListener(SelectionListener var1) {
         this.listeners.remove(var1);
      }

      public void updateSelection() {
         OrderedSelection var1 = OrderedSelection.EMPTY;
         OrderedSelection var2 = (OrderedSelection)DownloadProjectConsole.this.projectTree.getSelectionSource().getSelection();
         if (var2.size() == 1) {
            Object var3 = var2.head();
            if (var3 instanceof IntegrationProject) {
               File var4 = DownloadProjectConsole.this.destinationSelector.getSelectedFile();
               if (var4 != null) {
                  ProjectDownloadParams var5 = new ProjectDownloadParams((IntegrationProject)var3, var4);
                  var1 = SelectionFactory.newOrderedSelection(var5);
               }
            }
         }

         boolean var6 = var1 != this.selection;
         this.selection = var1;
         if (var6) {
            this.listeners.call().selectionChanged(new SelectionChangedEvent(this, var1));
         }
      }
   }
}
