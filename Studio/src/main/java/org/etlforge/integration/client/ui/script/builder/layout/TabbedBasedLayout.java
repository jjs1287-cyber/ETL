package org.etlforge.integration.client.ui.script.builder.layout;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.client.ui.script.builder.ExpressionAreaStatusPanel;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.client.ui.script.builder.ScriptTestUi;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;
import org.etlforge.util.preference.PreferencesKey;

public final class TabbedBasedLayout extends AbstractScriptBuilderLayout {
   private KongaSplitPane sourceTreeSplit;
   private KongaTabbedPane tabs;
   private int testTabIndex;

   public TabbedBasedLayout(ExpressionArea var1, ExpressionAreaStatusPanel var2) {
      super(var1, var2);
   }

   @Override
   public void addSection(String var1, Icon var2, UiProvider var3) {
      KongaTabbedPane var4 = this.getTabs();
      var4.addTab(var1, var2, var3);
   }

   @Override
   public void setDebugUi(ScriptTestUi var1) {
      KongaTabbedPane var2 = this.getTabs();
      this.testTabIndex = var2.getTabCount();
      var2.addTab("&Test", Icons.TEST_ICON_16, var1);
   }

   private KongaTabbedPane getTabs() {
      if (this.tabs == null) {
         this.tabs = new KongaTabbedPane();
         this.tabs.setInterpretTitlesAsMnemonicStrings(true);
         this.topBottomSplit = createSplitPane(0, true);
         this.topBottomSplit.setTopComponent(this.top);
         this.topBottomSplit.setBottomComponent(this.tabs);
         this.left.center(this.topBottomSplit);
      }

      return this.tabs;
   }

   @Override
   public void setSourceTree(UiProvider var1) {
      this.sourceTreeSplit = createSplitPane(1, true);
      this.sourceTreeSplit.split(this.left, var1);
   }

   @Override
   public void selectTestAndDebuggingTab() {
      if (this.testTabIndex >= 0) {
         this.tabs.setSelectedIndex(this.testTabIndex);
      }
   }

   @Override
   protected JComponent buildUi() {
      if (this.sourceTreeSplit == null) {
         if (this.tabs == null) {
            this.left.center(this.top);
         }

         return this.left.container();
      } else {
         return this.sourceTreeSplit;
      }
   }

   @Override
   protected void postDisplay() {
      if (this.sourceTreeSplit != null) {
         this.sourceTreeSplit.setPreferenceStore(new PreferencesKey(ScriptBuilderUi.class, "SourceTreeSplit", null));
      }
   }
}
