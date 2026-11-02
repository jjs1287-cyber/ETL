package org.etlforge.application.ui.window.action;

import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.system.Disposable;

public interface ActiveObjectsAction extends ApplicationAction, ActiveObjectProviderListener, Disposable {
   void setActiveObjects(List<?> var1);

   boolean accepts(Object var1);
}
