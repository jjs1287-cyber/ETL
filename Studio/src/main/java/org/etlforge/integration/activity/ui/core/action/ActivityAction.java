package org.etlforge.integration.activity.ui.core.action;

import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.action.ActiveObjectsAction;
import org.etlforge.application.ui.window.editor.EditorDisplayer;

public interface ActivityAction extends ApplicationAction, ActiveObjectsAction {
   void setPageDisplayer(EditorDisplayer var1);
}
