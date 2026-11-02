package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.awt.EventQueue;
import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.entity.selector.ExistingEntityTreeSelectionUi;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.CreateNewActivityContentAction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;

public class EntityContentProvider<E extends IntegrationEntity> implements ActivityContentProvider<E> {
   private final ContentProviderContext<E> context;
   private final IntegrationProject project;
   private final EntityType entityType;
   private final Class<E> entityClass;
   private UiEntityFactory entityFactory;
   private final EntityFilterProvider<E> filterProvider;
   private final Receiver<E> contentReceiver;

   public static <E extends IntegrationEntity> EntityContentProvider<E> create(
      ContentProviderContext<E> var0, IntegrationProject var1, EntityFilterProvider<E> var2, EntityType var3, Class<E> var4, Receiver<E> var5
   ) {
      return new EntityContentProvider<>(var0, var1, var2, var3, var4, var5);
   }

   public EntityContentProvider(
      ContentProviderContext<E> var1, IntegrationProject var2, EntityFilterProvider<E> var3, EntityType var4, Class<E> var5, Receiver<E> var6
   ) {
      this.context = var1;
      this.project = var2;
      this.filterProvider = var3;
      this.entityType = var4;
      this.entityClass = var5;
      this.contentReceiver = var6;
   }

   public final void setEntityFactory(UiEntityFactory var1) {
      this.entityFactory = var1;
   }

   public final ContentProviderContext<E> getContext() {
      return this.context;
   }

   @Override
   public void createNew() {
      EntityContentProvider.NewEntityCreator var1 = new EntityContentProvider.NewEntityCreator();
      var1.start();
   }

   @Override
   public final boolean isCreateNewSupported() {
      return this.entityFactory != null;
   }

   @Override
   public final void selectExisting() {
      PipelineActivity var1 = this.context.getActivity();
      PipelineActivityType var2 = var1.getType();
      ExistingEntityTreeSelectionUi var3 = new ExistingEntityTreeSelectionUi<>(this.project, this.entityClass, var2.getDisplayName(), var2.getPlural());
      var3.setSelectedEntity((IntegrationEntity)var1.getContent());
      EntityFilter var4 = EntityFilters.or(this.filterProvider.getFilter(var1), EntityFilters.allOfType(EntityType.RootFolder));
      var3.setFilter(var4);
      var3.setCreateNewItemAction(new CreateNewActivityContentAction<>(var1, this));
      var3.select(this.contentReceiver);
   }

   protected void preProjectInsertion(E var1) {
   }

   private class NewEntityCreator implements UiEntityFactoryCallback {
      private NewEntityCreator() {
      }

      public void start() {
         ApplicationWorker var1 = Application.getWorker();
         var1.submitForParallel(new ApplicationWorker.SafeRunnable() {
            @Override
            public void run() {
               EntityContentProvider.this.entityFactory.createEntity(EntityContentProvider.this.entityType, NewEntityCreator.this);
            }
         });
      }

      @Override
      public void postProjectInsertion(IntegrationEntity var1) {
         final IntegrationEntity var2 = EntityContentProvider.this.entityClass.cast(var1);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EntityContentProvider.this.contentReceiver.handle((E)var2);
            }
         });
      }

      @Override
      public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
         EntityContentProvider.this.preProjectInsertion(EntityContentProvider.this.entityClass.cast(var1));
         return Result.continueProcess(var2);
      }
   }
}
