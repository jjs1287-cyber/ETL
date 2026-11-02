package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

final class TreeElementImpl extends TreeMapperNodeImpl implements TreeElement {
   private final List<TreeMapperNode> children;
   private ElementCardinality cardinality = ElementCardinality.ONCE_ONLY;

   public TreeElementImpl(String var1) {
      this(var1, null);
   }

   public TreeElementImpl(String var1, TreeMapperNode var2) {
      super(var1, var2);
      this.children = Lists.newArrayList();
   }

   @Override
   public TreeElement addElement(String var1, ElementCardinality var2) {
      TreeElementImpl var3 = new TreeElementImpl(var1, this);
      var3.cardinality = var2;
      this.children.add(var3);
      return var3;
   }

   @Override
   public ElementCardinality cardinality() {
      return this.cardinality;
   }

   @Override
   public TreeAttribute addAttribute(String var1, String var2, int var3, String var4, boolean var5) {
      TreeAttributeImpl var6 = new TreeAttributeImpl(this, var1, var2, var3, var4, var5);
      this.children.add(var6);
      return var6;
   }

   @Override
   public void accept(TreeMapperNodeVisitor var1) {
      var1.visit(this);
   }

   @Override
   public Iterator<TreeMapperNode> children() {
      return Collections.unmodifiableList(this.children).iterator();
   }

   @Override
   public String toString() {
      return "[E] " + this.getName();
   }
}
