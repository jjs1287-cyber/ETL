package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

public interface TreeElement extends TreeMapperNode {
   TreeElement.RootFactory ROOT_FACTORY = new TreeElement.RootFactory() {
      @Override
      public TreeElement newRoot(String var1) {
         return new TreeElementImpl(var1);
      }
   };

   TreeElement addElement(String var1, ElementCardinality var2);

   TreeAttribute addAttribute(String var1, String var2, int var3, String var4, boolean var5);

   ElementCardinality cardinality();

   interface RootFactory {
      TreeElement newRoot(String var1);
   }
}
