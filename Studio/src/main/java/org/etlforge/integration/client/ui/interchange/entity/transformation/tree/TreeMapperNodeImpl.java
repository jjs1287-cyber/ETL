package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

abstract class TreeMapperNodeImpl implements TreeMapperNode {
   private final String name;
   private TreeMapperNode parent;

   TreeMapperNodeImpl(String var1) {
      this(var1, null);
   }

   TreeMapperNodeImpl(String var1, TreeMapperNode var2) {
      this.name = var1;
      this.parent = var2;
   }

   @Override
   public final String getName() {
      return this.name;
   }

   @Override
   public final TreeMapperNode getParent() {
      return this.parent;
   }

   void setParent(TreeMapperNode var1) {
      this.parent = var1;
   }
}
