package org.etlforge.integration.client.ui.admin.core.console;

import javax.swing.Icon;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public interface AdminConsolePart extends UiProvider, Disposable {
   String getId();

   String getTitle();

   Icon getIcon();

   void populatePopupMenu(KongaPopupMenu var1);

   void setActive(boolean var1);

   void serverConnectionChanged(ServerConnectionEvent var1);
}
