package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardAdapter;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.client.ui.interchange.entity.EntityPageFactory;
import org.etlforge.integration.client.ui.interchange.entity.ParentFolderLocator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactory;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactoryException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiThreadSupplier;

public final class EntityExplorerService extends EntityExplorerSupport implements UiEntityFactory, EntityWizardContext {
   private final ApplicationWindow appWin;
   private final EntityConfigLookup entityConfigs;
   private final CurrentProjectService currentProject;
   private final ProjectPersistor persistor;
   private final EntityPageDisplayer pageDisplayer;
   private final ParentFolderLocator parentLocator;

   public EntityExplorerService(
      ApplicationWindow var1, EntityConfigLookup var2, CurrentProjectService var3, ProjectPersistor var4, EntityPageDisplayer var5, ParentFolderLocator var6
   ) {
      this.appWin = Preconditions.checkNotNull(var1, "appWin");
      this.entityConfigs = Preconditions.checkNotNull(var2, "entityConfigs");
      this.currentProject = Preconditions.checkNotNull(var3, "currentProject");
      this.persistor = Preconditions.checkNotNull(var4, "persistor");
      this.pageDisplayer = Preconditions.checkNotNull(var5, "pageDisplayer");
      this.parentLocator = Preconditions.checkNotNull(var6, "parentLocator");
   }

   @Override
   public ApplicationWindow getWindow() {
      return this.appWin;
   }

   @Override
   public IntegrationProject getProject() {
      return this.currentProject.getProject();
   }

   @Override
   public Editor createPage(IntegrationEntity var1) {
      EntityPageFactory var2 = this.getPageFactory(var1);
      return var2.createPage(var1);
   }

   private EntityPageFactory getPageFactory(IntegrationEntity var1) {
      EntityConfig var2 = this.entityConfigs.getEntityConfig(var1.getEntityType());
      return var2.getPageFactory();
   }

   @Override
   public Editor openPageFor(final IntegrationEntity var1) {
      return UiThreadSupplier.call(new Supplier<Editor>() {
         public Editor get() {
            return EntityExplorerService.this.pageDisplayer.openPage(var1);
         }
      });
   }

   @Override
   public Editor findOpenPage(IntegrationEntity var1) {
      return this.pageDisplayer.findOpenPage(var1);
   }

   @Override
   public ProjectPersistor getProjectPersistor() {
      return this.persistor;
   }

   @Override
   public EntityExplorerSupport getProjectExplorer() {
      return this;
   }

   @Override
   public FileStore getFileStore() {
      IntegrationProject var1 = this.currentProject.getProject();
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      return var2.getFileStore();
   }

   @Override
   public UiEntityFactory getEntityFactory() {
      return this;
   }

   @Override
   public void createEntity(EntityType var1, UiEntityFactoryCallback var2) {
      this.createEntity(Request.defaultRequest(var1), var2);
   }

   @Override
   public void createEntity(Request var1, UiEntityFactoryCallback var2) {
      EntityType var3 = var1.getEntityType();
      Preconditions.checkArgument(var3 != EntityType.Folder, "Folders cannot be created through this factory");
      Folder var4 = var1.getParent().or(this.parentLocator.getParent(var3));
      IntegrationEntityFactory var5 = this.getEntityFactory(var3);

      try {
         IntegrationEntity var6 = var5.createEntity(var4);
         Receiver var7 = this.createPostCreationProcessor(var1.withParent(var4), var2);
         var7.handle(var6);
      } catch (IntegrationEntityFactoryException var8) {
         ErrorLog.attention(this.getClass(), "Internal error: " + var8.getMessage(), var8);
      }
   }

   private IntegrationEntityFactory getEntityFactory(EntityType var1) {
      EntityConfig var2 = this.entityConfigs.getEntityConfig(var1);
      return var2.getFactory();
   }

   private Receiver<IntegrationEntity> createPostCreationProcessor(Request var1, UiEntityFactoryCallback var2) {
      EntityConfig var3 = this.entityConfigs.getEntityConfig(var1.getEntityType());
      EntityWizardFactory var4 = var3.getEntityWizardFactory();
      assert var1.getParent().isPresent();
      return var4 == null ? new EntityExplorerService.NonWizardProcessor(var1, var2) : new EntityExplorerService.WizardRouter(var4, var1, var2);
   }

   private void doPostProjectInsertionProcessing(IntegrationEntity var1, UiEntityFactoryCallback var2, boolean var3) {
      if (var2 != null) {
         var2.postProjectInsertion(var1);
      }

      if (var3) {
         this.openPageFor(var1);
      }
   }

   private class NonWizardProcessor implements Receiver<IntegrationEntity> {
      private final IntegrationProject project;
      private final Request request;
      private final UiEntityFactoryCallback externalCallback;

      public NonWizardProcessor(Request var2, UiEntityFactoryCallback var3) {
         this.request = var2;
         this.project = EntityExplorerService.this.currentProject.getProject();
         this.externalCallback = var3;
      }

      public void handle(IntegrationEntity var1) {
         Result var2 = this.doAdditionalProcessing(var1, this.request.getParent().get());
         if (var2.isCanceled()) {
            var1.delete();
         } else {
            this.insertItemIntoProject(var1, var2.getParent());
            EntityExplorerService.this.doPostProjectInsertionProcessing(var1, this.externalCallback, this.request.openEditor());
         }
      }

      private Result doAdditionalProcessing(IntegrationEntity var1, Folder var2) {
         Result var3 = this.externalCallback.preProjectInsertion(var1, var2);
         if (!var3.isCanceled()) {
            Folder var4 = var3.getParent();
            if (var4 != var2) {
               this.sanitizeName(var1, var4);
            }
         }

         return var3;
      }

      private void sanitizeName(IntegrationEntity var1, Folder var2) {
         String var3 = EntityUtils.getAvailableChildName(var2, var1.getName());
         var1.setName(var3);
      }

      private void insertItemIntoProject(IntegrationEntity var1, Folder var2) {
         IntegrationProjectLock.acquire();

         try {
            this.project.insertEntity(var1, var2);
         } finally {
            IntegrationProjectLock.release();
         }
      }
   }

   private class WizardRouter implements Receiver<IntegrationEntity> {
      private final EntityWizardFactory wizardFactory;
      private final Request request;
      private final UiEntityFactoryCallback externalCallback;

      public WizardRouter(EntityWizardFactory var2, Request var3, UiEntityFactoryCallback var4) {
         this.wizardFactory = var2;
         this.request = var3;
         this.externalCallback = var4;
      }

      public void handle(IntegrationEntity var1) {
         Folder var2 = this.request.getParent().get();
         Result var3 = this.doAdditionalProcessing(var1, var2);
         if (!var3.isCanceled()) {
            this.startWizard(var1, var2);
         }
      }

      private Result doAdditionalProcessing(IntegrationEntity var1, Folder var2) {
         Result var3 = Result.continueProcess(var2);
         if (this.externalCallback != null) {
            var3 = this.externalCallback.preProjectInsertion(var1, var2);
         }

         if (var3.isCanceled()) {
            var1.delete();
         }

         return var3;
      }

      private void startWizard(final IntegrationEntity var1, final Folder var2) {
         this.ensureCurrentNameIsAvailable(var1, var2);
         Wizard var3 = UiThreadSupplier.call(new Supplier<Wizard<?>>() {
            public Wizard<?> get() {
               WizardRouter.this.wizardFactory.setShowInDialog(WizardRouter.this.request.getSource() == Request.Source.WIZARD);
               return WizardRouter.this.wizardFactory.createWizard(EntityExplorerService.this, var1, var2);
            }
         });
         this.addWizardListener(var3);
         var3.start();
      }

      private void ensureCurrentNameIsAvailable(IntegrationEntity var1, Folder var2) {
         String var3 = var1.getName();
         String var4 = EntityUtils.getAvailableChildName(var2, var3);
         if (!var4.equals(var3)) {
            var1.setName(var4);
         }
      }

      private void addWizardListener(Wizard var1) {
         var1.addWizardListener(new WizardAdapter() {
            @Override
            public void wizardCompleted(Object var1) {
               IntegrationEntity var2 = (IntegrationEntity)var1;
               EntityExplorerService.this.doPostProjectInsertionProcessing(var2, WizardRouter.this.externalCallback, WizardRouter.this.request.openEditor());
            }
         });
      }
   }
}
