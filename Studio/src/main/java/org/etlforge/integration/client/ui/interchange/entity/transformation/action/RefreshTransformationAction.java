package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.mapping.MappingStructureLog;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPageRefresher;

public final class RefreshTransformationAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.Refresh";
   private final TransformationPage page;

   public RefreshTransformationAction(TransformationPage var1) {
      super("action.ic.tx.Refresh", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      this.setEnabled(false);
      final RefreshTransformationAction.RefresherImpl var2 = new RefreshTransformationAction.RefresherImpl();
      var2.setLogger(MappingStructureLog.LOG);
      Runnable var3 = new Runnable() {
         @Override
         public void run() {
            var2.refresh();
         }
      };
      KongaSwingWorker.runWhileWaiting(var3, this.page.getWaitService());
   }

   private final class RefresherImpl extends TransformationPageRefresher {
      public RefresherImpl() {
         super(RefreshTransformationAction.this.page);
      }

      @Override
      protected void whenDone() {
         this.getPage().setDirty();
         this.enableAction();
      }

      private void enableAction() {
         RefreshTransformationAction.this.setEnabled(true);
      }
   }
}
