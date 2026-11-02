package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;

public final class SetSystemTransformationAction extends AbstractAction {
   private final SystemTransformation tx;
   private final OperationGraphController controller;
   private final OperationWrapper op;
   private final TransformationActivity activity;

   public SetSystemTransformationAction(
      String var1, SystemTransformation var2, OperationGraphController var3, OperationWrapper var4, TransformationActivity var5
   ) {
      super(var1);
      this.tx = var2;
      this.controller = var3;
      this.op = var4;
      this.activity = var5;
      this.setEnabled(var5.getContent() != var2);
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      this.controller.setActivityNodeContent(this.op, this.activity, this.tx);
   }
}
