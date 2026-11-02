package org.etlforge.integration.client.ui.script.builder;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.script.builder.dataelement.DataElementViewer;
import org.etlforge.integration.client.ui.script.debug.DebugConsole;
import org.etlforge.integration.client.ui.script.debug.ScriptDebugModel;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class DefaultScriptTestUi implements ScriptTestUi {
   private final DebugConsole console;
   private final ScriptDebugModel model;
   private final DataElementViewer deViewer;
   private final DefaultScriptTestUi.ResetDataElementsAction resetAction;
   private final KongaTextArea testArea;

   public DefaultScriptTestUi(DebugService var1, IntegrationEntity var2, Iterable<Action> var3) {
      this.model = new ScriptDebugModel(var1, var2.getID());
      this.console = new DebugConsole(this.model, var3);
      this.deViewer = new DataElementViewer(this.model.getDataElementsModel());
      this.deViewer.setSourceDataElementValuesEditable(this.model.areSourceDataElementValuesEditable());
      this.resetAction = new DefaultScriptTestUi.ResetDataElementsAction();
      this.testArea = KongaTextArea.viewer(Size.rows(10), Size.columns(50));
      this.model.addPropertyChangeListener(new DefaultScriptTestUi.ModelListener());
   }

   @Override
   public ScriptDebugModel getModel() {
      return this.model;
   }

   @Override
   public void dispose() {
      this.model.dispose();
      this.console.dispose();
   }

   @Override
   public boolean isTestEnabled() {
      return !this.model.getDebugService().isDebugging();
   }

   @Override
   public void startTest() {
      this.console.startTest();
   }

   @Override
   public void setItemLookup(IntegrationEntityLookup var1) {
      this.console.setItemLookup(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.console).center(this.createSplitPane());
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public void displayResult(TestResult var1) {
      BreakLocation var2 = var1.getCurrentLocation();
      this.model.setLineNumber(var2 != null ? var2.getLineNumber() : -1);
      String var3 = var1.getResult();
      if (var3 == null || var3.isEmpty()) {
         var3 = var1.getErrors();
      }

      this.model.setDebugMessage(var3);
   }

   private void showMessage(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DefaultScriptTestUi.this.testArea.setText(var1);
            DefaultScriptTestUi.this.testArea.setCaretPosition(0);
         }
      });
   }

   private KongaSplitPane createSplitPane() {
      KongaSplitPane var1 = KongaSplitPane.horizontalSplit(this.layoutDataElementsViewer(), this.layoutResultArea());
      var1.setDividerLocation(0.5);
      var1.setResizeWeight(0.5);
      return var1;
   }

   private JComponent layoutResultArea() {
      this.testArea.setLabel("Result:");
      return new KongaScrollPane(this.testArea.asUiProvider());
   }

   private JComponent layoutDataElementsViewer() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 3);
      JComponent var2 = DataElementViewer.getDefaultCaption();
      BoxBuilder var3 = BoxBuilder.horizontal().add(var2).glue().add(this.resetAction);
      var1.north(var3).center(this.deViewer);
      JComponent var4 = var1.container();
      var4.setMinimumSize(new Dimension(0, 0));
      return var4;
   }

   private final class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if ("debugMessage".equals(var2)) {
            DefaultScriptTestUi.this.showMessage((String)var1.getNewValue());
         } else if ("testProviderId".equals(var2)) {
            DefaultScriptTestUi.this.deViewer.setSourceDataElementValuesEditable(DefaultScriptTestUi.this.model.areSourceDataElementValuesEditable());
         }
      }
   }

   private final class ResetDataElementsAction extends AbstractAction {
      public ResetDataElementsAction() {
         super("Reset");
         this.putValue("ShortDescription", "Prepare for a new test by resetting the data element list");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         DefaultScriptTestUi.this.model.resetDataElements();
      }
   }
}
