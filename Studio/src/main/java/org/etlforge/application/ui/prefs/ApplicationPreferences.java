package org.etlforge.application.ui.prefs;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public final class ApplicationPreferences {
   private final ApplicationPreferences.Node root;
   private final Map<ApplicationPreferences.Node, PreferencesPage> pages;
   private final Comparator<String> pageOrder;

   public ApplicationPreferences() {
      this(new DefaultPageOrder());
   }

   public ApplicationPreferences(Comparator<String> var1) {
      this.pageOrder = var1;
      this.root = new ApplicationPreferences.Node(ApplicationPreferences.class.getName() + ".Root", null, var1);
      this.pages = Maps.newHashMap();
   }

   public ApplicationPreferences(List<? extends PreferencesPage> var1) {
      this(var1, new DefaultPageOrder());
   }

   public ApplicationPreferences(List<? extends PreferencesPage> var1, Comparator<String> var2) {
      this(var2);

      for (PreferencesPage var4 : var1) {
         this.addPage(var4);
      }
   }

   public void addPage(PreferencesPage var1) throws DuplicateNodeException {
      ApplicationPreferences.Node var2 = this.getNodeFromPath(var1.getPath());
      if (this.pages.containsKey(var2)) {
         throw new DuplicateNodeException(var1.getPath());
      }

      this.pages.put(var2, var1);
   }

   public ApplicationPreferences.Node getRoot() {
      return this.root;
   }

   public PreferencesPage getPage(ApplicationPreferences.Node var1) {
      return this.pages.get(var1);
   }

   public ApplicationPreferences.Node getNodeFromPath(String[] var1) {
      int var2 = var1.length;
      Preconditions.checkArgument(var2 > 0, "PreferencesPage.getPath() may not be empty");
      ApplicationPreferences.Node var3 = this.root;

      for (int var4 = 0; var4 < var2; var4++) {
         ApplicationPreferences.Node var5 = var3.getChild(var1[var4]);
         if (var5 == null) {
            var5 = new ApplicationPreferences.Node(var1[var4], var3, this.pageOrder);
            var3.addChild(var5);
         }

         if (var4 == var2 - 1) {
            return var5;
         }

         var3 = var5;
      }

      throw new RuntimeException("Should never reach this point");
   }

   public static final class Node {
      private final String name;
      private final SortedMap<String, ApplicationPreferences.Node> children;
      private final ApplicationPreferences.Node parent;

      private Node(String var1, ApplicationPreferences.Node var2, Comparator<String> var3) {
         this.name = var1;
         this.parent = var2;
         this.children = new TreeMap<>(var3);
      }

      public String getName() {
         return this.name;
      }

      public ApplicationPreferences.Node[] getChildren() {
         return this.children.values().toArray(new ApplicationPreferences.Node[this.children.size()]);
      }

      public String[] getPath() {
         LinkedList var1 = new LinkedList();
         var1.add(this.name);

         for (ApplicationPreferences.Node var2 = this.parent; var2 != null && var2.parent != null; var2 = var2.parent) {
            var1.addFirst(var2.name);
         }

         return (String[]) var1.toArray(new String[var1.size()]);
      }

      @Override
      public String toString() {
         return this.name;
      }

      ApplicationPreferences.Node getParent() {
         return this.parent;
      }

      private void addChild(ApplicationPreferences.Node var1) {
         this.children.put(var1.name, var1);
      }

      private ApplicationPreferences.Node getChild(String var1) {
         return this.children.get(var1);
      }
   }
}
