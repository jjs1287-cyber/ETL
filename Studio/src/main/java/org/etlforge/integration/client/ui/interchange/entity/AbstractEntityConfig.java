package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorHistoryPoint;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.ProjectItemTransform;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.DeleteRequestHandlerImpl;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageHistoryPoint;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequenceFactory;
import org.etlforge.integration.client.ui.interchange.core.action.NewEntityAction;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.factory.DefaultIntegrationEntityFactory;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactory;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public abstract class AbstractEntityConfig implements EntityConfig {
   private final EntityType type;
   private ApplicationWindow appWin;
   private CurrentProjectService currentProjectService;
   private ProjectPersistor projectPersistor;
   private DeployFacade deployFacade;
   private EntityRenamer renamer;
   private UiEntityFactory entityFactory;
   private EntityExplorerSupport projectExplorer;
   private PopupMenuManager popupMenuManager;
   private ApplicationAction newEntityAction;
   private EntityFilter publicTypesFilter = EntityFilters.ACCEPTS_ALL;
   private ImmutableList<? extends ApplicationAction> defaultToolbarActions = ImmutableList.of();
   private ImmutableList<? extends SaveConsequenceFactory> saveConsequenceFactories = ImmutableList.of();
   private ImmutableList<? extends ProjectItemTransform> transformGenerators = ImmutableList.of();
   private SearchValuesCollector searchValuesCollector = SearchValuesCollector.EMPTY;
   private SearchAndReplaceMetaData searchAndReplaceMetaData = SearchAndReplaceMetaData.EMPTY;

   public AbstractEntityConfig(EntityType var1) {
      this.type = var1;
   }

   @Override
   public final EntityType getType() {
      return this.type;
   }

   protected final EntityExplorerSupport getProjectBrowser() {
      return this.projectExplorer;
   }

   protected final IntegrationProject getProject() {
      return this.projectExplorer.getProject();
   }

   public final void setAppWin(ApplicationWindow var1) {
      this.appWin = var1;
   }

   public void setCurrentProjectService(CurrentProjectService var1) {
      this.currentProjectService = var1;
   }

   public final void setEntityFactory(UiEntityFactory var1) {
      this.entityFactory = var1;
   }

   protected final UiEntityFactory getEntityFactory() {
      return this.entityFactory;
   }

   public final void setProjectPersistor(ProjectPersistor var1) {
      this.projectPersistor = var1;
   }

   public final void setDeployFacade(DeployFacade var1) {
      this.deployFacade = var1;
   }

   public final void setProjectExplorer(EntityExplorerSupport var1) {
      this.projectExplorer = var1;
   }

   public final void setPopupMenuManager(PopupMenuManager var1) {
      this.popupMenuManager = var1;
   }

   public final void setPublicTypesFilter(EntityFilter var1) {
      this.publicTypesFilter = var1;
   }

   public final void setDefaultToolbarActions(List<? extends ApplicationAction> var1) {
      this.defaultToolbarActions = ImmutableList.copyOf(var1);
   }

   public final List<? extends SaveConsequenceFactory> getSaveConsequenceFactories() {
      return this.saveConsequenceFactories;
   }

   public final void setSaveConsequenceFactories(List<? extends SaveConsequenceFactory> var1) {
      this.saveConsequenceFactories = ImmutableList.copyOf(var1);
   }

   @Override
   public final ApplicationAction newEntityAction() {
      if (this.newEntityAction == null) {
         this.newEntityAction = this.createNewEntityAction();
      }

      return this.newEntityAction;
   }

   protected ApplicationAction createNewEntityAction() {
      NewEntityAction var1 = new NewEntityAction("New." + this.type, this.currentProjectService, this.entityFactory, this.type);
      var1.setOpenEditor(this.openEditorForNewItem());
      return var1;
   }

   protected boolean openEditorForNewItem() {
      return true;
   }

   @Override
   public final EntityPageFactory getPageFactory() {
      return new AbstractEntityConfig.PageFactory();
   }

   protected abstract IntegrationEntityPage createNewPage(IntegrationEntity var1);

   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new DefaultPageConfig(var1, var3);
   }

   @Override
   public final EntityRenamer getRenamer() {
      return this.renamer;
   }

   public final void setRenamer(EntityRenamer var1) {
      this.renamer = var1;
   }

   @Override
   public EntityWizardFactory getEntityWizardFactory() {
      return null;
   }

   @Override
   public IntegrationEntityFactory getFactory() {
      return new DefaultIntegrationEntityFactory(this.type);
   }

   @Override
   public final List<? extends ProjectItemTransform> getTransformGenerators() {
      return this.transformGenerators;
   }

   public final void setTransformGenerators(List<? extends ProjectItemTransform> var1) {
      this.transformGenerators = ImmutableList.copyOf(var1);
   }

   @Override
   public final SearchValuesCollector getSearchValuesCollector() {
      return this.searchValuesCollector;
   }

   public final void setSearchValuesCollector(SearchValuesCollector var1) {
      this.searchValuesCollector = var1;
   }

   @Override
   public final SearchAndReplaceMetaData getSearchAndReplaceMetaData() {
      return this.searchAndReplaceMetaData;
   }

   public final void setSearchAndReplaceMetaData(SearchAndReplaceMetaData var1) {
      this.searchAndReplaceMetaData = var1;
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new IntegrationEntityToolTip(var1);
   }

   @Override
   public void updateValueCache(IntegrationProject var1) {
   }

   private final class PageFactory implements EntityPageFactory {
      private PageFactory() {
      }

      @Override
      public IntegrationEntityPage createPage(IntegrationEntity var1) {
         IntegrationEntityPage var2 = AbstractEntityConfig.this.createNewPage(var1);
         if (var2 != null) {
            this.logCreated();
            PageConfig var3 = AbstractEntityConfig.this.createPageConfig(AbstractEntityConfig.this.appWin, var1, AbstractEntityConfig.this.projectPersistor);
            if (var3 instanceof DefaultPageConfig) {
               this.installCommonFunctionality((DefaultPageConfig)var3);
            }

            var3.configure(var2);
            this.logConfigured();
         }

         return var2;
      }

      private void installCommonFunctionality(DefaultPageConfig var1) {
         var1.setProject(AbstractEntityConfig.this.getProject());
         var1.setPublicEntityTypesFilter(AbstractEntityConfig.this.publicTypesFilter);
         var1.setDefaultToolbarActions(AbstractEntityConfig.this.defaultToolbarActions);
         var1.setDeployFacade(AbstractEntityConfig.this.deployFacade);
         var1.setContextMenu(AbstractEntityConfig.this.popupMenuManager);
         var1.setProjectExplorer(AbstractEntityConfig.this.projectExplorer);
         DeleteRequestHandlerImpl var2 = new DeleteRequestHandlerImpl(AbstractEntityConfig.this.projectPersistor, AbstractEntityConfig.this.publicTypesFilter);
         var2.setWaitService(AbstractEntityConfig.this.appWin);
         var1.setDeleteRequestHandler(var2);
         var1.setHistoryPointFactory(
            new HistoryPointFactory() {
               @Override
               public EditorHistoryPoint createHistoryPoint(IntegrationEntityPage var1) {
                  return new EntityPageHistoryPoint(
                     var1.getObject(), AbstractEntityConfig.this.currentProjectService, AbstractEntityConfig.this.projectExplorer, PageFactory.this
                  );
               }
            }
         );
      }

      private void logCreated() {
         IntegrationEntityPage.LOG.info("The page has been created.");
      }

      private void logConfigured() {
         IntegrationEntityPage.LOG.info("The page has been configured.");
      }
   }
}
