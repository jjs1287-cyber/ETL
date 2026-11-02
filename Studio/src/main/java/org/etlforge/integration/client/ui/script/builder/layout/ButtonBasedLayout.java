package org.etlforge.integration.client.ui.script.builder.layout;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import org.etlforge.application.ui.widget.HideableSplitPane;
import org.etlforge.application.ui.widget.HideableSplitPaneToggle;
import org.etlforge.application.ui.widget.SectionedSplitPane;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.client.ui.script.builder.ExpressionAreaStatusPanel;
import org.etlforge.integration.client.ui.script.builder.ScriptTestUi;
import org.etlforge.lang.Intervals;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.resources.UiResourceBundle;

public final class ButtonBasedLayout extends AbstractScriptBuilderLayout {
   private SectionedSplitPane sectionSplit;
   private HideableSplitPaneToggle topBottomToggle;
   private boolean testSectionSelected;

   public ButtonBasedLayout(ExpressionArea var1, ExpressionAreaStatusPanel var2) {
      super(var1, var2);
   }

   @Override
   public void setSourceTree(UiProvider var1) {
      this.addSection("&Source Tree", ClientIcons.getIcon("SourceTree.16"), var1);
   }

   @Override
   public void addSection(String var1, Icon var2, UiProvider var3) {
      this.prepareNewSection();
      SectionedSplitPane.Selector var4 = this.sectionSplit.addSection(null, var2, var3);
      MnemonicString var5 = UiResourceBundle.MNEMONIC_FACTORY.fromString(var1);
      var4.setToolTip(var5.getText());
   }

   private void prepareNewSection() {
      if (this.sectionSplit == null) {
         this.sectionSplit = this.createSectionSplit();
      }
   }

   private SectionedSplitPane createSectionSplit() {
      HideableSplitPane var1 = new HideableSplitPane(1, 1);
      var1.putClientProperty("section_split", Boolean.TRUE);
      var1.setResizeWeight(0.75);
      var1.setOpaque(false);
      var1.setDividerSize(4);
      var1.setLeftMinimumSize(new Dimension(50, 50));
      var1.setLeftComponent(this.left);
      SectionedSplitPane var2 = new SectionedSplitPane(var1);
      var2.setIncludeControlPanelInLayout(false);
      var2.setOpaque(false);
      return var2;
   }

   @Override
   public void setDebugUi(ScriptTestUi var1) {
      HideableSplitPane var2 = createTopBottomSplit();
      this.topBottomToggle = new HideableSplitPaneToggle(var2, Icons.TEST_ICON_16);
      this.topBottomToggle.setToolTipWhenDisplayed("Hide the test console");
      this.topBottomToggle.setToolTipWhenHidden("Show the test console");
      var2.setTopComponent(this.top);
      var2.setBottomComponent(var1);
      this.left.center(var2);
      this.topBottomSplit = var2;
      var2.addPropertyChangeListener("hidden", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            if (Boolean.FALSE.equals(var1.getNewValue())) {
               ButtonBasedLayout.this.makeSureTestSectionVisible();
            }
         }
      });
   }

   private static HideableSplitPane createTopBottomSplit() {
      HideableSplitPane var0 = new HideableSplitPane(0, 1);
      var0.putClientProperty("test_split", Boolean.TRUE);
      var0.setDividerSize(4);
      var0.setResizeWeight(0.5);
      var0.setBottomMinimumSize(new Dimension(0, 0));
      var0.setBottomPreferredSize(new Dimension(20, 20));
      var0.setDividerHidden(false);
      return var0;
   }

   private void makeSureTestSectionVisible() {
      double var1 = this.topBottomSplit.getProportionalDividerLocation();
      double var3 = Intervals.valueWithin(var1, 0.5, 0.75);
      int var5 = this.topBottomSplit.getHeight();
      if (var5 > 0) {
         this.topBottomSplit.setDividerLocation((int)(var3 * var5));
      } else {
         this.topBottomSplit.tryToSetPropertionalDividerLocation(var3, 5);
      }
   }

   @Override
   protected JComponent buildUi() {
      if (this.sectionSplit == null) {
         this.left.center(this.top);
         return this.left.container();
      } else {
         this.sectionSplit.useSelectorIndexAsShortcut();
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(2, 2).nonOpaque();
         var1.center(this.sectionSplit).east(this.layoutToggleButtons());
         return var1.container();
      }
   }

   private JComponent layoutToggleButtons() {
      JComponent var1 = this.sectionSplit.getControlPanel();
      JToggleButton var2 = this.topBottomToggle.getToggle();
      return Vertical.box().leftAligned().nonOpaque().add(var1).glue().add(var2).container();
   }

   @Override
   protected void postDisplay() {
      if (this.sectionSplit != null && !this.sectionSplit.isEmpty()) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ButtonBasedLayout.this.sectionSplit.showSection(0);
               ButtonBasedLayout.this.setSectionSplitPreferenceStore(ButtonBasedLayout.this.sectionSplit.getSplitPane());
            }
         });
      }

      if (this.topBottomSplit != null && !this.testSectionSelected) {
         ((HideableSplitPane)this.topBottomSplit).setDividerHidden(true);
      }
   }

   @Override
   public void selectTestAndDebuggingTab() {
      if (this.topBottomSplit != null) {
         ((HideableSplitPane)this.topBottomSplit).setDividerHidden(false);
         this.testSectionSelected = true;
      }
   }
}
