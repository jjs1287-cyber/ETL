package org.etlforge.application.ui.window;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;

public final class ActiveObjectProviderResource extends AbstractWindowElementResource {
   private final ActiveObjectService service;
   private final ActiveObjectProvider provider;

   public ActiveObjectProviderResource(ActiveObjectService var1, ActiveObjectProvider var2) {
      this(var1, var2, WindowElementStateSpan.WHEN_OPEN);
   }

   public ActiveObjectProviderResource(ActiveObjectService var1, ActiveObjectProvider var2, WindowElementStateSpan var3) {
      super(var3);
      Preconditions.checkNotNull(var1, "service");
      Preconditions.checkNotNull(var2, "provider");
      this.service = var1;
      this.provider = var2;
   }

   @Override
   public void create(WindowElement var1) {
      this.service.addActiveObjectProvider(this.provider);
   }

   @Override
   public void dispose(WindowElement var1) {
      this.service.removeActiveObjectProvider(this.provider);
   }
}
