package org.etlforge.integration.client.deploy.serverapi;

public abstract class DeployResultVisitor<T> {
   public final T visit(DeployResult var1) {
      if (var1.getCause() != null) {
         return this.onFailure(var1);
      } else {
         return hasWarnings(var1) ? this.onWarnings(var1) : this.onSuccess(var1);
      }
   }

   private static boolean hasWarnings(DeployResult var0) {
      return !var0.getGlobalWarnings().isEmpty() || !var0.getEntityWarnings().isEmpty();
   }

   protected T onFailure(DeployResult var1) {
      return null;
   }

   protected T onWarnings(DeployResult var1) {
      return null;
   }

   protected T onSuccess(DeployResult var1) {
      return null;
   }
}
