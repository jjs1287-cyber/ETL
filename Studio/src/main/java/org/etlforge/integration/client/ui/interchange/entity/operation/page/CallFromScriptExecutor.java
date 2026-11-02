package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import com.google.common.base.Optional;
import org.etlforge.application.ui.Alert;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.entity.ProjectItemTransform;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.operation.runner.OperationClientStartChecker;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.operation.OperationExecutionMode;
import org.etlforge.integration.data.entity.operation.RunnableStatus;
import org.etlforge.integration.data.script.ScriptConstants;

final class CallFromScriptExecutor implements CommandExecutor {
   private final Operation op;
   private final CallFromScriptExecutor.Transform transform;

   public CallFromScriptExecutor(UiEntityFactory var1, Operation var2) {
      this.op = var2;
      this.transform = new CallFromScriptExecutor.Transform(var1);
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      if (this.transform.isSupported(this.op, Optional.absent())) {
         this.transform.doTransform(this.op);
      } else {
         Alert.error("This operation cannot be started from a Script.", "Not Applicable");
      }
   }

   private static class Transform extends ProjectItemTransform {
      public Transform(UiEntityFactory var1) {
         super(var1, EntityType.Script);
      }

      @Override
      public boolean isSupported(IntegrationEntity var1, Optional<Folder> var2) {
         if (!(var1 instanceof Operation)) {
            return false;
         }

         OperationClientStartChecker var3 = new OperationClientStartChecker(OperationExecutionMode.RUN);
         RunnableStatus var4 = var3.checkRunnableStatus((Operation)var1);
         return var4.isRunnable();
      }

      @Override
      protected void configureDestination(IntegrationEntity var1, IntegrationEntity var2) {
         String var3 = "RunOperation(\"op." + var1.getID() + "\")";
         String var4 = ScriptConstants.wrapInTags(var3);
         ((Script)var2).setScript(var4);
      }

      @Override
      protected String getSuggestedNameOfNewItem(IntegrationEntity var1) {
         return "Run " + var1.getName();
      }
   }
}
