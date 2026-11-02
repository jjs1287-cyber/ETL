package org.etlforge.ui;

import com.google.common.base.Preconditions;
import java.awt.Component;
import javax.swing.Action;

public final class Enablabes {
   private Enablabes() {
   }

   public static Enablable fromComponent(Component var0) {
      return new Enablabes.ComponentAdapter(var0);
   }

   public static Enablable fromAction(Action var0) {
      return new Enablabes.ActionAdapter(var0);
   }

   private static class ActionAdapter extends Enablabes.Wrapper<Action> {
      public ActionAdapter(Action var1) {
         super(var1);
      }

      @Override
      public boolean isEnabled() {
         return this.getWrappee().isEnabled();
      }

      @Override
      public void setEnabled(boolean var1) {
         this.getWrappee().setEnabled(var1);
      }
   }

   private static class ComponentAdapter extends Enablabes.Wrapper<Component> {
      public ComponentAdapter(Component var1) {
         super(var1);
      }

      @Override
      public boolean isEnabled() {
         return this.getWrappee().isEnabled();
      }

      @Override
      public void setEnabled(boolean var1) {
         this.getWrappee().setEnabled(var1);
      }
   }

   private abstract static class Wrapper<T> implements Enablable {
      private final T wrappee;

      public Wrapper(T var1) {
         this.wrappee = Preconditions.checkNotNull((T)var1);
      }

      public final T getWrappee() {
         return this.wrappee;
      }

      @Override
      public final boolean equals(Object var1) {
         return var1 == this ? true : var1 instanceof Enablabes.Wrapper && this.wrappee.equals(((Enablabes.Wrapper)var1).wrappee);
      }

      @Override
      public final int hashCode() {
         return this.wrappee.hashCode();
      }
   }
}
