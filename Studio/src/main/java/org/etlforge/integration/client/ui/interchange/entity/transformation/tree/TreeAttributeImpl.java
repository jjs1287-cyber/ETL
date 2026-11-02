package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.util.Iterator;
import org.etlforge.collections.Collections2;

final class TreeAttributeImpl extends TreeMapperNodeImpl implements TreeAttribute {
   private final String dataType;
   private final int sqlDataType;
   private final String defaultValue;
   private final boolean isNullable;

   public TreeAttributeImpl(TreeMapperNode var1, String var2, String var3, int var4, String var5, boolean var6) {
      super(var2, var1);
      this.dataType = var3;
      this.defaultValue = var5;
      this.isNullable = var6;
      this.sqlDataType = var4;
   }

   @Override
   public void accept(TreeMapperNodeVisitor var1) {
      var1.visit(this);
   }

   @Override
   public String getDataType() {
      return this.dataType;
   }

   @Override
   public int getSqlDataType() {
      return this.sqlDataType;
   }

   @Override
   public String getDefaultValue() {
      return this.defaultValue;
   }

   @Override
   public boolean isNullable() {
      return this.isNullable;
   }

   @Override
   public Iterator<TreeMapperNode> children() {
      return Collections2.emptyIterator();
   }

   @Override
   public String toString() {
      return "[A] " + this.getName();
   }
}
