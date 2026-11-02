package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.interchange.core.action.AbstractProjectBasedAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.TransformationsRefresher;
import org.etlforge.integration.data.entity.Transformation;

public final class RefreshAllTransformationsAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.RefreshAllTransformations";
   private final ApplicationWindow appWin;
   private final MappingStructureFactory structureFactory;

   public RefreshAllTransformationsAction(ApplicationWindow var1, CurrentProjectService var2, MappingStructureFactory var3) {
      super(var2, "action.ic.RefreshAllTransformations", TransformationActionDictionary.DICTIONARY);
      this.appWin = Preconditions.checkNotNull(var1, "appWin");
      this.structureFactory = Preconditions.checkNotNull(var3, "structureFactory");
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RefreshAllTransformationsAction.this.startRefreshJob();
         }
      });
   }

   private void startRefreshJob() {
      ApplicationWorker.SafeRunnable var1 = new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            ManagedProject var1x = RefreshAllTransformationsAction.this.getManagedProject();
            List var2 = RefreshAllTransformationsAction.this.getTransformations(var1x);
            if (var2.isEmpty()) {
               Alert.info(RefreshAllTransformationsAction.this.appWin.getFrame(), "There are no transformations in this project.", "Nothing To Refresh");
            } else {
               TransformationsRefresher var3 = new TransformationsRefresher(
                  RefreshAllTransformationsAction.this.appWin, RefreshAllTransformationsAction.this.structureFactory
               );
               var3.setCache(var1x.getTransformationMappingStructureCache());
               var3.refreshInputAndOutput(var2);
            }
         }
      };
      Application.getWorker().submitForParallel(var1);
   }

   private List<Transformation> getTransformations(ManagedProject var1) {
      return var1.getProject().getAllEntitiesOfClass(Transformation.class).toImmutableList();
   }
}
