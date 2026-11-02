package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.Action;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;

public interface UserConsoleView extends UiProvider, Disposable {
   UserConsoleModel getModel();

   UsersAndGroupsViewer getUsersAndGroupsViewer();

   void setDetailsViewer(ServerItemDetailsViewer var1);

   ServerItemDetailsViewer getDetailsViewer();

   void setModelActions(Action[] var1);

   void setServerActions(Action[] var1);

   void setViewActions(Action[] var1);

   void addChangeListener(InputChangeListener var1);
}
