package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperMode;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class TreeMapperModeMenu {
   private final TransformationPage page;
   private final JRadioButtonMenuItem simpleModeItem;
   private final JRadioButtonMenuItem fullModeItem;
   private final JMenu menu;

   public TreeMapperModeMenu(TransformationPage var1) {
      this.page = var1;
      this.simpleModeItem = createMenuItem("Simple");
      this.fullModeItem = createMenuItem("Full");
      ButtonUtils.makeMutuallyExclusive(this.simpleModeItem, this.fullModeItem);
      ButtonUtils.addActionListener(new TreeMapperModeMenu.ActionListenerImpl(), this.simpleModeItem, this.fullModeItem);
      this.menu = this.createMenu();
      var1.addPostPageLoadingJob(new TreeMapperModeMenu.Activator());
   }

   private static JRadioButtonMenuItem createMenuItem(String var0) {
      JRadioButtonMenuItem var1 = new JRadioButtonMenuItem(var0);
      var1.setEnabled(false);
      return var1;
   }

   private JMenu createMenu() {
      JMenu var1 = new JMenu("Display Mode");
      var1.add(this.simpleModeItem);
      var1.add(this.fullModeItem);
      var1.setEnabled(false);
      return var1;
   }

   public JMenu getMenu() {
      return this.menu;
   }

   private class ActionListenerImpl implements ActionListener {
      private ActionListenerImpl() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TreeMapperMode var2 = this.getMode(var1.getSource());
         TreeMapperModeMenu.this.page.setDisplayMode(var2);
      }

      private TreeMapperMode getMode(Object var1) {
         return var1 == TreeMapperModeMenu.this.simpleModeItem ? TreeMapperMode.SIMPLE : TreeMapperMode.FULL;
      }
   }

   private class Activator implements Runnable {
      private Activator() {
      }

      @Override
      public void run() {
         TreeMapper var1 = TreeMapperModeMenu.this.page.getTreeMapper();
         var1.addPropertyChangeListener("mode", TreeMapperModeMenu.this.new ExternalChangeListener());
         this.updateMenu(var1);
      }

      private void updateMenu(TreeMapper var1) {
         TreeMapperMode var2 = var1.getMode();
         if (var2 == TreeMapperMode.SIMPLE) {
            TreeMapperModeMenu.this.simpleModeItem.setSelected(true);
         } else {
            TreeMapperModeMenu.this.fullModeItem.setSelected(true);
         }

         TreeMapperModeMenu.this.simpleModeItem.setEnabled(true);
         TreeMapperModeMenu.this.fullModeItem.setEnabled(true);
         TreeMapperModeMenu.this.menu.setEnabled(this.isApplicable());
      }

      private boolean isApplicable() {
         return TreeMapperModeMenu.this.page.getTreeMapper().hasSimpleDisplay();
      }
   }

   private class ExternalChangeListener implements PropertyChangeListener {
      private ExternalChangeListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "mode".equals(var1.getPropertyName());
         TreeMapperMode var2 = (TreeMapperMode)var1.getNewValue();
         if (var2 == TreeMapperMode.SIMPLE) {
            TreeMapperModeMenu.this.simpleModeItem.setSelected(true);
         } else {
            TreeMapperModeMenu.this.fullModeItem.setSelected(true);
         }
      }
   }
}
