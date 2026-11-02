package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.function.Function;

public final class HostedWebServiceCallChain {
   private final WebServiceCall wsCall;
   private final Transformation firstTf;
   private final Transformation secondTf;

   public HostedWebServiceCallChain(Operation var1, IntegrationEntityLookup var2) {
      this.wsCall = this.extractWebServiceCall(var1, var2);
      Transformation[] var3 = this.extractTransformations(var1, var2);
      this.firstTf = var3[0];
      this.secondTf = var3[1];
   }

   public WebServiceCall getWebServiceCall() {
      return this.wsCall;
   }

   public Transformation getFirstTransformation() {
      return this.firstTf;
   }

   public Transformation getSecondTransformation() {
      return this.secondTf;
   }

   private WebServiceCall extractWebServiceCall(Operation var1, IntegrationEntityLookup var2) {
      WebServiceCallId var3 = var1.getHostedWebServiceId();
      return var2.getEntity(var3, WebServiceCall.class);
   }

   private Transformation[] extractTransformations(Operation var1, IntegrationEntityLookup var2) {
      Transformation[] var3 = new Transformation[2];
      OperationFunctionInfo var4 = new OperationFunctionInfo(var1, var2);

      for (Function var6 : var4.getFunctions()) {
         if (var6 instanceof Transformation) {
            Transformation var7 = (Transformation)var6;
            if (var3[0] != null) {
               var3[1] = var7;
               break;
            }

            var3[0] = var7;
         }
      }

      return var3;
   }
}
