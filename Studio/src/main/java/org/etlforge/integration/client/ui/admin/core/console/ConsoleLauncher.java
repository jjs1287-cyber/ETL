package org.etlforge.integration.client.ui.admin.core.console;

import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public interface ConsoleLauncher extends UiProvider {
   String getName();

   void launch();

   void addToContextMenu(KongaPopupMenu var1);
}
