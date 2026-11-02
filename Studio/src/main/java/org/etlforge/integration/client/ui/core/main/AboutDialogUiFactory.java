package org.etlforge.integration.client.ui.core.main;

import com.google.common.base.Supplier;
import javax.swing.JComponent;
import org.etlforge.ui.UiProvider;

public final class AboutDialogUiFactory implements Supplier<UiProvider> {
   private final Supplier<JComponent> logoFactory;

   public AboutDialogUiFactory(Supplier<JComponent> var1) {
      this.logoFactory = var1;
   }

   public UiProvider get() {
      JComponent var1 = this.logoFactory.get();
      return new AboutDialogUi(var1);
   }
}
