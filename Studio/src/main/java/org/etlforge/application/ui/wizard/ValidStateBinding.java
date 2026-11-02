package org.etlforge.application.ui.wizard;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Strings;
import org.etlforge.functors.function.KongaFunctions;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ValidStateBinding<T> extends AbstractBinding<T> {
   private final AbstractWizardPage page;
   private final Function<? super T, Boolean> valueTransform;

   public static Binding forBoolean(AbstractWizardPage var0, AbstractPropertyBasedModel var1, String var2) {
      return forBoolean(var0, var1.getProperty(var2));
   }

   public static Binding forBoolean(AbstractWizardPage var0, Property<Boolean> var1) {
      return new ValidStateBinding<>(var0, var1, Functions.identity());
   }

   public static Binding notNull(AbstractWizardPage var0, AbstractPropertyBasedModel var1, String var2) {
      return notNull(var0, var1.getProperty(var2));
   }

   public static Binding notNull(AbstractWizardPage var0, Property<?> var1) {
      return new ValidStateBinding<Object>(var0, (Property<Object>)var1, KongaFunctions.notNull());
   }

   public static Binding notEmpty(AbstractWizardPage var0, AbstractPropertyBasedModel var1, String var2) {
      return notEmpty(var0, var1.getProperty(var2));
   }

   public static Binding notEmpty(AbstractWizardPage var0, Property<String> var1) {
      return new ValidStateBinding<>(var0, var1, new Function<String, Boolean>() {
         public Boolean apply(String var1) {
            return !Strings.isNullOrEmpty(var1);
         }
      });
   }

   public ValidStateBinding(AbstractWizardPage var1, Property<T> var2, Function<? super T, Boolean> var3) {
      super(var2);
      this.page = var1;
      this.valueTransform = var3;
   }

   @Override
   protected void updateUiWithValue(T var1) {
      Boolean var2 = this.valueTransform.apply((T)var1);
      this.page.setCanContinue(var2);
   }
}

