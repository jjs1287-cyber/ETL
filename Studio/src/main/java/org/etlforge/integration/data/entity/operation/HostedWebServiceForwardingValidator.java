package org.etlforge.integration.data.entity.operation;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;
import org.etlforge.integration.data.structure.WebServiceStructure;

public final class HostedWebServiceForwardingValidator {
   private final WebServiceCall wsCall;
   private final Operation hostedWsOp;
   private final IntegrationEntityLookup lookup;

   public HostedWebServiceForwardingValidator(WebServiceCall var1, Operation var2, IntegrationEntityLookup var3) {
      this.wsCall = var1;
      this.hostedWsOp = var2;
      this.lookup = var3;
   }

   public boolean isValid() {
      List var1 = this.hostedWsOp.getSiblingOperations();
      if (var1.isEmpty()) {
         return false;
      }

      boolean var2 = true;

      for (Object __etlItem35 : var1) {
         OperationId var4 = (OperationId) __etlItem35;
         if (!this.validateSibling(var4, var2)) {
            return false;
         }

         var2 = false;
      }

      return true;
   }

   private boolean validateSibling(OperationId var1, boolean var2) {
      Operation var3 = this.lookup.getEntity(var1, Operation.class);
      if (var3 == null) {
         return false;
      } else {
         OperationType var4 = var3.getOperationType();
         if (var4 != TransformationOperationType.TYPE && var4 != WebServiceOperationType.TYPE) {
            return false;
         } else if (this.isFirstTransformationCorrect(var3)) {
            return var2 ? this.checkLastTransformation(var3) : true;
         } else {
            return false;
         }
      }
   }

   private boolean isFirstTransformationCorrect(Operation var1) {
      OperationPipeline var2 = var1.getPipeline();
      Transformation var3 = this.getFirstTransformation(var2);
      return var3 != null ? this.checkInputStructure(var3) : false;
   }

   private boolean checkInputStructure(Transformation var1) {
      DataStructure var2 = var1.getInputStructure();
      return var2 instanceof WebServiceRequestStructure && this.checkWsCall((WebServiceRequestStructure)var2);
   }

   private boolean checkLastTransformation(Operation var1) {
      if (var1 == null) {
         return false;
      } else {
         OperationType var2 = var1.getOperationType();
         if (var2 == TransformationOperationType.TYPE) {
            return this.checkLastTransformationInTransformationTypeOperation(var1);
         } else {
            return var2 == WebServiceOperationType.TYPE ? this.checkLastTransformationInWebServiceTypeOperation(var1) : false;
         }
      }
   }

   private boolean checkLastTransformationInTransformationTypeOperation(Operation var1) {
      Transformation var2 = this.getFirstTransformation(var1.getPipeline());
      return this.checkLastTransformation(var2);
   }

   private boolean checkLastTransformationInWebServiceTypeOperation(Operation var1) {
      Transformation var2 = this.getLastTransformation(var1.getPipeline());
      return this.checkLastTransformation(var2);
   }

   private boolean checkLastTransformation(Transformation var1) {
      if (var1 == null) {
         return false;
      }

      DataStructure var2 = var1.getOutputStructure();
      return var2 instanceof WebServiceResponseStructure && this.checkWsCall((WebServiceResponseStructure)var2);
   }

   private boolean checkWsCall(WebServiceStructure var1) {
      return var1.getFunctionCallId().equals(this.wsCall.getID());
   }

   private Transformation getFirstTransformation(OperationPipeline var1) {
      TransformationActivity var2 = var1.getFirstActivityOfType(TransformationActivity.class);
      return getTransformation(var2);
   }

   private Transformation getLastTransformation(OperationPipeline var1) {
      TransformationActivity var2 = var1.getLastActivityOfType(TransformationActivity.class);
      return getTransformation(var2);
   }

   private static Transformation getTransformation(TransformationActivity var0) {
      return var0 == null ? null : var0.getContent();
   }
}
