package org.etlforge.application.ui.widget.tree;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.tree.KongaTree;

public final class TreeExpansionToolbar implements UiProvider {
   private String title;
   private final KongaTree tree;
   private final JButton expandAll;
   private final JButton collapseAll;
   private final JPanel panel;

   public TreeExpansionToolbar(KongaTree var1) {
      this.tree = var1;
      this.expandAll = KongaButton.forToolbar(new ExpandAllAction(var1));
      this.collapseAll = KongaButton.forToolbar(new CollapseAllAction(var1));
      this.panel = new JPanel();
      this.panel.setBackground(Colors.TREE_TITLE_FOCUSED_BACKGROUND);
      this.panel.setBorder(StyleSheet.TreeTitleBorder);
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public void setBackground(Color var1) {
      this.panel.setBackground(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal(this.panel);
      if (this.title != null) {
         KongaLabel var3 = KongaLabel.withMnemonic(this.title);
         var3.setLabelFor(this.tree);
         var2.add(var3);
      }

      var2.glue().addAllWithSpace(5, this.expandAll, this.collapseAll);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.expandAll.requestFocusInWindow();
   }
}
