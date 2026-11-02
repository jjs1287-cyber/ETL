package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.data.script.SourceDataElementContainer;

public class SourceTreeDataElementContainer implements SourceDataElementContainer {
   private final TreeComponent sourceTree;

   public SourceTreeDataElementContainer(TreeComponent var1) {
      this.sourceTree = Preconditions.checkNotNull(var1);
   }

   @Override
   public boolean contains(String var1) {
      return this.sourceTree.findNode_expandTreeIfNotExist(var1) != null;
   }

   @Override
   public boolean isFlat() {
      return this.sourceTree.m_flat;
   }
}
