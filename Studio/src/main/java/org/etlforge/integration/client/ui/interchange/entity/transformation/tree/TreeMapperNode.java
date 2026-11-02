package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.util.Iterator;

public interface TreeMapperNode {
   String getName();

   TreeMapperNode getParent();

   Iterator<TreeMapperNode> children();

   void accept(TreeMapperNodeVisitor var1);
}
