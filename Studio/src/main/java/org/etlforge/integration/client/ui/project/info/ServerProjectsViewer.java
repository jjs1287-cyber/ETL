package org.etlforge.integration.client.ui.project.info;

import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.PopupMenuRequestHandler;

public interface ServerProjectsViewer extends UiProvider, ActiveObjectProvider, Disposable {
   void setActive(boolean var1);

   void setContextMenuProvider(PopupMenuRequestHandler var1);

   SelectionSource getSelectionSource();
}
