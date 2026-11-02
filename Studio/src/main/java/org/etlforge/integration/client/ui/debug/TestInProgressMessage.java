package org.etlforge.integration.client.ui.debug;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.debug.action.DebugActionDictionary;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;

final class TestInProgressMessage {
   private final ActiveSessionDescription sessionDescription;
   private final ActionListener continueHandler;
   private final ActionListener stopHandler;

   public TestInProgressMessage(DebugSession var1, IntegrationEntityLookup var2, ActionListener var3, ActionListener var4) {
      this.sessionDescription = new ActiveSessionDescription(var1, var2);
      this.continueHandler = var3;
      this.stopHandler = var4;
   }

   public void display() {
      KongaDialog var1 = this.createDialog();
      var1.setVisible(true);
   }

   private KongaDialog createDialog() {
      KongaDialog var1 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Test In Progress", true);
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      this.layoutMessage(var2);
      Action var3 = this.createActionWrapper("action.debug.Stop", this.stopHandler, var1);
      BoxBuilder var4 = this.layoutButtons(var1, var3);
      var2.south(var4);
      var2.withBorder(Empty.border(10));
      var2.asContentPaneFor(var1);
      var1.pack();
      var1.setLocationRelativeTo(null);
      var1.setResizable(false);
      var1.manageLocation();
      var1.setDefaultButton(var3);
      return var1;
   }

   private BoxBuilder layoutButtons(KongaDialog var1, Action var2) {
      Action var3 = this.createActionWrapper("action.debug.Resume", this.continueHandler, var1);
      var2.putValue("Name", "Stop Current Test and Continue With New Test");
      JButton var4 = var1.createCancelButton();
      BoxBuilder var5 = Horizontal.box().withSmallSizedIconsForActions();
      var5.addAllWithSpace(10, var2, var3).glue().add(var4);
      return var5;
   }

   private void layoutMessage(UiCanvas var1) {
      JLabel var2 = new JLabel("Another test is running.");
      var2.setIcon(CommonIcons.WARNING_24);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 5);
      var3.northToSouth(var2, this.sessionDescription, "What do you want to do?");
      var3.decorate(var1);
   }

   private Action createActionWrapper(String var1, ActionListener var2, KongaDialog var3) {
      TestInProgressMessage.ActionWrapper var4 = new TestInProgressMessage.ActionWrapper(var3, var2);
      ActionDecorators.decorateFromDictionary(var4, DebugActionDictionary.DICTIONARY, var1);
      return var4;
   }

   private static class ActionWrapper extends AbstractAction {
      private final KongaDialog dialog;
      private final ActionListener handler;

      public ActionWrapper(KongaDialog var1, ActionListener var2) {
         this.dialog = var1;
         this.handler = var2;
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ActionWrapper.this.closeDialogAndInvokeHandler(var1);
            }
         });
      }

      private void closeDialogAndInvokeHandler(ActionEvent var1) {
         this.dialog.removeFromLocationManager();
         this.dialog.setVisible(false);
         this.handler.actionPerformed(var1);
      }
   }
}
