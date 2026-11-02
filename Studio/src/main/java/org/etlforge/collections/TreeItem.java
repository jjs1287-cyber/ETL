package org.etlforge.collections;

import java.util.ArrayList;
import java.util.List;

public final class TreeItem<T> {
   private final String m_name;
   private final List<TreeItem<T>> m_children = new ArrayList<>();
   private final List<T> m_obj;

   TreeItem(String var1) {
      this.m_name = var1;
      this.m_obj = new ArrayList<>();
   }

   boolean hasChild(TreeItem<T> var1) {
      for (TreeItem var3 : this.m_children) {
         if (var3 == var1) {
            return true;
         }
      }

      return false;
   }

   void addChild(TreeItem<T> var1) {
      this.m_children.add(var1);
   }

   public String getName() {
      return this.m_name;
   }

   public List<T> getData() {
      return this.m_obj;
   }

   public List<TreeItem<T>> getChildren() {
      return new ArrayList<>(this.m_children);
   }

   boolean removeChild(TreeItem<T> var1) {
      return this.m_children.remove(var1);
   }

   public void add(T var1) {
      this.m_obj.add((T)var1);
   }

   public void print(String var1) {
      System.out.println(var1 + this.m_name);
      String var2 = var1 + "    ";

      for (TreeItem var4 : this.m_children) {
         var4.print(var2);
      }
   }

   @Override
   public String toString() {
      return this.m_name;
   }

   public String toString(String var1) {
      StringBuilder var2 = new StringBuilder(var1).append(this.m_name).append("\n");
      String var3 = var1 + " ";

      for (TreeItem var5 : this.m_children) {
         var2.append(var5.toString(var3));
      }

      return var2.toString();
   }
}
