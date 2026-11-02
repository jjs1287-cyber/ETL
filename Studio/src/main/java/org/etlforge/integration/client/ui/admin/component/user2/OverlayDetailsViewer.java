package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public class OverlayDetailsViewer implements ServerItemDetailsViewer {
   private final OverlayContainer container;
   private final UserDetailsViewer userDetailsViewer;
   private final GroupDetailsViewer groupDetailsViewer;
   private OverlayDecorator currentOverlay;

   public OverlayDetailsViewer(UserConsoleModel var1, OverlayContainer var2) {
      this.container = var2;
      this.userDetailsViewer = new UserDetailsViewer(var1);
      this.groupDetailsViewer = new GroupDetailsViewer(var1);
   }

   @Override
   public void dispose() {
      this.removeOverlay();
      this.userDetailsViewer.dispose();
      this.groupDetailsViewer.dispose();
   }

   @Override
   public void apply() {
      this.userDetailsViewer.apply();
      this.groupDetailsViewer.apply();
   }

   @Override
   public void clear() {
      this.removeOverlay();
      this.displayUser(null);
      this.displayGroup(null);
   }

   @Override
   public void displayGroup(Group var1) {
      this.groupDetailsViewer.setGroup(var1);
      if (var1 != null) {
         this.showOverlay(this.groupDetailsViewer, "Group Details", ClientIcons.GROUP_32, new OverlayDetailsViewer.GroupMemento(var1));
      } else {
         this.removeOverlay();
      }
   }

   @Override
   public void displayUser(User var1) {
      this.userDetailsViewer.setUser(var1);
      if (var1 != null) {
         this.showOverlay(this.userDetailsViewer, "User Details", ClientIcons.USER_PROFILE_32, new OverlayDetailsViewer.UserMemento(var1));
      } else {
         this.removeOverlay();
      }
   }

   @Override
   public Group getDisplayedGroup() {
      return this.groupDetailsViewer.getGroup();
   }

   @Override
   public User getDisplayedUser() {
      return this.userDetailsViewer.getUser();
   }

   @Override
   public void addChangeListener(InputChangeListener var1) {
      this.userDetailsViewer.addChangeListener(var1);
      this.groupDetailsViewer.addChangeListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
   }

   @Override
   public void requestFocus() {
   }

   private void removeOverlay() {
      if (this.currentOverlay != null) {
         this.currentOverlay.hide();
         this.currentOverlay = null;
      }
   }

   private void showOverlay(UiProvider var1, String var2, Icon var3, final OverlayDetailsViewer.Memento var4) {
      this.removeOverlay();
      JPanel var5 = BorderLayoutBuilder.fillWith(var1);
      var5.setBorder(Empty.border(10));
      OverlayDecorator var6 = new OverlayDecorator(var5, var2) {
         @Override
         protected void onCancel() {
            var4.restore();
         }
      };
      var6.setTitleIcon(var3);
      var6.useOkAndCancel();
      var6.setOkAsDefault(true);
      var6.show(this.container, new OverlayCenterPositioner());
      var1.requestFocus();
      this.currentOverlay = var6;
   }

   private static class GroupMemento implements OverlayDetailsViewer.Memento {
      private final Group group;
      private final Group originalProperties;
      private final Set<User> originalMembers;

      public GroupMemento(Group var1) {
         this.group = var1;
         this.originalProperties = new Group(var1);
         this.originalMembers = Sets.newHashSet(var1.getMembers());
      }

      @Override
      public void restore() {
         this.group.restoreProperties(this.originalProperties, this.originalMembers);
      }
   }

   private interface Memento {
      void restore();
   }

   private static class UserMemento implements OverlayDetailsViewer.Memento {
      private final User user;
      private final User originalProperties;
      private final Set<Group> originalGroups;

      public UserMemento(User var1) {
         this.user = var1;
         this.originalProperties = new User(var1);
         this.originalGroups = Sets.newHashSet(var1.getGroups());
      }

      @Override
      public void restore() {
         this.user.restoreProperties(this.originalProperties, this.originalGroups);
      }
   }
}
