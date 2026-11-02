package org.etlforge.base;

import com.google.common.collect.ArrayListMultimap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.util.PropertyChangeManager;

public final class ListenerReport {
   public static final String PROPERTY_CHANGE_CATEGORY = "PropertyChangeListener";
   private final Object source;
   private final ArrayListMultimap<String, ListenerReport.Item> items;

   public ListenerReport(Object var1) {
      this.source = var1;
      this.items = ArrayListMultimap.create();
   }

   public Object getSource() {
      return this.source;
   }

   public void addListener(String var1, Object var2) {
      this.items.put(var1, new ListenerReport.Item(var1, var2));
   }

   public void addListeners(String var1, Iterable<?> var2) {
      for (Object var4 : var2) {
         this.addListener(var1, var4);
      }
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.addListener("PropertyChangeListener", var1);
   }

   public void addPropertyChangeListeners(PropertyChangeSupport var1) {
      if (var1 != null) {
         for (PropertyChangeListener var5 : var1.getPropertyChangeListeners()) {
            this.addPropertyChangeListener(var5);
         }
      }
   }

   public void addPropertyChangeListeners(PropertyChangeManager var1) {
      if (var1 != null) {
         for (PropertyChangeListener var5 : var1.getListeners()) {
            this.addPropertyChangeListener(var5);
         }
      }
   }

   public List<String> getCategories() {
      return new ArrayList<>(this.items.keySet());
   }

   public List<String> getCategoriesAlphabetically() {
      List var1 = this.getCategories();
      Collections.sort(var1, String.CASE_INSENSITIVE_ORDER);
      return var1;
   }

   public List<ListenerReport.Item> getItems(String var1) {
      List var2 = this.items.get(var1);
      Collections.sort(var2);
      return var2;
   }

   public void writeTo(PrintStream var1) {
      for (String var3 : this.getCategoriesAlphabetically()) {
         List var4 = this.getItems(var3);
         var1.println(var3 + ": [" + var4.size() + "]");

         for (Object __etlItem77 : var4) {
            ListenerReport.Item var6 = (ListenerReport.Item) __etlItem77;
            var6.writeTo(var1, "  ");
         }
      }
   }

   public static final class Item implements Comparable<ListenerReport.Item> {
      private final String category;
      private final Object listener;

      private Item(String var1, Object var2) {
         this.category = var1;
         this.listener = var2;
      }

      public String getCategory() {
         return this.category;
      }

      public Object getListener() {
         return this.listener;
      }

      public void writeTo(PrintStream var1, String var2) {
         if (var2 != null) {
            var1.print(var2);
         }

         var1.println(this.listener.toString() + " [" + this.getListenerString() + "]");
      }

      public int compareTo(ListenerReport.Item var1) {
         int var2 = this.category.compareToIgnoreCase(var1.category);
         if (var2 == 0) {
            var2 = this.getListenerString().compareToIgnoreCase(var1.getListenerString());
         }

         return var2;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof ListenerReport.Item)) {
            return false;
         }

         ListenerReport.Item var2 = (ListenerReport.Item)var1;
         return this.category.equalsIgnoreCase(var2.category) && this.getListenerString().equalsIgnoreCase(var2.getListenerString());
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + this.category.hashCode();
         return 31 * var1 + this.getListenerString().hashCode();
      }

      private String getListenerString() {
         return this.listener.getClass().getName();
      }
   }
}
