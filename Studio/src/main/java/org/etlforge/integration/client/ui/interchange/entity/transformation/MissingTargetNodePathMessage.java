package org.etlforge.integration.client.ui.interchange.entity.transformation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JButton;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.debug.action.ResumeDebuggingAction;
import org.etlforge.integration.client.ui.debug.action.StopDebuggingAction;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;

final class MissingTargetNodePathMessage {
   private final ResumeDebuggingAction resumeAction;
   private final StopDebuggingAction stopAction;

   public MissingTargetNodePathMessage(DebugService var1) {
      this.resumeAction = new ResumeDebuggingAction(var1);
      this.stopAction = new StopDebuggingAction(var1);
   }

   public void open() {
      KongaDialog var1 = this.createDialog();
      var1.setVisible(true);
      this.dispose();
   }

   private KongaDialog createDialog() {
      JButton var1 = createButton(this.resumeAction);
      JButton var2 = createButton(this.stopAction);
      KongaDialog var3 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Missing Break-Location Path", true);
      layoutDialog(var3, var1, var2);
      configureDebugActions(var3, var1, var2);
      var3.setLocationRelativeTo(null);
      var3.getRootPane().setDefaultButton(var1);
      return var3;
   }

   private static JButton createButton(Action var0) {
      return new KongaButton(var0).withSmallActionIcon();
   }

   private static void layoutDialog(KongaDialog var0, JButton var1, JButton var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(10, 10);
      var3.center("The test stopped at a breakpoint, but the server did not provide the location of the breakpoint. This is a bug.");
      var3.west(CommonIcons.ERROR_24);
      var3.south(layoutButtons(var1, var2, var0.createCloseButton()));
      var3.withEmptyBorder(10).asContentPaneFor(var0);
      var0.pack();
      var0.setResizable(false);
   }

   private static UiProvider layoutButtons(JButton var0, JButton var1, JButton var2) {
      BoxBuilder var3 = BoxBuilder.horizontal();
      var3.addAllWithSpace(10, var0, var1).glue().add(var2);
      return var3;
   }

   private static void configureDebugActions(final KongaDialog var0, JButton... var1) {
      ActionListener var2 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            var0.setVisible(false);
         }
      };
      ButtonUtils.addActionListener(var2, var1);
   }

   private void dispose() {
      this.resumeAction.dispose();
      this.stopAction.dispose();
   }
}
