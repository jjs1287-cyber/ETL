package com.etlforge.integration.client.ui.script.fcn;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.client.ui.de.global.tree.DataElementsTreeWidget;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.SourceTreeLayout;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferSupport;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.widget.split.KongaSplitPane;

public final class VariableSelector implements UiProvider {
   private final DataElementsTreeWidget globalDataElementsTree;
   private final SourceTreeLayout sourceTreeLayout;
   private Receiver<String> sourceDataElementReceiver;
   private Receiver<String> globalDataElementReceiver;

   public VariableSelector(GlobalDataElements var1, FormulaBuilderSourceTree var2) {
      Preconditions.checkArgument(var1 != null || var2 != null, "Both arguments cannot be null");
      this.globalDataElementsTree = this.createDataElementsTree(var1);
      this.sourceTreeLayout = this.layoutSourceTree(var2);
      this.sourceDataElementReceiver = NoReceiver.create();
      this.globalDataElementReceiver = NoReceiver.create();
   }

   private DataElementsTreeWidget createDataElementsTree(GlobalDataElements var1) {
      if (var1 == null) {
         return null;
      }

      DataElementsTreeWidget var2 = new DataElementsTreeWidget();
      var2.setIncludeBuiltInDataElements(true);
      var2.setVisibleRowCount(4);
      var2.setDataElements(var1);
      var2.setDefaultAction(new AbstractSingleObjectSelectionAction<String>(String.class) {
         protected void run(String var1, ActionEvent var2x) {
            VariableSelector.this.globalDataElementReceiver.handle(var1);
         }
      });
      return var2;
   }

   private SourceTreeLayout layoutSourceTree(FormulaBuilderSourceTree var1) {
      return var1 == null ? null : new SourceTreeLayout(var1, new Receiver<String>() {
         public void handle(String var1) {
            VariableSelector.this.sourceDataElementReceiver.handle(var1);
         }
      });
   }

   public void setSourceDataElementReceiver(Receiver<String> var1) {
      this.sourceDataElementReceiver = var1;
   }

   public void setGlobalDataElementReceiver(Receiver<String> var1) {
      this.globalDataElementReceiver = var1;
   }

   public void setSingleFieldReceiver(final JTextComponent var1) {
      DeTemplateTransferSupport.installDropHandler(var1, "$%s");
      this.setSourceDataElementReceiver(new Receiver<String>() {
         public void handle(String var1x) {
            VariableSelector.this.insertTextIntoField(var1, var1x);
         }
      });
      this.setGlobalDataElementReceiver(new Receiver<String>() {
         public void handle(String var1x) {
            VariableSelector.this.insertTextIntoField(var1, GlobalDE.forScript(var1x));
         }
      });
   }

   private void insertTextIntoField(final JTextComponent var1, final String var2) {
      var1.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.replaceSelection(var2);
         }
      });
   }

   public void expandSourceTree() {
      if (this.sourceTreeLayout != null) {
         this.sourceTreeLayout.expand();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.sourceTreeLayout == null) {
         this.globalDataElementsTree.decorate(var1);
      } else if (this.globalDataElementsTree == null) {
         this.sourceTreeLayout.decorate(var1);
      } else {
         KongaSplitPane var2 = KongaSplitPane.verticalSplit(wrap(this.sourceTreeLayout), wrap(this.globalDataElementsTree));
         var2.setResizeWeight(0.5);
         var1.display(var2);
      }
   }

   private static JComponent wrap(UiProvider var0) {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(var0);
      var1.withBackground(StyleSheet.GenericEditorPanelClass.getBackground());
      return var1.container();
   }

   @Override
   public void requestFocus() {
      if (this.sourceTreeLayout != null) {
         this.sourceTreeLayout.requestFocus();
      } else {
         this.globalDataElementsTree.requestFocus();
      }
   }
}
