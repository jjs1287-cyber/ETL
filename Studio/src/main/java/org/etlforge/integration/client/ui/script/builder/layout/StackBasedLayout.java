package org.etlforge.integration.client.ui.script.builder.layout;

import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.client.ui.script.builder.ExpressionAreaStatusPanel;
import org.etlforge.integration.client.ui.script.builder.ScriptTestUi;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.StackedSections;
import org.etlforge.ui.widget.split.KongaSplitPane;

public final class StackBasedLayout extends AbstractScriptBuilderLayout {
   private StackedSections sectionStack;
   private KongaSplitPane sectionSplit;

   public StackBasedLayout(ExpressionArea var1, ExpressionAreaStatusPanel var2) {
      super(var1, var2);
   }

   @Override
   public void addSection(String var1, Icon var2, UiProvider var3) {
      if (this.sectionStack == null) {
         this.sectionStack = new StackedSections();
         this.sectionStack.setExclusive(true);
      }

      this.sectionStack.addSection(var1, var2, var3);
   }

   @Override
   public void setSourceTree(UiProvider var1) {
      this.addSection("&Source Tree", ClientIcons.getIcon("SourceTree.16"), var1);
   }

   @Override
   public void setDebugUi(ScriptTestUi var1) {
      this.topBottomSplit = createSplitPane(0, true);
      this.topBottomSplit.setTopComponent(this.top);
      this.topBottomSplit.setBottomComponent(var1);
      this.left.center(this.topBottomSplit);
   }

   @Override
   protected JComponent buildUi() {
      if (this.sectionStack == null) {
         this.left.center(this.top);
         return this.left.container();
      } else {
         this.sectionSplit = createSplitPane(1, true);
         this.sectionSplit.split(this.left, this.sectionStack);
         return this.sectionSplit;
      }
   }

   @Override
   protected void postDisplay() {
      if (this.sectionStack != null) {
         this.sectionStack.openSection(0);
         this.setSectionSplitPreferenceStore(this.sectionSplit);
      }
   }

   @Override
   public void selectTestAndDebuggingTab() {
   }
}
