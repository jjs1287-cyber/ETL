package org.etlforge.integration.client.ui.interchange.entity.script.page;

import javax.swing.Action;
import javax.swing.JButton;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.debug.TestResultDisplayer;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.util.Interval;

public final class ScriptPage extends IntegrationEntityPage implements TestResultDisplayer {
   private final ScriptBuilderUi scriptBuilder;

   public ScriptPage(Script var1, ScriptBuilderUi var2) {
      super(new IntegrationEntityPageModel(var1));
      this.scriptBuilder = var2;
      var2.setPathChangeReaction(new PathChangeReaction(this));
      this.setHelpId("integration.script.main");
      this.setUseScroll(false);
   }

   public Script getObject() {
      return (Script)super.getObject();
   }

   protected ScriptPanel createDataPanel() {
      ScriptPanel var1 = new ScriptPanel(this.getWindow(), this.getObject(), this.scriptBuilder);
      var1.setSettings(this.getSettingsForScript());
      Action var2 = this.scriptBuilder.getFindAction();
      this.addToolbarButton(forToolbar(var2));
      Action var3 = this.getTestAction();
      this.addToolbarButton(forToolbar(var3));
      return var1;
   }

   private EntitySettings getSettingsForScript() {
      ManagedProject var1 = ProjectUtils.getManagedProject(this.getObject());
      return var1.getEntityResources().getSettings();
   }

   private static JButton forToolbar(Action var0) {
      KongaButton var1 = KongaButton.forToolbar(var0);
      if (USE_MEDIUM_SIZED_BUTTONS_IN_TOOLBAR) {
         var1.setUseMediumActionIcon(true);
      } else {
         var1.setUseSmallActionIcon(true);
      }

      return var1;
   }

   protected ScriptPanel getDetailsPanel() {
      return (ScriptPanel)super.getDetailsPanel();
   }

   @Override
   public void showTestResult(TestResult var1) {
      ApplicationWindow var2 = this.getWindow();
      if (var2 != null) {
         var2.getEditorService().openEditor(this);
         this.scriptBuilder.showTestResult(var1);
      }
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.getPageModel().bindProperty(new ExpressionProperty(this));
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      boolean var2 = super.removeFromWindow(var1);
      if (var2 && !this.isDirty()) {
         this.getDetailsPanel().storeCurrentCaretPosition();
      }

      return var2;
   }

   public void highlightText(Interval var1) {
      this.scriptBuilder.highlightText(var1);
   }

   public Action getTestAction() {
      return this.scriptBuilder.getTestAction();
   }

   public ScriptBuilderUi getScriptBuilder() {
      return this.scriptBuilder;
   }

   void resetExpression(String var1) {
      this.scriptBuilder.setExpression(var1);
      this.getUndoService().discardAllEdits();
      this.setClean();
   }
}
