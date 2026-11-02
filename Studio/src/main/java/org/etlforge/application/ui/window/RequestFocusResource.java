package org.etlforge.application.ui.window;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import org.etlforge.ui.UiProvider;

public final class RequestFocusResource extends AbstractWindowElementResource {
   private final UiProvider ui;

   public RequestFocusResource(UiProvider var1, WindowElementStateSpan var2) {
      super(var2);
      this.ui = Preconditions.checkNotNull(var1);
   }

   @Override
   public void create(WindowElement var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RequestFocusResource.this.ui.requestFocus();
         }
      });
   }
}
