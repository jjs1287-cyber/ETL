package org.etlforge.integration.client.ui.interchange;

import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.entity.move.DefaultEntityMover;
import org.etlforge.integration.client.ui.entity.move.DialogConflictResolver;
import org.etlforge.integration.client.ui.entity.move.EntityMover;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.ui.wait.WaitService;

public final class InterchangeEntityMover implements EntityMover {
   private final ProjectPersistor projectPersistor;
   private final EntityMover decoratee;

   public InterchangeEntityMover(ProjectPersistor var1, WaitService var2) {
      this.projectPersistor = var1;
      this.decoratee = this.createDecoratee(var2);
   }

   private EntityMover createDecoratee(WaitService var1) {
      DefaultEntityMover var2 = new DefaultEntityMover();
      DialogConflictResolver var3 = new DialogConflictResolver(this.createDeleteHandler(var1));
      var2.setConflictResolver(var3);
      return var2;
   }

   private DeleteRequestHandler createDeleteHandler(WaitService var1) {
      DeleteRequestHandlerImpl var2 = new DeleteRequestHandlerImpl(this.projectPersistor, EntityFilters.ACCEPTS_ALL);
      var2.setWaitService(var1);
      return var2;
   }

   @Override
   public List<IntegrationEntity> executeMove(IntegrationEntity var1) {
      List var2 = this.decoratee.executeMove(var1);
      new InterchangeEntityMover.Persistor(var2).save();
      return var2;
   }

   @Override
   public boolean isAllowedTarget(IntegrationEntity var1) {
      return this.decoratee.isAllowedTarget(var1);
   }

   @Override
   public void setItemsToMove(List<? extends IntegrationEntity> var1) {
      this.decoratee.setItemsToMove(var1);
   }

   @Override
   public List<IntegrationEntity> getItemsToMove() {
      return this.decoratee.getItemsToMove();
   }

   private class Persistor implements ApplicationWorker.SafeRunnable {
      private final List<IntegrationEntity> toSave;

      public Persistor(List<IntegrationEntity> var2) {
         this.toSave = var2;
      }

      public void save() {
         Application.getWorker().submitForParallel(this);
      }

      @Override
      public void run() {
         try {
            InterchangeEntityMover.this.projectPersistor.saveProjectRelations();

            for (IntegrationEntity var2 : this.toSave) {
               InterchangeEntityMover.this.projectPersistor.save(var2);
            }
         } catch (InterchangeSavingException var3) {
         }
      }
   }
}
