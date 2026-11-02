package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import java.awt.dnd.DropTargetDragEvent;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.TransformationActivityNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;

public final class WebServiceTransformationDropBehavior extends PipelineActivityDropBehavior {
   private final WebServicePipelineBrowser browser;

   public WebServiceTransformationDropBehavior(OperationGraphController var1, OperationWrapperNode var2, TransformationActivityNode var3) {
      super(var1, var3);
      OperationPipeline var4 = var2.getPipeline();
      if (var4.getType() != WebServiceOperationType.TYPE) {
         throw new IllegalArgumentException("opNode must represent a web service call");
      }

      this.browser = new WebServicePipelineBrowser(var4);
   }

   @Override
   public boolean isDropAllowed(DropTargetDragEvent var1) {
      IntegrationEntity var2 = this.getDraggedEntity(var1);
      if (var2 instanceof Transformation) {
         Transformation var3 = (Transformation)var2;
         TransformationActivityNode var4 = (TransformationActivityNode)this.getNode();
         if (this.browser.getRequestActivity() == var4.getDataObject()) {
            return var3.getOutputStructure() instanceof WebServiceRequestStructure;
         }

         if (this.browser.getResponseActivity() == var4.getDataObject()) {
            return var3.getInputStructure() instanceof WebServiceResponseStructure;
         }
      }

      return false;
   }
}
