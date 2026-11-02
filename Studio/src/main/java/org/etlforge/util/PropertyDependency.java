package org.etlforge.util;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.system.Disposable;

public final class PropertyDependency<S, T> {
   private final Property<S> source;
   private final Property<T> target;
   private final Function<S, T> valueTransform;
   private final PropertyChangeListener listener;

   public static <S, T> Disposable install(Property<S> var0, Property<T> var1, Function<S, T> var2) {
      PropertyDependency var3 = new PropertyDependency(var0, var1, var2);
      return var3.asDisposable();
   }

   public static <S, T> Disposable installAndSync(Property<S> var0, Property<T> var1, Function<S, T> var2) {
      PropertyDependency var3 = new PropertyDependency(var0, var1, var2);
      var3.updateTarget(var0.get());
      return var3.asDisposable();
   }

   private PropertyDependency(Property<S> var1, Property<T> var2, Function<S, T> var3) {
      this.source = Preconditions.checkNotNull(var1, "source");
      this.target = Preconditions.checkNotNull(var2, "target");
      this.valueTransform = Preconditions.checkNotNull(var3, "valueTransform");
      this.listener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            S var2x = (S)var1.getNewValue();
            PropertyDependency.this.updateTarget(var2x);
         }
      };
      var1.addListener(this.listener);
   }

   private void updateTarget(S var1) {
      Object var2 = this.valueTransform.apply((S)var1);
      this.target.set((T)var2);
   }

   private Disposable asDisposable() {
      return new Disposable() {
         @Override
         public void dispose() {
            PropertyDependency.this.source.removeListener(PropertyDependency.this.listener);
         }
      };
   }
}
