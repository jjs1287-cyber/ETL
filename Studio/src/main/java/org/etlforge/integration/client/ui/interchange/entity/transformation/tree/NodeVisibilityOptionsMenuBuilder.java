package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.menu.SimpleNodeDisplayAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.menu.SimpleXmlElementDisplayAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.widget.button.PopupMenuButton;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.menu.ToggleActionCheckBoxMenuItem;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class NodeVisibilityOptionsMenuBuilder {
   private static final Icon ICON = CommonIcons.FILTER_16;
   private static final String SHOW_DATA_NODES_ONLY = "Show Data Nodes Only";
   private static final String HIDE_UNMAPPED_NODES = "Hide Unmapped Nodes";
   private final TreeMapper treeMapper;

   public NodeVisibilityOptionsMenuBuilder(TreeMapper var1) {
      this.treeMapper = var1;
   }

   public PopupMenuButton buttonForSourceTree() {
      PopupMenuButton var1 = new PopupMenuButton(null, ICON, this.buildForSourceTree());
      var1.setToolTipText("Options for hiding nodes in the source tree");
      return var1;
   }

   private JPopupMenu buildForSourceTree() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      this.addMenuItem(var1, "action.ic.tx.ShowMappedSourceNodes", "Hide Unmapped Nodes");
      this.addMenuItem(var1, "action.ic.tx.ShowSourceData", "Show Data Nodes Only");
      this.addSimpleNodeDisplaySwitch(var1, this.treeMapper.m_sourceTreeComponent);
      this.addSimpleXmlElementDisplaySwitchIfSupported(var1, this.treeMapper.m_sourceTreeComponent);
      return var1;
   }

   private void addMenuItem(JPopupMenu var1, String var2, String var3) {
      Action var4 = this.treeMapper.getPageAction(var2);
      if (var4 != null) {
         JMenuItem var5 = var4 instanceof ToggleAction ? new ToggleActionCheckBoxMenuItem((ToggleAction)var4) : new KongaMenuItem(var4);
         if (var3 != null) {
            var5.setText(var3);
         }

         var1.add(var5);
      }
   }

   private void addSimpleNodeDisplaySwitch(JPopupMenu var1, TreeComponent var2) {
      SimpleNodeDisplayAction var3 = new SimpleNodeDisplayAction("Simple Node Display");
      var3.setTree(var2);
      var1.add(var3);
   }

   private void addSimpleXmlElementDisplaySwitchIfSupported(JPopupMenu var1, TreeComponent var2) {
      if (var2.isXml()) {
         Action var3 = this.createSimpleXmlElementDisplayAction(var2);
         var1.add(var3);
      }
   }

   private Action createSimpleXmlElementDisplayAction(TreeComponent var1) {
      SimpleXmlElementDisplayAction var2 = new SimpleXmlElementDisplayAction("Simple XML Element Display");
      var2.setTree(var1);
      return var2;
   }

   public PopupMenuButton buttonForTargetTree() {
      PopupMenuButton var1 = new PopupMenuButton(null, ICON, this.buildForTargetTree());
      var1.setToolTipText("Options for hiding nodes in the target tree");
      return var1;
   }

   private JPopupMenu buildForTargetTree() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      this.addMenuItem(var1, "action.ic.tx.ShowMappedTargetNodes", "Hide Unmapped Nodes");
      this.addMenuItem(var1, "action.ic.tx.ShowTargetData", "Show Data Nodes Only");
      this.addSimpleNodeDisplaySwitch(var1, this.treeMapper.m_targetTreeComponent);
      this.addSimpleXmlElementDisplaySwitchIfSupported(var1, this.treeMapper.m_targetTreeComponent);
      return var1;
   }
}
