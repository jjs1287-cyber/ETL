package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeExpansionMode;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class NodeExpansionMenu {
   private final TransformationPage page;
   private final JRadioButtonMenuItem normalModeItem;
   private final JRadioButtonMenuItem minimalModeItem;

   public NodeExpansionMenu(TransformationPage var1) {
      this.page = var1;
      this.normalModeItem = new JRadioButtonMenuItem("Normal");
      this.minimalModeItem = new JRadioButtonMenuItem("Minimal");
      ButtonUtils.makeMutuallyExclusive(this.normalModeItem, this.minimalModeItem);
      ButtonUtils.addActionListener(new NodeExpansionMenu.Listener(), this.normalModeItem, this.minimalModeItem);
      var1.addPostPageLoadingJob(new NodeExpansionMenu.InitialStateSetter());
   }

   public JMenu createMenu() {
      JMenu var1 = new JMenu("XML Node Expansion Mode");
      var1.add(this.normalModeItem);
      var1.add(this.minimalModeItem);
      return var1;
   }

   private class InitialStateSetter implements Runnable {
      private InitialStateSetter() {
      }

      @Override
      public void run() {
         NodeExpansionMode var1 = NodeExpansionMenu.this.page.getTreeMapper().getNodeExpansionMode();
         if (var1 == NodeExpansionMode.MINIMAL) {
            NodeExpansionMenu.this.minimalModeItem.setSelected(true);
         } else {
            NodeExpansionMenu.this.normalModeItem.setSelected(true);
         }
      }
   }

   private class Listener implements ActionListener {
      private Listener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         NodeExpansionMode var2 = this.getMode(var1);
         NodeExpansionMenu.this.page.getTreeMapper().setNodeExpansionMode(var2);
      }

      private NodeExpansionMode getMode(ActionEvent var1) {
         return var1.getSource() == NodeExpansionMenu.this.normalModeItem ? NodeExpansionMode.NORMAL : NodeExpansionMode.MINIMAL;
      }
   }
}
