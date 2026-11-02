package org.etlforge.ui.util.focus;

import java.awt.event.FocusListener;
import org.etlforge.ui.UiProvider;

public abstract class MuteFocusable implements Focusable {
   public static Focusable fromUiProvider(final UiProvider var0) {
      return new MuteFocusable() {
         @Override
         public void requestFocus() {
            var0.requestFocus();
         }
      };
   }

   @Override
   public final void addFocusListener(FocusListener var1) {
   }

   @Override
   public final void removeFocusListener(FocusListener var1) {
   }
}
