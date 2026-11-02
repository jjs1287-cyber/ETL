package org.etlforge.integration.client.ui.admin.component.user2;

import java.beans.PropertyChangeEvent;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.GroupMemberListener;
import org.etlforge.integration.user.User;

public interface UserConsoleModelListener extends GroupMemberListener {
   void newServerConnectedTo(UserConsoleModel var1);

   void newUserDataDownloaded(UserConsoleModel var1);

   void allDataRemoved(UserConsoleModel var1);

   void userAdded(UserConsoleModel var1, User var2);

   void userRemoved(UserConsoleModel var1, User var2);

   void groupAdded(UserConsoleModel var1, Group var2);

   void groupRemoved(UserConsoleModel var1, Group var2);

   void downloadStatusChanged(UserConsoleModel var1, boolean var2);

   void uploadStatusChanged(UserConsoleModel var1, boolean var2);

   void dirtyStatusChanged(UserConsoleModel var1, boolean var2);

   void activeStatusChanged(UserConsoleModel var1, boolean var2);

   void readOnlyStateChanged(UserConsoleModel var1, boolean var2);

   void userPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2);

   void groupPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2);

   abstract class Adapter implements UserConsoleModelListener {
      @Override
      public void memberAdded(Group var1, User var2) {
      }

      @Override
      public void memberRemoved(Group var1, User var2) {
      }

      @Override
      public void allDataRemoved(UserConsoleModel var1) {
      }

      @Override
      public void dirtyStatusChanged(UserConsoleModel var1, boolean var2) {
      }

      @Override
      public void activeStatusChanged(UserConsoleModel var1, boolean var2) {
      }

      @Override
      public void readOnlyStateChanged(UserConsoleModel var1, boolean var2) {
      }

      @Override
      public void downloadStatusChanged(UserConsoleModel var1, boolean var2) {
      }

      @Override
      public void uploadStatusChanged(UserConsoleModel var1, boolean var2) {
      }

      @Override
      public void groupAdded(UserConsoleModel var1, Group var2) {
      }

      @Override
      public void groupRemoved(UserConsoleModel var1, Group var2) {
      }

      @Override
      public void newServerConnectedTo(UserConsoleModel var1) {
      }

      @Override
      public void newUserDataDownloaded(UserConsoleModel var1) {
      }

      @Override
      public void userAdded(UserConsoleModel var1, User var2) {
      }

      @Override
      public void userRemoved(UserConsoleModel var1, User var2) {
      }

      @Override
      public void groupPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2) {
      }

      @Override
      public void userPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2) {
      }
   }
}
