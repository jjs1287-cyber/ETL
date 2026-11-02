package org.etlforge.ui.property;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import javax.swing.Action;
import org.etlforge.functors.function.KongaFunctions;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ActionEnabledBinding<T> extends AbstractBinding<T> {
   private final Action action;
   private final Function<? super T, Boolean> valueTransform;

   public static Binding forBoolean(Action var0, AbstractPropertyBasedModel var1, String var2) {
      return new ActionEnabledBinding<>(var0, var1, var2, Functions.identity());
   }

   public static Binding forBoolean(Action var0, Property<Boolean> var1) {
      return new ActionEnabledBinding<>(var0, var1, Functions.identity());
   }

   public static Binding notNull(Action var0, AbstractPropertyBasedModel var1, String var2) {
      return new ActionEnabledBinding<>(var0, var1, var2, KongaFunctions.notNull());
   }

   public static Binding notNull(Action var0, Property<?> var1) {
      return new ActionEnabledBinding<Object>(var0, (Property<Object>)var1, KongaFunctions.notNull());
   }

   public ActionEnabledBinding(Action var1, AbstractPropertyBasedModel var2, String var3, Function<? super T, Boolean> var4) {
      super(var2, var3);
      this.action = var1;
      this.valueTransform = var4;
   }

   public ActionEnabledBinding(Action var1, Property<T> var2, Function<? super T, Boolean> var3) {
      super(var2);
      this.action = var1;
      this.valueTransform = var3;
   }

   @Override
   protected void updateUiWithValue(T var1) {
      Boolean var2 = this.valueTransform.apply((T)var1);
      this.action.setEnabled(var2);
   }
}

