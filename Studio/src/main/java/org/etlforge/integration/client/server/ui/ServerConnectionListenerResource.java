package org.etlforge.integration.client.server.ui;

import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;

public final class ServerConnectionListenerResource extends AbstractWindowElementResource {
   private final ServerConnectionService service;
   private final ServerConnectionListener listener;

   public ServerConnectionListenerResource(ServerConnectionService var1, ServerConnectionListener var2, WindowElementStateSpan var3) {
      super(var3);
      this.service = var1;
      this.listener = var2;
   }

   public ServerConnectionListenerResource(ServerConnectionService var1, ServerConnectionListener var2) {
      this(var1, var2, WindowElementStateSpan.WHEN_OPEN);
   }

   @Override
   public void create(WindowElement var1) {
      this.service.addServerConnectionListener(this.listener);
   }

   @Override
   public void dispose(WindowElement var1) {
      this.service.removeServerConnectionListener(this.listener);
   }
}
