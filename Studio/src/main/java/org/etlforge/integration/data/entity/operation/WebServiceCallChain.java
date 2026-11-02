package org.etlforge.integration.data.entity.operation;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.id.WebServiceCallId;

public final class WebServiceCallChain {
   private final Transformation requestTf;
   private final Transformation responseTf;
   private final WebServiceCall wsCall;

   public WebServiceCallChain(Operation var1, IntegrationEntityLookup var2) {
      Preconditions.checkArgument(var1.getOperationType() == WebServiceOperationType.TYPE, "op must be of type WebServiceCall");
      List var3 = var1.getFunctionIDs();
      if (var3.isEmpty()) {
         this.wsCall = null;
         this.requestTf = null;
         this.responseTf = null;
      } else {
         this.wsCall = this.extractWebServiceCall(var3, var2);
         this.requestTf = this.extractRequestTransformation(var3, var2);
         this.responseTf = this.extractResponseTransformation(var3, var2);
      }
   }

   public Transformation getRequestTransformation() {
      return this.requestTf;
   }

   public Transformation getResponseTransformation() {
      return this.responseTf;
   }

   public WebServiceCall getWebServiceCall() {
      return this.wsCall;
   }

   private WebServiceCall extractWebServiceCall(List<IntegrationId> var1, IntegrationEntityLookup var2) {
      for (IntegrationId var4 : var1) {
         if (var4 instanceof WebServiceCallId) {
            return var2.getEntity((WebServiceCallId)var4, WebServiceCall.class);
         }
      }

      return null;
   }

   private Transformation extractRequestTransformation(List<IntegrationId> var1, IntegrationEntityLookup var2) {
      IntegrationId var3 = (IntegrationId)var1.get(0);
      return var3 instanceof TransformationId ? var2.getEntity((TransformationId)var3, Transformation.class) : null;
   }

   private Transformation extractResponseTransformation(List<IntegrationId> var1, IntegrationEntityLookup var2) {
      boolean var3 = false;

      for (IntegrationId var5 : var1) {
         if (var5 instanceof TransformationId) {
            if (var3) {
               return var2.getEntity((TransformationId)var5, Transformation.class);
            }
         } else if (this.wsCall != null && var5.equals(this.wsCall.getID())) {
            var3 = true;
         }
      }

      return null;
   }
}
