package org.etlforge.integration.client.ui.interchange.entity.script.page;

import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.cli.CommandFactory;
import org.etlforge.cli.ui.ActionCommand;
import org.etlforge.integration.client.ui.debug.action.DebugActions;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.debug.InterchangeDebugService;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class ScriptPageConfig extends DefaultPageConfig {
   private final InterchangeDebugService debugService;
   private final DebugActions debugActions;

   public ScriptPageConfig(ApplicationWindow var1, ProjectPersistor var2, InterchangeDebugService var3, DebugActions var4) {
      super(var1, var2);
      this.debugService = var3;
      this.debugActions = var4;
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      this.installTestResultDisplayer((ScriptPage)var1);
      this.addCommands((ScriptPage)var1);
   }

   private void installTestResultDisplayer(final ScriptPage var1) {
      var1.addResource(new AbstractWindowElementResource() {
         @Override
         public void create(WindowElement var1x) {
            ScriptId var2 = var1.getObject().getID();
            ScriptPageConfig.this.debugService.addTestResultDisplayer(var2, var1);
         }

         @Override
         public void dispose(WindowElement var1x) {
            ScriptId var2 = var1.getObject().getID();
            ScriptPageConfig.this.debugService.removeTestResultDisplayer(var2, var1);
         }
      });
   }

   private void addCommands(ScriptPage var1) {
      this.addCommand(CommandFactory.newCommand("test", new TestScriptExecutor(var1)));
      this.addCommand(new ActionCommand("resume", this.debugActions.getResumeAction()));
      this.addCommand(new ActionCommand("stop", this.debugActions.getStopAction()));
      this.addCommand(new ActionCommand("step", this.debugActions.getStepAction()));
   }
}
