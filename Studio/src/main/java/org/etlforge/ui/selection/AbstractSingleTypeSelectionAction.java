package org.etlforge.ui.selection;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Icon;

public abstract class AbstractSingleTypeSelectionAction<T> extends AbstractSelectionAction {
   private final Class<T> objectClass;
   private List<T> selected = Lists.newArrayList();
   private final LinkedList<Predicate<List<T>>> selectionPredicates;

   protected AbstractSingleTypeSelectionAction(Class<T> var1) {
      this(var1, null, null);
   }

   protected AbstractSingleTypeSelectionAction(Class<T> var1, String var2) {
      this(var1, var2, null);
   }

   protected AbstractSingleTypeSelectionAction(Class<T> var1, String var2, Icon var3) {
      super(var2, var3);
      Preconditions.checkNotNull(var1, "clazz");
      this.objectClass = var1;
      this.selectionPredicates = Lists.newLinkedList();
      this.selectionPredicates.add(new AbstractSingleTypeSelectionAction.StandardSelectionPredicate());
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      if (this.selected.isEmpty()) {
         this.setEnabled(false);
      } else {
         this.run(this.selected);
      }
   }

   protected abstract void run(List<T> var1);

   protected final List<T> getSelectedObjects() {
      return this.selected;
   }

   @Override
   protected final void handleSelectionChange() {
      this.selected = this.updateSelectedObjects();
      boolean var1 = this.isEnabledFor(this.selected);
      this.setEnabled(var1);
   }

   public final void addSelectionPredicate(Predicate<List<T>> var1) {
      this.selectionPredicates.addFirst(var1);
   }

   public final void removeSelectionPredicate(Predicate<List<T>> var1) {
      this.selectionPredicates.remove(var1);
   }

   protected boolean isEnabledFor(List<T> var1) {
      for (Predicate var3 : this.selectionPredicates) {
         if (!var3.apply(var1)) {
            return false;
         }
      }

      return true;
   }

   protected boolean isEnabledFor(T var1) {
      return true;
   }

   private List<T> updateSelectedObjects() {
      ArrayList var1 = Lists.newArrayList();

      for (Object var3 : this.getSelection().items()) {
         if (!this.objectClass.isInstance(var3)) {
            var1.clear();
            break;
         }

         var1.add(this.objectClass.cast(var3));
      }

      return var1;
   }

   private class StandardSelectionPredicate implements Predicate<List<T>> {
      private StandardSelectionPredicate() {
      }

      public boolean apply(List<T> var1) {
         if (var1.isEmpty()) {
            return false;
         }

         for (Object var3 : var1) {
            if (!AbstractSingleTypeSelectionAction.this.isEnabledFor((T)var3)) {
               return false;
            }
         }

         return true;
      }
   }
}
