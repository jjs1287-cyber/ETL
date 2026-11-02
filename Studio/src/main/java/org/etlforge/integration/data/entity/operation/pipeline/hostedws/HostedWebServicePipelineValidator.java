package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.HostedWebServiceForwardingValidator;
import org.etlforge.integration.data.entity.operation.ValidationUtils;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class HostedWebServicePipelineValidator extends OperationPipelineValidator {
   private final HostedWebServicePipelineBrowser browser;
   private WebServiceCall wsCall;

   public HostedWebServicePipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      super(var1, var2, var3);
      this.browser = new HostedWebServicePipelineBrowser(var2);
   }

   @Override
   public void validate() {
      this.validateWsCall();
      if (this.wsCall != null) {
         this.validateTransformations();
      }
   }

   private void validateWsCall() {
      this.wsCall = this.browser.getWebServiceCall();
      if (this.wsCall == null) {
         this.fatal("No web service call specified");
      }
   }

   private void validateTransformations() {
      Transformation var1 = this.browser.getFirstTransformation();
      Transformation var2 = this.browser.getSecondTransformation();
      if (var1 != null) {
         if (var2 != null && var2 != var1) {
            this.validateDoubleTransformation(var1, var2);
         } else {
            this.validateSingleTransformation(var1);
         }
      } else if (var2 == null) {
         this.validateCallForwarding();
      } else {
         this.fatal("The first transformation is not specified");
      }
   }

   private void validateSingleTransformation(Transformation var1) {
      if (!this.isCompleteImplementation(var1)) {
         this.fatal("The transformation must take the WS request as input and produce the WS response as output.");
      }
   }

   private boolean isCompleteImplementation(Transformation var1) {
      return this.wsCall.getInputStructure().equals(var1.getInputStructure()) && this.wsCall.getOutputStructure().equals(var1.getOutputStructure());
   }

   private void validateDoubleTransformation(Transformation var1, Transformation var2) {
      DataStructure var3 = var1.getInputStructure();
      if (!this.wsCall.getInputStructure().equals(var3)) {
         this.fatal("The first transformation must take the WS request as input");
      } else {
         DataStructure var4 = var2.getOutputStructure();
         if (!this.wsCall.getOutputStructure().equals(var4)) {
            this.fatal("The second transformation must produce the WS response as output");
         } else {
            this.checkTransformationJoin(var1, var2);
         }
      }
   }

   private void checkTransformationJoin(Transformation var1, Transformation var2) {
      DataStructure var3 = var2.getInputStructure();
      DataStructureType var4 = var3.getStructureType();
      if (var4 != DataStructureType.None && var4 != DataStructureType.Database && !var2.acceptsOutputFrom(var1)) {
         this.warning(ValidationUtils.getIllegalFunctionJoinMessage());
      }
   }

   private void validateCallForwarding() {
      HostedWebServiceForwardingValidator var1 = new HostedWebServiceForwardingValidator(this.wsCall, this.operation, this.collector.context().getDataStore());
      if (!var1.isValid()) {
         this.fatal(ValidationUtils.getNoFunctionsMessage(this.operation));
      }
   }
}
