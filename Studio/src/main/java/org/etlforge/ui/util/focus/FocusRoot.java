package org.etlforge.ui.util.focus;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.event.FocusListener;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;

public final class FocusRoot implements Focusable, Disposable {
   private final Component root;
   private final KongaFocusManager manager = new KongaFocusManager();
   private final FocusDispatcher dispatcher = new FocusDispatcher();
   private UiProvider focusReceiver;

   public static FocusRoot of(Component var0) {
      return new FocusRoot(var0);
   }

   public FocusRoot(Component var1) {
      this.root = Preconditions.checkNotNull(var1);
      this.manager.addFocusRoot(var1, this.dispatcher);
      this.manager.register();
   }

   @Override
   public void dispose() {
      this.manager.unregister();
   }

   public FocusRoot withFocusReceiver(Component var1) {
      this.focusReceiver = var1 == null ? null : new UiProviderAdapter(var1);
      return this;
   }

   public FocusRoot withFocusReceiver(UiProvider var1) {
      this.focusReceiver = var1;
      return this;
   }

   @Override
   public void requestFocus() {
      if (this.focusReceiver != null) {
         this.focusReceiver.requestFocus();
      } else {
         this.root.requestFocusInWindow();
      }
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.dispatcher.addReceiver(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.dispatcher.removeReceiver(var1);
   }
}
