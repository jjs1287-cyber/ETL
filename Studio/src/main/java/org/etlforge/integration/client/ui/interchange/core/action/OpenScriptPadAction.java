package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.debug.InterchangeDebugService;
import org.etlforge.integration.client.ui.interchange.entity.script.ScriptPad;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUiFactory;
import org.etlforge.integration.data.project.IntegrationProject;

public final class OpenScriptPadAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.ScriptPad";
   private final ApplicationWindow appWin;
   private final InterchangeDebugService debugService;
   private final ScriptBuilderUiFactory uiFactory;
   private ScriptPad scriptPad;

   public OpenScriptPadAction(ApplicationWindow var1, InterchangeDebugService var2, ScriptBuilderUiFactory var3, CurrentProjectService var4) {
      super(var4, "action.ic.ScriptPad");
      this.appWin = var1;
      this.debugService = var2;
      this.uiFactory = var3;
   }

   @Override
   public void execute(ActionEvent var1) {
      new BusyWorker(this.appWin, new Runnable() {
         @Override
         public void run() {
            OpenScriptPadAction.this.openPad();
         }
      }).run();
   }

   private void openPad() {
      if (this.scriptPad == null) {
         this.scriptPad = new ScriptPad(this.appWin.getEditorService(), this.getProject(), this.uiFactory, this.debugService);
      }

      this.scriptPad.open();
   }

   @Override
   protected void onProjectClosed(IntegrationProject var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenScriptPadAction.this.closePad();
         }
      });
   }

   private void closePad() {
      if (this.scriptPad != null) {
         this.scriptPad.close();
         this.scriptPad = null;
      }
   }
}
