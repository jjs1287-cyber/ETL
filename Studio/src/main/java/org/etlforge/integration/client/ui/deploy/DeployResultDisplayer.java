package org.etlforge.integration.client.ui.deploy;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResultVisitor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.event.ApplicationMessageEvent;

public final class DeployResultDisplayer {
   public static void display(DeployResult var0, IntegrationProject var1) {
      Preconditions.checkNotNull(var1, "project");
      DeployResultDisplayer.Visitor var2 = new DeployResultDisplayer.Visitor(var1);
      var2.visit(var0);
   }

   private DeployResultDisplayer() {
   }

   private static class Visitor extends DeployResultVisitor<Void> {
      private final IntegrationProject project;

      public Visitor(IntegrationProject var1) {
         this.project = var1;
      }

      protected Void onFailure(DeployResult var1) {
         ErrorLog.attention(DeployResultDisplayer.class, "Deploy failed", var1.getCause());
         return null;
      }

      protected Void onWarnings(final DeployResult var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DeployWarningsDialog.display(var1, Visitor.this.project.getItemLookup());
            }
         });
         return null;
      }

      protected Void onSuccess(DeployResult var1) {
         ApplicationMessageEvent.postInformationMessage(DeployResultDisplayer.class, Strings.get("Deploy.Result.Messages.Success"));
         return null;
      }
   }
}
