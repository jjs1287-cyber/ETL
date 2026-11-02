package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ActivityContentProvider;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class SelectExistingActivityContentAction<E extends IntegrationEntity> extends AbstractAction {
   private final ContentProviderGate gate;
   private final ActivityContentProvider<E> provider;

   public SelectExistingActivityContentAction(ContentProviderGate var1, PipelineActivity<E> var2, ActivityContentProvider<E> var3) {
      super(Strings.format("SelectExisting.Name", var2.getType().getDisplayName()));
      this.gate = var1;
      this.provider = var3;
      this.setEnabled(!var2.isReadOnly());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.gate.isOpen()) {
         this.provider.selectExisting();
      }
   }
}
