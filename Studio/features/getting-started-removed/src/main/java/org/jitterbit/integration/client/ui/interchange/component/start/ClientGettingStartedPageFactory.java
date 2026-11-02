package org.jitterbit.integration.client.ui.interchange.component.start;

import javax.swing.Action;
import org.jitterbit.application.ui.GettingStartedPageFactory;
import org.jitterbit.application.ui.window.editor.Editor;

public final class ClientGettingStartedPageFactory implements GettingStartedPageFactory {
   private final Action newOperationAction;
   private final Action openConnectConsoleAction;

   public ClientGettingStartedPageFactory(Action var1, Action var2) {
      this.newOperationAction = var1;
      this.openConnectConsoleAction = var2;
   }

   @Override
   public boolean isSupported() {
      return true;
   }

   @Override
   public String getId() {
      return "design.getting.started";
   }

   @Override
   public Editor newGettingStartedPage() {
      return new GettingStartedPage(this.newOperationAction, this.openConnectConsoleAction);
   }
}
