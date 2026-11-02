package org.etlforge.application.ui.action.popup;

import java.awt.Component;
import java.util.List;
import org.etlforge.ui.util.PopupMenuInvoker;

public interface PopupMenuDisplayer {
   void showPopupMenu(PopupMenuSite var1, List<?> var2, Component var3, int var4, int var5);

   void showPopupMenu(PopupMenuSite var1, List<?> var2, PopupMenuInvoker var3);
}
