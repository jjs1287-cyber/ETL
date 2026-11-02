package org.etlforge.application.ui.action;

import com.google.common.base.Preconditions;
import org.etlforge.ui.resources.UiResourceBundle;

public final class ActionDictionaries {
   public static ActionDictionary get(Class<?> var0) {
      return get(var0, null);
   }

   public static ActionDictionary get(Class<?> var0, ActionDictionary var1) {
      Preconditions.checkArgument(var0.isAnnotationPresent(ActionStrings.class), "The dictionary class is not annotated with ActionStrings");
      Preconditions.checkArgument(var0.isAnnotationPresent(ActionIcons.class), "The dictionary class is not annotated with ActionIcons");
      String var2 = var0.getAnnotation(ActionStrings.class).value();
      String var3 = var0.getAnnotation(ActionIcons.class).value();
      return NewResourceBasedActionDictionary.newDictionary(var2, UiResourceBundle.MNEMONIC_FACTORY, var3, var1);
   }

   private ActionDictionaries() {
   }
}
