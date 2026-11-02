package org.etlforge.application.ui.action.popup;

import java.util.List;
import javax.swing.JPopupMenu;

public interface PopupMenuManager extends PopupMenuDisplayer {
   void addFactories(SectionFactories var1);

   void addFactory(ContextMenuSection var1, PopupMenuItemFactory var2);

   void reset();

   JPopupMenu getPopupMenu(PopupMenuSite var1, List<?> var2);
}
