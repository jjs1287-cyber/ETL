package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.ui.entity.tree.EntityCollectionTreeFactory;
import org.etlforge.integration.client.ui.interchange.deploy.InterchangeDeployFacade;
import org.etlforge.integration.client.ui.operation.runner.OperationRunner;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.tree.KongaTree;

public final class RunAllOperationsAction extends ActiveOperationsAction {
   public static final String ID = "action.ic.RunAllOperations";
   private final ApplicationWindow appWin;
   private final OperationRunnerFactory factory;
   private final InterchangeDeployFacade deployFacade;

   public RunAllOperationsAction(ApplicationWindow var1, OperationRunnerFactory var2, InterchangeDeployFacade var3) {
      super(var1.getActiveObjectService(), "action.ic.RunAllOperations", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.factory = var2;
      this.deployFacade = var3;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RunAllOperationsAction.this.submitOperations();
         }
      });
   }

   private void submitOperations() {
      final List var1 = this.getOperationsToRun(this.getActiveObjects());
      if (var1.isEmpty()) {
         Alert.info(this.appWin.getFrame(), "There are no operations to run!", "No Operations");
      } else {
         DeployGate var2 = this.deployFacade.newDeployGate();
         var2.setItemsToCheck(var1);
         var2.setJob(new Runnable() {
            @Override
            public void run() {
               OperationRunner var1x = RunAllOperationsAction.this.factory.newRunner();
               var1x.runAll(var1);
            }
         });
         var2.start();
      }
   }

   private List<Operation> getOperationsToRun(List<?> var1) {
      RunAllOperationsAction.Collector var2 = new RunAllOperationsAction.Collector();
      var2.collect(var1);
      return var2.getOperationsToRun(this.appWin);
   }

   private static final class Collector {
      private final List<Operation> operations = Lists.newArrayList();
      private final List<IntegrationEntity> contributors = Lists.newArrayList();

      private Collector() {
      }

      public void collect(List<?> var1) {
         for (Object var3 : var1) {
            if (var3 instanceof Operation) {
               Operation var4 = (Operation)var3;
               this.operations.add(var4);
               this.contributors.add(var4);
            } else if (var3 instanceof Folder) {
               this.collectOperations((Folder)var3);
            }
         }
      }

      public List<Operation> getOperationsToRun(ApplicationWindow var1) {
         return this.isGreenLight(var1) ? this.operations : new ArrayList<>();
      }

      private boolean isGreenLight(ApplicationWindow var1) {
         return this.operations.size() <= 1 ? true : RunAllOperationsAction.Prompter.isOk(var1.getFrame(), this.contributors);
      }

      private void collectOperations(Folder var1) {
         if (var1.getItemType() == EntityType.Operation) {
            for (IntegrationEntity var3 : var1.getAllChildren()) {
               if (var3 instanceof Operation) {
                  this.contributors.add(var1);
                  Operation var4 = (Operation)var3;
                  this.contributors.add(var4);
                  this.operations.add(var4);
               } else if (var3 instanceof Folder) {
                  this.collectOperations((Folder)var3);
               }
            }
         }
      }
   }

   private static final class Prompter implements UiProvider {
      private final KongaTree tree;

      private Prompter(List<IntegrationEntity> var1) {
         EntityCollectionTreeFactory var2 = new EntityCollectionTreeFactory();
         this.tree = var2.newTree(var1);
         this.tree.setShowsRootHandles(true);
      }

      public static boolean isOk(JFrame var0, List<IntegrationEntity> var1) {
         RunAllOperationsAction.Prompter var2 = new RunAllOperationsAction.Prompter(var1);
         KongaDialog var3 = UiProviderDialogDisplayer.createDialog(var0, var2, "Are you sure?", KongaDialog.CONTINUE_CANCEL, null);
         var3.setContinueButtonAsDefault();
         var3.setVisible(true);
         return var3.wasOkPressed();
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
         var2.north("Are you sure you want to run all of the following operations?").center(this.tree);
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.tree.requestFocusInWindow();
      }
   }
}
