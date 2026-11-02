package org.etlforge.ui.widget.tabbedpane;

import java.util.EventListener;

public interface KongaTabbedPaneCloseHandler extends EventListener {
   void closeCurrentTabRequested(KongaTabbedPaneCloseEvent var1);

   void closeOtherTabsRequested(KongaTabbedPaneCloseEvent var1);

   void closeAllTabsRequested(KongaTabbedPaneCloseEvent var1);
}
