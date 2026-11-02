package org.etlforge.integration.client.ui.interchange.entity.script;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.interchange.debug.InterchangeDebugService;
import org.etlforge.integration.client.ui.interchange.debug.TestResultDisplayer;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUiFactory;
import org.etlforge.integration.client.ui.script.text.VariableCompletionContext;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.de.DefaultGlobalDataElements;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.debug.client.TestResult;

public final class ScriptPad {
   private final IntegrationProject project;
   private final ScriptBuilderUiFactory uiFactory;
   private final InterchangeDebugService interchangeDebugService;
   private final Script script;
   private ScriptBuilderUi scriptBuilder;
   private String latestExpression;
   private int latestCaretPosition;
   private final ScriptPadDisplayer displayer;
   private final TestResultDisplayer testResultDisplayer;

   public ScriptPad(EditorService var1, IntegrationProject var2, ScriptBuilderUiFactory var3, InterchangeDebugService var4) {
      this.project = var2;
      this.uiFactory = var3;
      this.interchangeDebugService = var4;
      this.displayer = new ScriptPadEditorDisplayer(var1);
      this.script = new Script("__throw_away__");
      this.testResultDisplayer = new ScriptPad.TestResultHandler();
   }

   public ScriptBuilderUi getUi() {
      return this.scriptBuilder;
   }

   public void open() {
      if (this.scriptBuilder == null) {
         DefaultGlobalDataElements var1 = new DefaultGlobalDataElements();
         this.scriptBuilder = this.createScriptBuilder(var1);
      }

      this.displayer.open(this);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (ScriptPad.this.latestExpression != null) {
               ScriptPad.this.scriptBuilder.setCaretPosition(ScriptPad.this.latestCaretPosition);
            }

            ScriptPad.this.scriptBuilder.requestFocus();
         }
      });
   }

   private ScriptBuilderUi createScriptBuilder(GlobalDataElements var1) {
      this.interchangeDebugService.addTestResultDisplayer(this.script.getID(), this.testResultDisplayer);
      ScriptBuilderUi var2 = this.uiFactory.newScriptBuilder(this.script, this.project);
      this.configureCompletionContext(var1, var2);
      if (this.latestExpression != null) {
         var2.setExpression(this.latestExpression);
      }

      var2.getDebugUi().getModel().setDebugMessage("");
      var2.setPostDisplayDelay(6);
      return var2;
   }

   private void configureCompletionContext(GlobalDataElements var1, ScriptBuilderUi var2) {
      VariableCompletionContext var3 = new VariableCompletionContext();
      var3.setGlobalDataElements(var1);
      var2.addAdditionalCompletionContext(var3);
   }

   public void close() {
      this.displayer.close();
   }

   void onPadClosed() {
      this.latestExpression = this.scriptBuilder.getExpression();
      this.latestCaretPosition = this.scriptBuilder.getCaretPosition();
      this.interchangeDebugService.removeTestResultDisplayer(this.script.getID(), this.testResultDisplayer);
      this.scriptBuilder.dispose();
      this.scriptBuilder = null;
   }

   private class TestResultHandler implements TestResultDisplayer {
      private TestResultHandler() {
      }

      @Override
      public void showTestResult(TestResult var1) {
         if (ScriptPad.this.scriptBuilder != null) {
            ScriptPad.this.displayer.requestAttention();
            ScriptPad.this.scriptBuilder.showTestResult(var1);
         }
      }
   }
}
