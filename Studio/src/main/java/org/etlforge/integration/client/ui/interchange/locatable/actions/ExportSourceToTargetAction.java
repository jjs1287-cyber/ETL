package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.interchange.entity.SourceToTargetFactory;
import org.etlforge.integration.data.entity.Source;

public final class ExportSourceToTargetAction extends AbstractSingleActiveObjectAction<Source> {
   public static final String ID = "action.ic.SourceToTarget";
   private final UiEntityFactory factory;

   public ExportSourceToTargetAction(ActiveObjectService var1, UiEntityFactory var2) {
      super(var1, "action.ic.SourceToTarget", LocatableActionDictionary.DICTIONARY, Source.class);
      this.factory = var2;
      this.setEnabled(true);
   }

   protected void runOnObject(final Source var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExportSourceToTargetAction.this.doExport(var1);
         }
      });
   }

   private void doExport(Source var1) {
      SourceToTargetFactory var2 = new SourceToTargetFactory(this.factory);
      var2.doTransform(var1);
   }
}
