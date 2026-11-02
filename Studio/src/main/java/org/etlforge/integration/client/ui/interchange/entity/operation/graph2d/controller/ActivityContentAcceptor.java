package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.MutablePipelineActivity;
import org.etlforge.system.callback.Receiver;

public final class ActivityContentAcceptor<E extends IntegrationEntity> implements Receiver<E> {
   private final OperationGraphController controller;
   private final OperationWrapper op;
   private final MutablePipelineActivity<E> activity;

   public static <E extends IntegrationEntity> ActivityContentAcceptor<E> create(
      OperationGraphController var0, OperationWrapper var1, MutablePipelineActivity<E> var2
   ) {
      return new ActivityContentAcceptor<>(var0, var1, var2);
   }

   private ActivityContentAcceptor(OperationGraphController var1, OperationWrapper var2, MutablePipelineActivity<E> var3) {
      this.controller = var1;
      this.op = var2;
      this.activity = var3;
   }

   public void handle(E var1) {
      if (var1 != null) {
         this.controller.setActivityNodeContent(this.op, this.activity, (E)var1);
      }
   }
}
