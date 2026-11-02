package org.etlforge.integration.data.entity.operation;

import java.util.List;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.function.Function;

public final class ValidationUtils {
   public static String getIllegalFunctionJoinMessage() {
      return "The output structure of the first transformation does not match the input structure of the second transformation";
   }

   public static String getFunctionMessageRepresentation(Function var0) {
      String var1 = "";
      if (var0 instanceof Transformation) {
         var1 = "transformation";
      } else if (var0 instanceof WebServiceCall) {
         var1 = "webservice call";
      }

      return var1 + " \"" + var0.getName() + "\"";
   }

   public static boolean hasFunction(List<IntegrationId> var0) {
      for (IntegrationId var2 : var0) {
         if (!TransformationId.UNDEFINED_ID.equals(var2) && !WebServiceCallId.UNDEFINED_ID.equals(var2)) {
            return true;
         }
      }

      return false;
   }

   public static String getNoFunctionsMessage(Operation var0) {
      OperationType var1 = var0.getOperationType();
      if (var1 == TransformationOperationType.TYPE) {
         return "No transformation specified.";
      } else if (var1 == WebServiceOperationType.TYPE) {
         return "No webservice call specified.";
      } else {
         return var1 == HostedWebServiceOperationType.TYPE ? "Missing or incompatible transformations." : "No function specified.";
      }
   }

   private ValidationUtils() {
   }
}
