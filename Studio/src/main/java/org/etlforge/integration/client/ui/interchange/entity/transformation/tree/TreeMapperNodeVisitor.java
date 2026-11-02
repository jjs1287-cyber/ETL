package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

public interface TreeMapperNodeVisitor {
   void visit(TreeElement var1);

   void visit(TreeAttribute var1);
}
