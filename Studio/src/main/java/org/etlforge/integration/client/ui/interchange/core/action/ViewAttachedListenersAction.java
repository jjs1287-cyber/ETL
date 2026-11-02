package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import javax.swing.JComponent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.entity.ListenerReportTree;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityLabel;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;

public final class ViewAttachedListenersAction extends AbstractSingleActiveObjectAction<IntegrationEntity> {
   public static final String ID = "action.ic.AttachedListeners";
   private final ApplicationWindow appWin;

   public ViewAttachedListenersAction(ApplicationWindow var1) {
      super(var1.getActiveObjectService(), "action.ic.AttachedListeners", InterchangeActionDictionary.DICTIONARY, IntegrationEntity.class);
      this.appWin = var1;
   }

   protected void runOnObject(final IntegrationEntity var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewAttachedListenersAction.this.viewListeners(var1);
         }
      });
   }

   private void viewListeners(IntegrationEntity var1) {
      JComponent var2 = this.createCaption(var1);
      ListenerReportTree var3 = new ListenerReportTree(var1);
      BorderLayoutBuilder var4 = new BorderLayoutBuilder(0, 3).north(var2).center(var3);
      UiProviderDialogDisplayer.display(this.appWin.getFrame(), var4, "Attached Listeners");
   }

   private JComponent createCaption(IntegrationEntity var1) {
      return Horizontal.box().of("The following listeners are attached to  ", new IntegrationEntityLabel(var1)).container();
   }
}
