package org.etlforge.json.dom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public final class ObjectNode extends AbstractJsonNode implements RootNode, ValueNode {
   private final String name;
   private final LinkedHashMap<String, FieldNode> fields = Maps.newLinkedHashMap();

   public ObjectNode(String var1) {
      this.name = var1;
   }

   public void addField(FieldNode var1) {
      var1.setParent(this);
      this.fields.put(var1.getName(), var1);
   }

   public FieldNode getChildAt(int var1) {
      List var2 = this.getChildList();
      return (FieldNode)var2.get(var1);
   }

   private List<FieldNode> getChildList() {
      return Lists.newArrayList(this.fields.values());
   }

   public FieldNode getField(String var1) {
      return this.fields.get(var1);
   }

   @Override
   public Iterable<FieldNode> getChildren() {
      return ImmutableList.copyOf(this.fields.values());
   }

   public int getFieldCount() {
      return this.fields.size();
   }

   @Override
   public int getFingerprint() {
      int var1 = 17;
      var1 = 31 * var1 + (this.name == null ? 0 : this.name.hashCode());

      for (FieldNode var3 : this.sortedFieldList()) {
         var1 = 31 * var1 + var3.getFingerprint();
      }

      return var1;
   }

   private List<FieldNode> sortedFieldList() {
      List var1 = this.getChildList();
      Collections.sort(var1, new Comparator<FieldNode>() {
         public int compare(FieldNode var1, FieldNode var2) {
            return var1.getName().compareTo(var2.getName());
         }
      });
      return var1;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Object(");
      var1.append(this.name).append(")[");
      int var2 = 0;

      for (FieldNode var4 : this.fields.values()) {
         if (var2 > 20) {
            var1.append(",...");
            break;
         }

         if (var2 > 0) {
            var1.append(",");
         }

         var1.append(var4.getName());
         var2++;
      }

      var1.append("]");
      return var1.toString();
   }
}
