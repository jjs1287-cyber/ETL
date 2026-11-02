package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.border.RoundedBorder;
import org.etlforge.ui.widget.DefaultComponentAction;
import org.etlforge.ui.widget.button.KongaButton;

public final class SourceTreeLayout implements UiProvider {
   private final FormulaBuilderSourceTree sourceTree;
   private final JButton sourceSearchButton;
   private final JButton toggleSourceTreeExpansionButton;
   private final JButton sourceTransferButton;

   public SourceTreeLayout(FormulaBuilderSourceTree var1, Receiver<String> var2) {
      this.sourceTree = var1;
      SourceTreeNodeTransferAction var3 = new SourceTreeNodeTransferAction(var1, var2);
      DefaultComponentAction.bind(var1, var3);
      this.sourceSearchButton = this.createTreeSearchButton("Search in the source tree");
      this.toggleSourceTreeExpansionButton = this.createToggleSourceTreeButton();
      this.sourceTransferButton = KongaButton.forToolbar(var3);
      var1.setSourceTransferButton(this.sourceTransferButton);
      this.installDragSupport();
   }

   private JButton createTreeSearchButton(String var1) {
      JButton var2 = createToolbarButton(PackageResources.SEARCH_ICON, var1);
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            SourceTreeLayout.this.sourceTree.search();
         }
      });
      return var2;
   }

   private static JButton createToolbarButton(Icon var0, String var1) {
      KongaButton var2 = new KongaButton(var0).makeToolbarButton();
      var2.setToolTipText(var1);
      return var2;
   }

   private JButton createToggleSourceTreeButton() {
      final JButton var1 = createToolbarButton(PackageResources.EXPAND_ICON, "Expand/Collapse the source tree");
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            SourceTreeLayout.this.sourceTree.changeShowChildren();
            switch (SourceTreeLayout.this.sourceTree.m_expandMode) {
               case 0:
                  var1.setIcon(PackageResources.EXPAND_ICON);
                  break;
               case 1:
               case 2:
                  var1.setIcon(PackageResources.COLLAPSE_ICON);
            }

            SourceTreeLayout.this.sourceTree.refresh();
         }
      });
      return var1;
   }

   private void installDragSupport() {
      DragSource var1 = new DragSource();
      var1.createDefaultDragGestureRecognizer(this.sourceTree, 1, new DragGestureListener() {
         @Override
         public void dragGestureRecognized(DragGestureEvent var1) {
            Point var2 = var1.getDragOrigin();
            SourceTreeLayout.this.sourceTree.getLocationStatus(var2.x, var2.y, true);
            Node var3 = SourceTreeLayout.this.sourceTree.m_getLocationNode;
            String var4 = var3.m_deName;
            var1.startDrag(DragSource.DefaultCopyDrop, new org.etlforge.integration.client.ui.script.builder.SourcePathTransferable(var4));
         }
      });
   }

   public void expand() {
      if (this.sourceTree.m_expandMode == 0) {
         this.toggleSourceTreeExpansionButton.doClick();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder().nonOpaque();
      JButton var3 = this.createLoadSourceButton();
      JComponent var4 = this.createToolbar(this.toggleSourceTreeExpansionButton, this.sourceSearchButton, this.sourceTransferButton, var3);
      var2.north(var4).center(this.sourceTree.m_scroll);
      var2.decorate(var1);
   }

   private JButton createLoadSourceButton() {
      Action var1 = this.sourceTree.getLoadSourceDataAction();
      return var1 == null ? null : new KongaButton(var1);
   }

   private JComponent createToolbar(JButton... var1) {
      RoundedBorder var2 = new RoundedBorder(6, 6, Color.GRAY);
      return Horizontal.box().nonOpaque().addAllWithSpace(5, var1).withBorder(var2).container();
   }

   @Override
   public void requestFocus() {
      this.sourceTree.requestFocusInWindow();
   }
}
