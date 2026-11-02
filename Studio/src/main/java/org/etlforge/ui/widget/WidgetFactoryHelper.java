package org.etlforge.ui.widget;

import javax.swing.JComponent;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringFactory;

public final class WidgetFactoryHelper<T extends JComponent> {
   private final MnemonicStringFactory mnemonicFactory;
   private final ComponentCustomizer<? super T> customizer;

   public WidgetFactoryHelper() {
      this(null, null);
   }

   public WidgetFactoryHelper(MnemonicStringFactory var1, ComponentCustomizer<? super T> var2) {
      this.mnemonicFactory = var1 != null ? var1 : MnemonicStringFactory.NO_MNEMONIC_FACTORY;
      this.customizer = var2;
   }

   public MnemonicString getLabel(String var1) {
      return this.mnemonicFactory.fromString(var1);
   }

   public final T customize(T var1) {
      if (this.customizer != null) {
         this.customizer.customize((T)var1);
      }

      return (T)var1;
   }
}
