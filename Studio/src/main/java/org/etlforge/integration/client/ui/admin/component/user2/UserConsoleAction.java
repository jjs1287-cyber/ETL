package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.Action;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public interface UserConsoleAction extends Action {
   void setModel(UserConsoleModel var1);

   void setController(UserConsoleController var1);

   void setOverlayContainer(OverlayContainer var1);
}
