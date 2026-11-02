package org.etlforge.collections;

import com.google.common.collect.Maps;
import java.util.Map;

public final class BuildTree<T> {
   private final Map<String, TreeItem<T>> m_map;
   private final TreeItem<T> m_root = new TreeItem<>("_ROOT_");

   public BuildTree() {
      this.m_map = Maps.newHashMap();
   }

   public TreeItem<T> getRoot() {
      return this.m_root;
   }

   public TreeItem<T> addRelation(String var1, String var2) {
      TreeItem var3 = this.m_map.get(var1);
      TreeItem var4 = null;
      if (var3 == null) {
         var3 = new TreeItem(var1);
         this.m_map.put(var1, var3);
         this.m_root.addChild(var3);
      }

      if (var2 != null && var2.length() > 0) {
         var4 = this.m_map.get(var2);
         if (var4 == null) {
            var4 = new TreeItem(var2);
            this.m_map.put(var2, var4);
            var3.addChild(var4);
         } else {
            if (!var3.hasChild(var4)) {
               var3.addChild(var4);
            }

            if (this.m_root.hasChild(var4)) {
               this.m_root.removeChild(var4);
            }
         }
      }

      return var4;
   }

   public void print(String var1) {
      if (this.m_root == null) {
         System.out.println("nothing in the tree");
      } else {
         System.out.println(var1);
         System.out.println("BuildTree:");
         this.m_root.print("");
      }
   }

   @Override
   public String toString() {
      return this.m_map.toString() + "\n" + this.m_root.toString("");
   }
}
