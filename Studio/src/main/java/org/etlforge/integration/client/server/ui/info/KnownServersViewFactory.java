package org.etlforge.integration.client.server.ui.info;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.server.ServerManager;

public final class KnownServersViewFactory implements WindowSectionContentProvider {
   private final ServerManager serverManager;

   public KnownServersViewFactory(ServerManager var1) {
      Preconditions.checkNotNull(var1, "serverManager");
      this.serverManager = var1;
   }

   @Override
   public WindowSectionContent getContent(WindowSection var1) {
      return new KnownServersViewImpl(this.serverManager, var1);
   }

   @Override
   public String getContentId() {
      return KnownServersView.CONTENT_ID;
   }
}
