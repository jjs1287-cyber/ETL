package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public final class ZoomAction extends AbstractAction {
   private final TreeMapper treeMapper;
   private final int delta;

   public static ZoomAction zoomIn(TreeMapper var0) {
      return new ZoomAction(var0, "Zoom In", ClientIcons.getIcon("ZoomIn.16"), ClientIcons.getIcon("ZoomIn.24"), "Zoom In", 1);
   }

   public static ZoomAction zoomOut(TreeMapper var0) {
      return new ZoomAction(var0, "Zoom Out", ClientIcons.getIcon("ZoomOut.16"), ClientIcons.getIcon("ZoomOut.24"), "Zoom Out", -1);
   }

   private ZoomAction(TreeMapper var1, String var2, Icon var3, Icon var4, String var5, int var6) {
      super(var2, var3);
      this.treeMapper = var1;
      this.delta = var6;
      this.putValue("KongaMediumIconKey", var4);
      this.putValue("ShortDescription", var5);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.treeMapper.changeFontSize(this.delta);
   }
}
