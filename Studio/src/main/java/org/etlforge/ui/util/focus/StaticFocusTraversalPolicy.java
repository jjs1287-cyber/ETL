package org.etlforge.ui.util.focus;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import org.etlforge.lang.Arrays2;
import org.etlforge.ui.widget.ComponentPredicates;

public final class StaticFocusTraversalPolicy extends FocusTraversalPolicy {
   private final Component defaultComponent;
   private final Component firstComponent;
   private final Component lastComponent;
   private final Component[] componentOrder;

   public StaticFocusTraversalPolicy(Iterable<? extends Component> var1) {
      this(Arrays2.toArray(var1, Component.class));
   }

   public StaticFocusTraversalPolicy(Component[] var1) {
      this(var1[0], var1[0], var1[0], var1);
   }

   public StaticFocusTraversalPolicy(Component var1, Component var2, Component var3, Component[] var4) {
      this.defaultComponent = var1;
      this.firstComponent = var2;
      this.lastComponent = var3;
      this.componentOrder = (Component[])var4.clone();
   }

   @Override
   public Component getComponentAfter(Container var1, Component var2) {
      int var3 = this.indexOf(var2);
      return var3 == -1 ? null : Arrays2.first(this.componentOrder, var3 + 1, ComponentPredicates.ENABLED);
   }

   private int indexOf(Component var1) {
      for (int var2 = 0; var2 < this.componentOrder.length; var2++) {
         if (this.componentOrder[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   @Override
   public Component getComponentBefore(Container var1, Component var2) {
      int var3 = this.indexOf(var2);
      return Arrays2.last(this.componentOrder, var3 - 1, ComponentPredicates.ENABLED);
   }

   @Override
   public Component getFirstComponent(Container var1) {
      if (this.firstComponent == null) {
         return null;
      } else {
         return this.firstComponent.isEnabled() ? this.firstComponent : this.getComponentAfter(var1, this.firstComponent);
      }
   }

   @Override
   public Component getLastComponent(Container var1) {
      if (this.lastComponent == null) {
         return null;
      } else {
         return this.lastComponent.isEnabled() ? this.lastComponent : this.getComponentBefore(var1, this.lastComponent);
      }
   }

   @Override
   public Component getDefaultComponent(Container var1) {
      return this.defaultComponent != null && this.defaultComponent.isEnabled() ? this.defaultComponent : null;
   }
}
