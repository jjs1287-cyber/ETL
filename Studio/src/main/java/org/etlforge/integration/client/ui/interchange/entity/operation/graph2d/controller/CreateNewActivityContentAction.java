package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ActivityContentProvider;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class CreateNewActivityContentAction<E extends IntegrationEntity> extends AbstractAction {
   private final ContentProviderGate gate;
   private final ActivityContentProvider<E> provider;

   public CreateNewActivityContentAction(PipelineActivity<E> var1, ActivityContentProvider<E> var2) {
      this(null, var1, var2);
   }

   public CreateNewActivityContentAction(ContentProviderGate var1, PipelineActivity<E> var2, ActivityContentProvider<E> var3) {
      super(Strings.format("CreateNew.Name", var2.getType().getDisplayName()));
      this.gate = var1;
      this.provider = var3;
      this.setEnabled(!var2.isReadOnly() && var3.isCreateNewSupported());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (CreateNewActivityContentAction.this.gate == null || CreateNewActivityContentAction.this.gate.isOpen()) {
               CreateNewActivityContentAction.this.provider.createNew();
            }
         }
      });
   }
}
