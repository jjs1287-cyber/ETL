package com.etlforge.integration.client.ui.script.fcn;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.HideableSplitPane;
import org.etlforge.application.ui.widget.HtmlDisplayer;
import org.etlforge.application.ui.widget.SectionedSplitPane;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.data.script.Function;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.util.preference.PreferencesKey;

public abstract class AbstractSimpleFunctionWizardPage implements SimpleFunctionWizardPage {
   private UiProvider inputArea;
   private final VariableSelector variableSelector;
   private final KongaSplitPane pageSplit;
   private final SectionedSplitPane fieldsAndDocumentationSplit;

   public AbstractSimpleFunctionWizardPage(Function var1, VariableSelector var2, UiProvider var3) {
      this.variableSelector = var2;
      this.fieldsAndDocumentationSplit = this.createFieldsAndDocumentationSplit(var1, var3);
      this.pageSplit = createEmptyPageSplit();
      this.pageSplit.split(this.fieldsAndDocumentationSplit, var2);
   }

   private static KongaSplitPane createEmptyPageSplit() {
      KongaSplitPane var0 = new KongaSplitPane(1);
      var0.setResizeWeight(0.75);
      var0.setLeftPreferredSize(new Dimension(200, 200));
      var0.setRightPreferredSize(new Dimension(200, 200));
      var0.setOneTouchExpandable(true);
      return var0;
   }

   private SectionedSplitPane createFieldsAndDocumentationSplit(Function var1, UiProvider var2) {
      HideableSplitPane var3 = new HideableSplitPane(0, 1);
      var3.setResizeWeight(0.5);
      var3.setBottomMinimumSize(new Dimension(0, 0));
      var3.setBottomPreferredSize(new Dimension(0, 200));
      SectionedSplitPane var4 = new SectionedSplitPane(var3);
      JComponent var5 = this.layoutDocumentation(var1);
      SectionedSplitPane.Selector var6 = var4.addSection(null, CommonIcons.HELP_16, var5);
      var6.setToolTip("Documentation");
      if (var2 != null) {
         SectionedSplitPane.Selector var7 = var4.addSection(null, Icons.TEST_ICON_16, var2);
         var7.setToolTip("Test");
      }

      var4.useSelectorIndexAsShortcut();
      var4.setOpaque(false);
      var4.setBackground(StyleSheet.GenericEditorPanelClass.getBackground());
      var4.setControlPanelBorder(Empty.border(4, 2, 4, 2));
      return var4;
   }

   private JComponent layoutDocumentation(Function var1) {
      HtmlDisplayer var2 = new HtmlDisplayer();
      var2.setText(var1.getDocumentation());
      var2.setCaretPosition(0);
      var2.setPreferredSize(new Dimension(300, 300));
      return new JScrollPane(var2);
   }

   public final void setInputArea(UiProvider var1) {
      this.inputArea = var1;
      this.fieldsAndDocumentationSplit.getSplitPane().setTopComponent(var1);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(this.pageSplit);
      var2.setBorder(Empty.border(0, 2, 0, 2));
      var1.display(var2);
   }

   @Override
   public final void requestFocus() {
      this.inputArea.requestFocus();
   }

   @Override
   public final void postDisplay() {
      this.requestFocus();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PreferencesKey var1 = new PreferencesKey(AbstractSimpleFunctionWizardPage.this.getClass(), "Divider", null);
            AbstractSimpleFunctionWizardPage.this.pageSplit.setPreferenceStore(var1, 0.65);
            AbstractSimpleFunctionWizardPage.this.variableSelector.expandSourceTree();
         }
      });
   }

   @Override
   public final void showTestSection() {
      if (this.fieldsAndDocumentationSplit.getSectionCount() == 2) {
         this.fieldsAndDocumentationSplit.showSection(1);
      }
   }
}
