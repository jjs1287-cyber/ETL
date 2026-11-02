package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class MapAction extends AbstractSingleObjectSelectionAction<Node> {
   private TreeMapper treeMapper;

   public MapAction() {
      super(Node.class, "Map", ClientIcons.MAP_16);
      this.putValue("KongaMediumIconKey", ClientIcons.MAP_24);
      this.putValue("ShortDescription", "Map a Source data element to a Target data element");
   }

   public MapAction(TreeMapper var1) {
      this();
      this.setTreeMapper(var1);
   }

   public MapAction(final TransformationPage var1) {
      this();
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            MapAction.this.setTreeMapper(var1.getTreeMapper());
         }
      });
   }

   public void setTreeMapper(TreeMapper var1) {
      this.treeMapper = var1;
      this.setSelectionSource(var1.m_targetTreeComponent);
   }

   protected boolean isEnabledFor(Node var1) {
      return !var1.isFolder();
   }

   protected void run(Node var1, ActionEvent var2) {
      this.treeMapper.doMap(var1, false);
   }
}
