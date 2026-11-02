package org.etlforge.integration.client.ui.script.builder;

import com.google.common.collect.ImmutableList;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutFactory;
import org.etlforge.integration.client.ui.script.builder.layout.ScriptBuilderLayout;
import org.etlforge.integration.client.ui.script.debug.ScriptDebugModel;
import org.etlforge.integration.data.entity.script.CachedPathsExpressionTranslator;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.serverapi.ServerTestCallbackAdapter;
import org.etlforge.integration.debug.serverapi.TestScriptProvider;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.LineMarker;
import org.etlforge.util.Interval;

public final class ScriptBuilderUi implements UiProvider, Disposable {
   public static final KeyStroke TEST_SCRIPT_SHORTCUT = KeyUtils.F11;
   private final ScriptBuilderModel model;
   private final ExpressionArea expressionArea;
   private final ExpressionTranslator translator;
   private final ExpressionAreaStatusPanel statusPanel;
   private final TokenInserter tokenInserter;
   private ImmutableList<ScriptBuilderSection> sections = ImmutableList.of();
   private final Action testAction;
   private ScriptTestUi debugUi;
   private final ScriptBuilderLayout layout;
   private final PathChangeHandler pathChangeHandler;
   private final DisposableCollection disposables;
   private final FunctionArgumentSuggestions functionArgumentSuggestions;
   private final RegistryBasedFunctionInserterLookup functionInserterLookup;
   private UiProvider sourceTree;

   public ScriptBuilderUi(ScriptBuilderModel var1, ScriptModeSelector var2) {
      this.model = var1;
      this.testAction = new ScriptBuilderUi.TestAction();
      this.functionArgumentSuggestions = new FunctionArgumentSuggestions();
      this.functionInserterLookup = this.createFunctionInserterLookup();
      this.expressionArea = this.createExpressionArea();
      this.tokenInserter = new TokenInserter(this.expressionArea);
      this.translator = new CachedPathsExpressionTranslator(var1);
      var2.setScriptBuilder(this);
      this.statusPanel = new ExpressionAreaStatusPanel(this.expressionArea, var2);
      this.statusPanel.setModel(var1);
      this.layout = LayoutFactory.createPreferredLayout(this.expressionArea, this.statusPanel);
      this.pathChangeHandler = new PathChangeHandler(this.expressionArea);
      var1.addGuidPathCacheListener(this.pathChangeHandler);
      this.disposables = new DisposableCollection(var1);
   }

   private RegistryBasedFunctionInserterLookup createFunctionInserterLookup() {
      IntegrationProject var1 = this.model.getProject();
      return new RegistryBasedFunctionInserterLookup(var1, this.functionArgumentSuggestions);
   }

   private ExpressionArea createExpressionArea() {
      ExpressionAreaFactory var1 = new ExpressionAreaFactory();
      var1.setFunctionArgumentSuggestions(this.functionArgumentSuggestions);
      var1.setFunctionInserterLookup(this.functionInserterLookup);
      var1.setProject(this.model.getProject());
      var1.setTestAction(this.testAction);
      ExpressionArea var2 = var1.newExpressionArea();
      var2.getDocument().addDocumentListener(new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            ScriptBuilderUi.this.updateTestActionStatus();
         }
      });
      return var2;
   }

   private void updateTestActionStatus() {
      boolean var1 = this.debugUi != null && this.expressionArea.getDocument().getLength() > 0 && this.debugUi.isTestEnabled();
      this.testAction.setEnabled(var1);
   }

   public ScriptBuilderModel getModel() {
      return this.model;
   }

   public void setMode(ScriptBuilderMode var1) {
      this.model.setMode(var1);
      this.functionInserterLookup.setMode(var1);

      for (ScriptBuilderSection var3 : this.sections) {
         var3.setMode(var1);
      }
   }

   public void setSourceTree(UiProvider var1) {
      this.sourceTree = var1;
   }

   public void setDebugUi(ScriptTestUi var1) {
      this.debugUi = var1;
      this.disposables.add(var1);
      this.expressionArea.setScriptDataElements(var1.getModel().getDataElementsModel());
      var1.setItemLookup(this.model.getProject().getItemLookup());
      this.prepareDebugModel(var1.getModel());
      this.provideFunctionInserterWithTester(var1);
      this.updateTestActionStatus();
   }

   private void provideFunctionInserterWithTester(ScriptTestUi var1) {
      TestScriptProvider var2 = this.getTestProvider();
      ScriptDebugModel var3 = var1.getModel();
      DefaultFunctionTester var4 = new DefaultFunctionTester(var2, var3, this.translator);
      this.functionInserterLookup.setFunctionTester(var4);
      ExpressionParser var5 = new ExpressionParser(this.model.getSource());
      this.functionInserterLookup.setExpressionParser(var5);
   }

   private TestScriptProvider getTestProvider() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      return var1.getServerCall(TestScriptProvider.class);
   }

   public ScriptTestUi getDebugUi() {
      return this.debugUi;
   }

   public void setSections(Iterable<? extends ScriptBuilderSection> var1) {
      this.sections = ImmutableList.copyOf(var1);

      for (ScriptBuilderSection var3 : var1) {
         this.registerSection(var3);
      }
   }

   private void registerSection(ScriptBuilderSection var1) {
      this.disposables.add(var1);
      var1.collectArgumentSuggestions(this.functionArgumentSuggestions);
      var1.setReceiver(this.tokenInserter);
      var1.setMode(this.model.getMode());
   }

   @Override
   public void dispose() {
      this.disposables.dispose();
   }

   public void installScriptScanner(ExpressionScanner var1) {
      var1.setScriptBuilderModel(this.model);
      if (this.debugUi != null) {
         var1.setDebugModel(this.debugUi.getModel());
      }

      this.expressionArea.installScriptScanner(var1);
   }

   public void addAdditionalCompletionContext(CompletionContext var1) {
      this.expressionArea.addAdditionalCompletionContext(var1);
   }

   public void showTestResult(final TestResult var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (ScriptBuilderUi.this.layout != null) {
               ScriptBuilderUi.this.layout.selectTestAndDebuggingTab();
            }

            if (ScriptBuilderUi.this.debugUi != null) {
               ScriptBuilderUi.this.debugUi.displayResult(var1);
            }
         }
      });
   }

   public void setPostDisplayDelay(int var1) {
      this.layout.setPostDisplayDelay(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout.installTestActionShortcut(this.testAction);
      if (this.sourceTree != null) {
         this.layout.setSourceTree(this.sourceTree);
      }

      for (ScriptBuilderSection var3 : this.sections) {
         this.layout.addSection(var3);
      }

      if (this.debugUi != null) {
         this.layout.setDebugUi(this.debugUi);
      }

      this.layout.decorate(var1);
   }

   @Override
   public void requestFocus() {
      UiUtils.nestedInvokeLater(4, new Runnable() {
         @Override
         public void run() {
            ScriptBuilderUi.this.expressionArea.requestFocus();
         }
      });
   }

   public int getCaretPosition() {
      return this.expressionArea.getCaretPosition();
   }

   public void setCaretPosition(int var1) {
      this.expressionArea.setCaretPosition(var1);
      this.statusPanel.updateRowAndColumnNumbers();
   }

   public void setDefaultCaretPosition() {
      String var1 = this.expressionArea.getText();
      if (var1.equals("<trans>\n\n</trans>")) {
         this.setCaretPosition("<trans>\n\n</trans>".length() / 2);
      } else {
         int var2 = var1.lastIndexOf("</trans>");
         if (var2 > 0) {
            char var3 = var1.charAt(var2 - 1);
            if (var3 == '\n') {
               var2--;
            }
         }

         this.setCaretPosition(var2 >= 0 ? var2 : var1.length());
      }
   }

   public ExpressionArea getExpressionArea() {
      return this.expressionArea;
   }

   public void setExpression(String var1) {
      String var2 = this.translator.toDisplay(var1);
      this.expressionArea.setText(var2);
   }

   public String getExpression() {
      return this.expressionArea.getText();
   }

   public String getTranslatedExpression() {
      return this.translator.fromDisplay(this.getExpression());
   }

   public boolean hasExpression() {
      return this.expressionArea.getDocument().getLength() > 0;
   }

   public void replaceDisplayedExpression(String var1) {
      this.expressionArea.replaceDisplayedExpression(var1);
   }

   public void addExpressionListener(DocumentListener var1) {
      this.expressionArea.getDocument().addDocumentListener(var1);
   }

   public void removeExpressionListener(DocumentListener var1) {
      this.expressionArea.getDocument().removeDocumentListener(var1);
   }

   public Action getTestAction() {
      return this.testAction;
   }

   public Action getFindAction() {
      return this.expressionArea.getFindAction();
   }

   public void setButtonPanel(JComponent var1) {
      this.layout.setButtonPanel(var1);
   }

   public void setPathChangeReaction(Runnable var1) {
      this.pathChangeHandler.setReaction(var1);
   }

   public void highlightText(Interval var1) {
      this.expressionArea.highlightText(var1);
   }

   private void prepareDebugModel(final ScriptDebugModel var1) {
      var1.setProject(this.model.getProject());
      var1.setExpressionArea(this.expressionArea);
      var1.setExpressionTranslator(this.translator);
      var1.setCallback(new ServerTestCallbackAdapter() {
         @Override
         public void cancelled() {
            var1.setDebugMessage("");
         }

         @Override
         public void caught(Throwable var1x) {
            var1x.printStackTrace();
            var1.setDebugMessage(var1x.getMessage());
         }
      });
      var1.addPropertyChangeListener("isStartEnabled", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            ActionUtils.setEnabledSafely(ScriptBuilderUi.this.testAction, (Boolean)var1.getNewValue());
         }
      });
      var1.addPropertyChangeListener("lineNumber", new ScriptBuilderUi.BreakLocationDisplayer());
   }

   private class BreakLocationDisplayer implements PropertyChangeListener {
      private LineMarker currentMarker;

      private BreakLocationDisplayer() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (this.currentMarker != null) {
            ScriptBuilderUi.this.expressionArea.removeLineMarker(this.currentMarker);
            this.currentMarker = null;
         }

         Integer var2 = (Integer)var1.getNewValue();
         if (var2 > 0) {
            this.currentMarker = new BreakLocationMarker(var2, ScriptBuilderUi.this.expressionArea);
            ScriptBuilderUi.this.expressionArea.addLineMarker(this.currentMarker);
            ScriptBuilderUi.this.expressionArea.ensureRowIsVisible(var2);
            ScriptBuilderUi.this.expressionArea.moveCaretToRow(var2);
         }
      }
   }

   private class TestAction extends AbstractAction {
      public TestAction() {
         super(Strings.get("TestAction.Name"), Icons.TEST_ICON_16);
         this.putValue("ShortDescription", Strings.get("TestAction.ToolTip"));
         this.putValue("AcceleratorKey", ScriptBuilderUi.TEST_SCRIPT_SHORTCUT);
         this.putValue("KongaMediumIconKey", Icons.TEST_ICON_24);
         this.putValue("SwingLargeIconKey", Icons.TEST_ICON_32);
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (ScriptBuilderUi.this.layout != null) {
            ScriptBuilderUi.this.layout.selectTestAndDebuggingTab();
         }

         ScriptBuilderUi.this.debugUi.startTest();
      }
   }
}
