package org.etlforge.application.ui.action.popup;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPopupMenu;
import org.etlforge.ui.util.PopupMenuInvoker;

public class DefaultPopupMenuManager implements PopupMenuManager {
   private final ListMultimap<ContextMenuSection, PopupMenuItemFactory> factories = ArrayListMultimap.create();

   @Override
   public void addFactories(SectionFactories var1) {
      for (PopupMenuItemFactory var3 : var1) {
         this.addFactory(var1.getSection(), var3);
      }
   }

   @Override
   public void addFactory(ContextMenuSection var1, PopupMenuItemFactory var2) {
      Preconditions.checkNotNull(var1, "section");
      Preconditions.checkNotNull(var2, "factory");
      this.factories.get(var1).add(var2);
   }

   @Override
   public void reset() {
      for (PopupMenuItemFactory var2 : this.factories.values()) {
         var2.reset();
      }
   }

   @Override
   public JPopupMenu getPopupMenu(PopupMenuSite var1, List<?> var2) {
      JPopupMenu var3 = null;

      for (PopupMenuItem var5 : this.getItemList(var1, var2)) {
         if (var3 == null) {
            var3 = new JPopupMenu();
         }

         var5.addToPopupMenu(var3);
      }

      return var3;
   }

   public final void preloadMenuItems() {
      PopupMenuSite var1 = new PopupMenuSite() {
         @Override
         public boolean isTree() {
            return false;
         }
      };
      this.getItemList(var1, new ArrayList());
   }

   private Collection<PopupMenuItem> getItemList(PopupMenuSite var1, List<?> var2) {
      LinkedList var3 = Lists.newLinkedList();

      for (ContextMenuSection var7 : ContextMenuSection.values()) {
         for (PopupMenuItemFactory var9 : this.factories.get(var7)) {
            PopupMenuItem var10 = var9.getMenuItem(var1, var2);
            if (var10 != null) {
               var3.add(var10);
            }
         }

         var3.add(PopupMenuSeparatorFactory.FACTORY.getMenuItem(var1, var2));
      }

      if (var3.isEmpty()) {
         return var3;
      }

      trimSeparatorsFromBeginningAndEnd(var3);
      mergeSeparators(var3);
      return var3;
   }

   private static void trimSeparatorsFromBeginningAndEnd(LinkedList<PopupMenuItem> var0) {
      while (!var0.isEmpty() && var0.getFirst() instanceof PopupMenuSeparator) {
         var0.removeFirst();
      }

      while (!var0.isEmpty() && var0.getLast() instanceof PopupMenuSeparator) {
         var0.removeLast();
      }
   }

   private static void mergeSeparators(LinkedList<PopupMenuItem> var0) {
      boolean var1 = false;
      Iterator var2 = var0.iterator();

      while (var2.hasNext()) {
         PopupMenuItem var3 = (PopupMenuItem)var2.next();
         if (var3 instanceof PopupMenuSeparator) {
            if (var1) {
               var2.remove();
            } else {
               var1 = true;
            }
         } else {
            var1 = false;
         }
      }
   }

   @Override
   public void showPopupMenu(PopupMenuSite var1, List<?> var2, PopupMenuInvoker var3) {
      Preconditions.checkNotNull(var3, "invoker");
      this.showPopupMenuImpl(var1, var2, var3);
   }

   private void showPopupMenuImpl(final PopupMenuSite var1, final List<?> var2, final PopupMenuInvoker var3) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            JPopupMenu var1x = DefaultPopupMenuManager.this.getPopupMenu(var1, var2);
            var3.showPopupMenu(var1x);
         }
      });
   }

   @Override
   public void showPopupMenu(PopupMenuSite var1, List<?> var2, Component var3, int var4, int var5) {
      this.showPopupMenuImpl(var1, var2, var3, var4, var5);
   }

   private void showPopupMenuImpl(final PopupMenuSite var1, final List<?> var2, final Component var3, final int var4, final int var5) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            JPopupMenu var1x = DefaultPopupMenuManager.this.getPopupMenu(var1, var2);
            var1x.show(var3, var4, var5);
         }
      });
   }
}
