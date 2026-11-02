package org.etlforge.application.ui.action.popup;

import java.util.List;

public interface PopupMenuItemFactory {
   PopupMenuItem getMenuItem(PopupMenuSite var1, List<?> var2);

   void reset();
}
