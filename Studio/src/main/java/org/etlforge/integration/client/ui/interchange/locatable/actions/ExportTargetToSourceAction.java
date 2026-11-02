package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.interchange.entity.TargetToSourceFactory;
import org.etlforge.integration.data.entity.Target;

public final class ExportTargetToSourceAction extends AbstractSingleActiveObjectAction<Target> {
   public static final String ID = "action.ic.TargetToSource";
   private final UiEntityFactory factory;

   public ExportTargetToSourceAction(ActiveObjectService var1, UiEntityFactory var2) {
      super(var1, "action.ic.TargetToSource", LocatableActionDictionary.DICTIONARY, Target.class);
      this.factory = var2;
      this.setEnabled(true);
   }

   protected void runOnObject(final Target var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExportTargetToSourceAction.this.doExport(var1);
         }
      });
   }

   private void doExport(Target var1) {
      TargetToSourceFactory var2 = new TargetToSourceFactory(this.factory);
      var2.doTransform(var1);
   }
}
