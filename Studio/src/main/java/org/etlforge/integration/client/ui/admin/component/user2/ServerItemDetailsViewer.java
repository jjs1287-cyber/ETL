package org.etlforge.integration.client.ui.admin.component.user2;

import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;

public interface ServerItemDetailsViewer extends UiProvider, Disposable {
   void displayUser(User var1);

   User getDisplayedUser();

   void displayGroup(Group var1);

   Group getDisplayedGroup();

   void clear();

   void apply();

   void addChangeListener(InputChangeListener var1);
}
