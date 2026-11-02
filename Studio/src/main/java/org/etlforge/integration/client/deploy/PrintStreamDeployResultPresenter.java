package org.etlforge.integration.client.deploy;

import com.google.common.base.Preconditions;
import java.io.PrintStream;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResultVisitor;
import org.etlforge.integration.client.deploy.serverapi.EntityWarning;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;

public final class PrintStreamDeployResultPresenter {
   private final PrintStream printStream;
   private final IntegrationEntityLookup objectLookup;

   public PrintStreamDeployResultPresenter(PrintStream var1, IntegrationEntityLookup var2) {
      this.printStream = Preconditions.checkNotNull(var1, "s");
      this.objectLookup = var2;
   }

   public void handle(DeployResult var1) {
      (new DeployResultVisitor<Void>() {
         protected Void onFailure(DeployResult var1) {
            PrintStreamDeployResultPresenter.this.printStream.println("+++ Deploy failed: ");
            var1.getCause().printStackTrace(PrintStreamDeployResultPresenter.this.printStream);
            return null;
         }

         protected Void onWarnings(DeployResult var1) {
            for (String var3 : var1.getGlobalWarnings()) {
               PrintStreamDeployResultPresenter.this.printGlobalWarning(var3);
            }

            for (EntityWarning var5 : var1.getEntityWarnings()) {
               PrintStreamDeployResultPresenter.this.printItemWarning(var5);
            }

            return null;
         }

         protected Void onSuccess(DeployResult var1) {
            PrintStreamDeployResultPresenter.this.printStream.println("   Complete success!");
            return null;
         }
      }).visit(var1);
   }

   private void printGlobalWarning(String var1) {
      this.printStream.println("+++ Global Warning: " + var1);
   }

   private void printItemWarning(EntityWarning var1) {
      this.printStream.print("+++ Item Warning: ");
      this.printStream.println(this.translate(var1.getIntegrationEntityId()));
      this.printStream.print("   ");
      this.printStream.println(var1.getWarning());
   }

   private String translate(IntegrationEntityId var1) {
      StringBuilder var2 = new StringBuilder("[");
      var2.append(var1.getEntityType());
      var2.append(" - ").append(var1.toString()).append(" - ");
      if (this.objectLookup != null) {
         IntegrationEntity var3 = this.objectLookup.getEntity(var1);
         if (var3 != null) {
            var2.append(var3.getName());
         }
      }

      var2.append("]");
      return var2.toString();
   }
}
