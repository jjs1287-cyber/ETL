package org.jitterbit.integration.client.ui.interchange.component.start;

import java.awt.event.ActionListener;
import javax.swing.Icon;
import org.jitterbit.application.ui.resources.CommonIcons;
import org.jitterbit.application.ui.window.editor.EditorHistoryPoint;
import org.jitterbit.application.ui.window.editor.EditorService;

public final class GettingStartedPageHistoryPoint implements EditorHistoryPoint {
   private ActionListener newOperationAction;
   private ActionListener startConnectAction;
   private boolean disposed;

   public GettingStartedPageHistoryPoint(ActionListener var1, ActionListener var2) {
      this.newOperationAction = var1;
      this.startConnectAction = var2;
   }

   @Override
   public String getId() {
      return "design.getting.started";
   }

   @Override
   public boolean isAvailable() {
      return true;
   }

   @Override
   public void dispose() {
      this.newOperationAction = null;
      this.startConnectAction = null;
      this.disposed = true;
   }

   @Override
   public boolean isDisposed() {
      return this.disposed;
   }

   @Override
   public String getDisplayString() {
      return "Getting Started";
   }

   @Override
   public Icon getIcon() {
      return CommonIcons.GETTING_STARTED_16;
   }

   @Override
   public void restore(EditorService var1) {
      GettingStartedPage var2 = new GettingStartedPage(this.newOperationAction, this.startConnectAction);
      var1.openEditor(var2);
   }
}
