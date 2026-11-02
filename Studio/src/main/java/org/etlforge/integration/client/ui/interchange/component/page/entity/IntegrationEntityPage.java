package org.etlforge.integration.client.ui.interchange.component.page.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.help.ApplicationUiHelp;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.action.SaveEditorAction;
import org.etlforge.application.ui.window.editor.ApplyAbortedException;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorHistoryPoint;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderEvent;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListeners;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies.DisplayedEntityDependencies;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.core.action.popup.PopupMenuSites;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.DeleteHandler;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.OverlayAlert;

public abstract class IntegrationEntityPage extends GenericEditor implements ActiveObjectProvider {
   public static final boolean USE_MEDIUM_SIZED_BUTTONS_IN_TOOLBAR = ApplicationConfiguration.getBoolean("EntityPage.Toolbar.Medium", false);
   public static final Logger LOG = IntegrationEntityPageLog.LOG;
   private final IntegrationEntityPageModel model;
   private IntegrationEntityPanel inputPanel;
   private EntityPersistor entityPersistor = EntityPersistor.NONE;
   private ValidationDecorator validator;
   private DisplayedEntityDependencies dependencies;
   private final EntityPageSaveJobManager saveJobManager;
   private final List<SaveConsequence> saveConsequences = Lists.newCopyOnWriteArrayList();
   private EditorHistoryPoint historyPoint;
   private DescriptionEditor descriptionEditor;
   private final ActiveObjectProviderListeners activeObjectListeners = new ActiveObjectProviderListeners();
   private ShowContextMenuAction contextMenuShortcut;
   private final List<ActiveObjectPageAction> activeObjectActions = Lists.newArrayList();
   private final InputChangeListener changeListener;
   private EntityExplorerSupport explorerSupport;
   private PopupMenuManager contextMenuHandler;
   private ValidationModel validationModel;
   private final IntegrationEntityPage.DeletionListener deletionListener;
   private final SaveEditorAction saveAction;
   private boolean includeNameField = true;

   protected IntegrationEntityPage(IntegrationEntityPageModel var1) {
      super(var1.getId(), var1.getMainTitle(), var1.getSubTitle());
      this.model = var1;
      this.saveJobManager = new EntityPageSaveJobManager(this);
      this.changeListener = new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            IntegrationEntityPage.this.setDirty();
         }
      };
      IntegrationEntity var2 = var1.getEntity();
      this.deletionListener = new IntegrationEntityPage.DeletionListener(var2);
      this.setMementoPath(new String[]{var2.getEntityType().name(), var2.getID().toString()});
      this.setIcon(var1.getSmallIcon());
      this.setTitleIcon(this.getSmallIcon());
      this.setContributeGlobalUndoRedo();
      this.setCategory(this.createCategory());
      this.saveAction = new SaveEditorAction(this);
   }

   protected EditorCategory createCategory() {
      IntegrationEntity var1 = this.getObject();
      EntityType var2 = var1.getEntityType();
      String var3 = var2.getPlural();
      if (ClientInfo.isDataLoader() && var2 != EntityType.SalesforceConnector && var3.startsWith("Salesforce ")) {
         var3 = StringUtils.substringAfter(var3, "Salesforce ");
      }

      Icon var4 = EntityIcons.forType(var2).getDefaultIcon(EntityIcons.Size.SMALL);
      return new EditorCategory(var3, var4);
   }

   public final IntegrationEntityPageModel getPageModel() {
      return this.model;
   }

   public final void setValidationModel(ValidationModel var1) {
      this.validationModel = var1;
   }

   public final void setEntityExplorerSupport(EntityExplorerSupport var1) {
      this.explorerSupport = var1;
   }

   public final EntityExplorerSupport getEntityExplorerSupport() {
      return this.explorerSupport;
   }

   public final void setContextMenuHandler(PopupMenuManager var1) {
      this.contextMenuHandler = var1;
   }

   public final boolean showContextMenu(int var1, int var2) {
      if (this.contextMenuHandler != null) {
         JPopupMenu var3 = this.contextMenuHandler.getPopupMenu(this.getContextMenuSite(), Collections.singletonList(this.getObject()));
         if (var3 != null) {
            var3.show(this.getMainPanel(), var1, var2);
            return true;
         }
      }

      return false;
   }

   private PopupMenuSite getContextMenuSite() {
      return this.isAttached() ? PopupMenuSites.PAGE : PopupMenuSites.DETACHED_PAGE;
   }

   public final void setIncludeNameField(boolean var1) {
      this.includeNameField = var1;
   }

   @Override
   protected final void activated() {
      super.activated();
      if (this.validator != null) {
         this.validator.setEnabled(true);
      }

      this.fireActiveObjectsChanged();
   }

   @Override
   protected final void deactivated() {
      super.deactivated();
      if (this.validator != null) {
         this.validator.setEnabled(false);
      }
   }

   private void createContents() {
      this.createTitlePanel();
      this.inputPanel = this.createDataPanel();
      this.inputPanel.setIncludeNameField(this.includeNameField);
      this.layoutPage(this.inputPanel);
      ActionKeyBinding.install(this.saveAction, this.getMainPanel(), 1);
      this.bindNameField();
      this.setToolbarButtons(this.createToolBarButtons());
      this.setToolBar(this.createToolBar());
      this.contextMenuShortcut = ShowContextMenuAction.install(this);
   }

   protected void layoutPage(IntegrationEntityPanel var1) {
      this.setMainPanel(var1.getUi());
   }

   private void bindNameField() {
      BoundProperty var1 = this.inputPanel.getNameFieldProperty();
      this.getPageModel().bindProperty(var1);
   }

   public IntegrationEntity getObject() {
      return this.model.getEntity();
   }

   protected abstract IntegrationEntityPanel createDataPanel();

   protected IntegrationEntityPanel getDetailsPanel() {
      return this.inputPanel;
   }

   protected final void removeContextMenuShortcut() {
      if (this.contextMenuShortcut != null) {
         this.contextMenuShortcut.uninstall();
         this.contextMenuShortcut = null;
      }
   }

   @Override
   public final EditorHistoryPoint getHistoryPoint() {
      return this.historyPoint;
   }

   public final void setEditorHistoryPoint(EditorHistoryPoint var1) {
      if (var1 != this.historyPoint) {
         if (this.historyPoint != null) {
            this.historyPoint.dispose();
         }

         this.historyPoint = var1;
      }
   }

   @Override
   public final String getTitle() {
      return super.getSubTitle();
   }

   @Override
   public final void setClean() {
      this.setSubTitle(this.getObject().getName());
      super.setClean();
   }

   @Override
   public final void setDirty() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            IntegrationEntityPage.super.setDirty();
            IntegrationEntityPage.this.setSubTitle(IntegrationEntityPage.this.getObject().getName() + "*");
         }
      });
   }

   public void setEntityPersistor(EntityPersistor var1) {
      this.entityPersistor = var1;
   }

   public final EntityPersistor getEntityPersistor() {
      return this.entityPersistor;
   }

   @Override
   public final void save() throws SaveEditorException {
      SaveConsequence var1 = this.getSaveConsequence();
      if (this.isDirty() && var1.isAllowed()) {
         if (IntegrationProjectLock.acquireWithin(3L, TimeUnit.SECONDS)) {
            try {
               this.saveImpl(var1);
            } catch (ApplyAbortedException var8) {
            } catch (IntegrationDataPanelException var9) {
               throw new SaveEditorException(this, var9.getMessage(), var9);
            } catch (InterchangeSavingException var10) {
            } finally {
               IntegrationProjectLock.release();
            }
         } else {
            Alert.error("The page could not be saved because a background process did not complete in time. Please try again.", "Timeout");
         }
      }
   }

   private SaveConsequence getSaveConsequence() {
      return new CompoundSaveConsequence(this.saveConsequences);
   }

   private void saveImpl(SaveConsequence var1) throws IntegrationDataPanelException, InterchangeSavingException {
      try {
         this.getPageModel().setBlockPropertyChangeEvents(true);
         this.getDetailsPanel().apply();
         this.applyDescription();
         this.runAllSaveJobs();
         this.doAdditionalSaveWorkBeforePageIsMarkedAsClean();
         this.setClean();
         this.entityPersistor.save(this.getObject());
         var1.apply();
      } finally {
         this.getPageModel().setBlockPropertyChangeEvents(false);
      }
   }

   public final void addSaveJob(EntityPageSaveJob var1) {
      this.saveJobManager.add(var1);
   }

   final void runAllSaveJobs() {
      this.saveJobManager.doSave();
   }

   protected void doAdditionalSaveWorkBeforePageIsMarkedAsClean() throws InterchangeSavingException {
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.dependencies = new DisplayedEntityDependencies(this);
      this.createValidator();
      this.prepareModel();
      this.createContents();
      this.prepareDetailsPanel();
      this.registerActiveObjectProviders(var1);
      this.checkInitialDirtyState();
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      ApplicationWindow var2 = this.getWindow();
      boolean var3 = super.removeFromWindow(var1 || this.isObjectDeleted());
      if (var3) {
         this.prepareForRemoval(var2);
      }

      return var3;
   }

   private boolean isObjectDeleted() {
      return this.getObject().isDeleted();
   }

   protected void prepareForRemoval(ApplicationWindow var1) {
      this.model.dispose();
      this.deletionListener.dispose();
      this.unregisterActiveObjectProviders(var1);
      if (this.validator != null) {
         this.validator.dispose();
      }

      this.getDetailsPanel().dispose();
      this.getDetailsPanel().removeInputChangeListener(this.changeListener);
      this.getDetailsPanel().removeUndoableEditListener(this);
   }

   public final void addActiveObjectAction(ActiveObjectPageAction var1) {
      this.activeObjectActions.add(var1);
   }

   @Override
   public final void requestFocus() {
      this.getDetailsPanel().setInitialFocus();
   }

   public final void addSaveConsequence(SaveConsequence var1) {
      Preconditions.checkNotNull(var1);
      this.saveConsequences.add(var1);
   }

   public final void removeSaveConsequence(SaveConsequence var1) {
      this.saveConsequences.remove(var1);
   }

   @Override
   public List<?> getActiveObjects() {
      return Collections.singletonList(this.getObject());
   }

   @Override
   public final String getProviderId() {
      return this.getId();
   }

   @Override
   public final boolean isActive() {
      return this.getState().contains(WindowElementState.ACTIVE);
   }

   public final boolean isAttached() {
      return this.getState().contains(WindowElementState.ATTACHED);
   }

   @Override
   public final void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectListeners.add(var1);
   }

   @Override
   public final void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectListeners.add(var1);
   }

   protected final void fireActiveObjectsChanged() {
      ActiveObjectProviderEvent var1 = new ActiveObjectProviderEvent(this, this.getActiveObjects());
      this.activeObjectListeners.call().activeObjectsChanged(var1);
   }

   public final void showDescriptionEditor() {
      if (!EventQueue.isDispatchThread()) {
         throw new IllegalStateException("showDescriptionEditor() must be called from the EDT.");
      }

      if (this.descriptionEditor == null) {
         this.descriptionEditor = new DescriptionEditor(this);
      }

      this.descriptionEditor.show();
   }

   public final void askForAttention() {
      this.firePropertyChange("notification", Boolean.FALSE, Boolean.TRUE);
   }

   public final void showOverlayMessage(String var1, String var2, boolean var3) {
      OverlayAlert var4 = new OverlayAlert(var2, AlertIcons.INFO, var1);
      var4.setAlpha(0.75F);
      var4.show(this.getOverlayContainer());
      if (var3) {
         this.askForAttention();
      }
   }

   private void applyDescription() {
      if (this.descriptionEditor != null) {
         this.descriptionEditor.save();
      }
   }

   final void forceInputChangeEvent(Object var1) {
      this.changeListener.inputChanged(new InputChangeEvent(var1, null));
   }

   private void createValidator() {
      if (this.validationModel != null) {
         this.validator = new ValidationDecorator(this.validationModel, this.model.getEntity(), this.getWindow());
         this.validator.decorate(new UiCanvas() {
            @Override
            public void display(Component var1) {
               IntegrationEntityPage.this.setAdditionalTitleDecoration(var1);
            }
         });
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               IntegrationEntity var1 = IntegrationEntityPage.this.model.getEntity();
               if (!var1.isTransient()) {
                  IntegrationEntityPage.this.validator.validate();
               }
            }
         });
      }
   }

   private void prepareDetailsPanel() {
      this.getDetailsPanel().addUndoableEditListener(this);
      this.getDetailsPanel().addInputChangeListener(this.changeListener);
      this.getDetailsPanel().display();
   }

   private void prepareModel() {
      this.model.prepareForDisplay();
   }

   private void registerActiveObjectProviders(ApplicationWindow var1) {
      ActiveObjectService var2 = var1.getActiveObjectService();
      var2.addActiveObjectProvider(this);

      for (ActiveObjectProvider var4 : this.activeObjectActions) {
         var2.addActiveObjectProvider(var4);
      }
   }

   private void unregisterActiveObjectProviders(ApplicationWindow var1) {
      ActiveObjectService var2 = var1.getActiveObjectService();
      var2.removeActiveObjectProvider(this);

      for (ActiveObjectProvider var4 : this.activeObjectActions) {
         var2.removeActiveObjectProvider(var4);
      }
   }

   private void checkInitialDirtyState() {
      if (this.getObject().isTransient()) {
         this.setDirty();
      }
   }

   private List<JButton> createToolBarButtons() {
      KongaButton var1 = KongaButton.forToolbar(this.saveAction);
      if (USE_MEDIUM_SIZED_BUTTONS_IN_TOOLBAR) {
         var1.setUseMediumActionIcon(true);
      } else {
         var1.setUseSmallActionIcon(true);
      }

      ArrayList var2 = Lists.newArrayList();
      var2.add(this.createPopupMenuToolBarButton());
      var2.add(null);
      if (this.includeViewDependenciesButton()) {
         JButton var3 = this.dependencies.getActiveButton();
         var2.add(var3);
      }

      var2.add(var1);
      var2.addAll(this.getToolbarButtons());
      return var2;
   }

   protected boolean includeViewDependenciesButton() {
      return true;
   }

   @Override
   protected final JButton createHelpButton(String var1) {
      if (!ApplicationUiHelp.ENABLED) {
         return null;
      }

      JButton var2 = super.createHelpButton(var1);
      if (USE_MEDIUM_SIZED_BUTTONS_IN_TOOLBAR) {
         var2.setIcon(ApplicationResources.getIcon("Help.24"));
      }

      return var2;
   }

   public final void showDependentObjects() {
      this.dependencies.getActiveButton().doClick();
   }

   public final String getDisplayedName() {
      return this.getDetailsPanel().getDisplayedName();
   }

   private class DeletionListener extends DeleteHandler {
      public DeletionListener(IntegrationEntity var2) {
         super(var2);
      }

      @Override
      protected void onDelete(IntegrationEntity var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DeletionListener.this.closeIfStillOpen();
            }
         });
      }

      private void closeIfStillOpen() {
         if (IntegrationEntityPage.this.getWindow() != null) {
            IntegrationEntityPage.this.setClean();
            IntegrationEntityPage.this.close();
         }
      }
   }
}
