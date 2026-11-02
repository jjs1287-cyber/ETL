package org.etlforge.integration.client.ui.script.builder.layout;

import java.awt.EventQueue;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.client.ui.script.builder.ExpressionAreaStatusPanel;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSection;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.util.preference.PreferencesKey;

abstract class AbstractScriptBuilderLayout implements ScriptBuilderLayout {
   private JComponent ui;
   protected final JComponent top;
   protected final BorderLayoutBuilder left;
   protected KongaSplitPane topBottomSplit;
   private int postDisplayDelay = 1;

   protected AbstractScriptBuilderLayout(ExpressionArea var1, ExpressionAreaStatusPanel var2) {
      this.left = new BorderLayoutBuilder(0, 10);
      this.top = getExpressesionAreaLayout(var1, var2);
   }

   private static JComponent getExpressesionAreaLayout(ExpressionArea var0, ExpressionAreaStatusPanel var1) {
      return new BorderLayoutBuilder().center(var0).south(var1).container();
   }

   protected static KongaSplitPane createSplitPane(int var0, boolean var1) {
      KongaSplitPane var2 = ComponentFactories.splitPaneFactory().newSplitPane(var0);
      var2.setDividerSize(var1 ? 8 : 4);
      var2.setOneTouchExpandable(var1);
      var2.setResizeWeight(0.5);
      return var2;
   }

   public abstract void addSection(String var1, Icon var2, UiProvider var3);

   @Override
   public final void addSection(ScriptBuilderSection var1) {
      ScriptBuilderSection.Title var2 = var1.getTitleDecoration();
      this.addSection(var2.getText(), var2.getIcon(), var1);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.ui = this.buildUi();
      var1.display(this.ui);
      UiUtils.nestedInvokeLater(this.postDisplayDelay, new Runnable() {
         @Override
         public void run() {
            AbstractScriptBuilderLayout.this.positionTopBottomDivider();
            AbstractScriptBuilderLayout.this.postDisplay();
         }
      });
   }

   private void positionTopBottomDivider() {
      if (this.isTestingEnabled()) {
         PreferencesKey var1 = new PreferencesKey(ScriptBuilderUi.class, "TestSplit", null);
         this.topBottomSplit.setPreferenceStore(var1, 0.65);
      }
   }

   protected abstract JComponent buildUi();

   protected abstract void postDisplay();

   @Override
   public final void setPostDisplayDelay(int var1) {
      this.postDisplayDelay = var1;
   }

   @Override
   public final void setButtonPanel(JComponent var1) {
      this.left.south(var1);
      if (this.ui != null) {
         UiUtils.revalidate(this.ui);
      }
   }

   @Override
   public final void installTestActionShortcut(Action var1) {
      if (this.isTestingEnabled()) {
         InputMap var2 = this.topBottomSplit.getInputMap(2);
         var2.put(ScriptBuilderUi.TEST_SCRIPT_SHORTCUT, "testExpression");
         ActionMap var3 = this.topBottomSplit.getActionMap();
         var3.put("testExpression", var1);
      }
   }

   protected final boolean isTestingEnabled() {
      return this.topBottomSplit != null;
   }

   protected final void setSectionSplitPreferenceStore(final KongaSplitPane var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PreferencesKey var1x = new PreferencesKey(ScriptBuilderUi.class, "SectionSplit", null);
            var1.setPreferenceStore(var1x, 0.65);
         }
      });
   }
}
