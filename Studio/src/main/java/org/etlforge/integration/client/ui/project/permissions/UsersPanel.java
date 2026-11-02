package org.etlforge.integration.client.ui.project.permissions;

import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.LabelDecorators;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.user.UpdateUserManagerCallback;
import org.etlforge.integration.client.server.user.UserManager;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.ListUtils;
import org.etlforge.util.name.NameSorter;

final class UsersPanel implements UiProvider {
   private final PermissionsViewImpl view;
   private final PermissionsCache cache;
   private final UserManager userManager;
   private KongaList userAndGroupList;
   private JButton refreshButton;
   private final PermissionsPanel permissionsPanel;
   private Role currentSelection;
   private JPanel displayer;

   public UsersPanel(PermissionsViewImpl var1, PermissionsCache var2, UserManager var3, PermissionsPanel var4) {
      this.view = var1;
      this.cache = var2;
      this.userManager = var3;
      this.permissionsPanel = var4;
      this.createList();
      this.createRefreshButton();
      this.layoutPanel();
      this.setActive(false);
   }

   public void setActive(boolean var1) {
      UiUtils.setEnabledRecursively(this.displayer, var1);
   }

   public boolean hasSelection() {
      return this.currentSelection != null;
   }

   public Role getSelectedUserOrGroup() {
      return this.currentSelection;
   }

   public User getSelectedUser() {
      return this.currentSelection instanceof User ? (User)this.currentSelection : null;
   }

   public Group getSelectedGroup() {
      return this.currentSelection instanceof Group ? (Group)this.currentSelection : null;
   }

   public void addSelectionListener(SelectionListener var1) {
      this.userAndGroupList.addSelectionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.userAndGroupList.requestFocus();
   }

   public void updateList() {
      final ListModel var1 = this.getListModel();
      Runnable var2 = new Runnable() {
         @Override
         public void run() {
            UsersPanel.this.userAndGroupList.setModel(var1);
         }
      };
      UiUtils.runOnEventThread(var2);
   }

   private void createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setCellRenderer(new UsersPanel.ListRenderer());
      var1.setVisibleRowCount(10);
      var1.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      this.userAndGroupList = var1.newList(this.getListModel());
      this.userAndGroupList.addSelectionListener(new UsersPanel.ListSelectionHandler());
   }

   private ListModel getListModel() {
      return ListUtils.createModel(this.getListItems());
   }

   private List<UsersPanel.ListItem> getListItems() {
      ArrayList var1 = Lists.newArrayList();
      PermissionsManager var2 = this.view.getPermissions();
      if (var2 != null) {
         this.addUserItems(var1);
         this.addGroupItems(var1);
      }

      return var1;
   }

   private void addUserItems(List<UsersPanel.ListItem> var1) {
      var1.add(this.getUserHeader());
      ArrayList var2 = Lists.newArrayList(this.userManager.getAllUsers());
      Collections.sort(var2, NameSorter.naturalOrdering());

      for (Object __etlItem148 : var2) {
         User var4 = (User) __etlItem148;
         var1.add(new UsersPanel.ServerItemListEntry(var4));
      }
   }

   private void addGroupItems(List<UsersPanel.ListItem> var1) {
      var1.add(this.getGroupHeader());
      ArrayList var2 = Lists.newArrayList(this.userManager.getAllGroups());
      Collections.sort(var2, NameSorter.naturalOrdering());

      for (Object __etlItem158 : var2) {
         Group var4 = (Group) __etlItem158;
         var1.add(new UsersPanel.ServerItemListEntry(var4));
      }
   }

   private UsersPanel.HeaderItem getUserHeader() {
      return new UsersPanel.HeaderItem(ClientIcons.USER_16, Strings.get("Permissions.Label.Users"), Empty.border(1, 1, 3, 1));
   }

   private UsersPanel.HeaderItem getGroupHeader() {
      return new UsersPanel.HeaderItem(ClientIcons.GROUP_16, Strings.get("Permissions.Label.Groups"), Empty.border(5, 1, 3, 1));
   }

   private void createRefreshButton() {
      this.refreshButton = new KongaButton(LabelDecorators.REFRESH_BUTTON);
      this.refreshButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            UsersPanel.this.updateUsersList();
         }
      });
      StyleSheet.LinkButton.makeOver(this.refreshButton);
      this.refreshButton.setContentAreaFilled(false);
      ButtonUtils.enableRolloverForButton(this.refreshButton);
   }

   private void updateUsersList() {
      WaitLock var1 = this.view.getWindow().startWait();
      UsersPanel.UpdateUserAndGroupCallback var2 = new UsersPanel.UpdateUserAndGroupCallback(var1);
      UserAdmin var3 = IntegrationServer.getInstance().getServerCall(UserAdmin.class);
      this.userManager.updateUsersAndGroups(var3, false, var2);
   }

   private void layoutPanel() {
      this.displayer = new JPanel(new BorderLayout(0, 5)) {
         @Override
         public Dimension getMaximumSize() {
            return this.getPreferredSize();
         }
      };
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.add(Strings.get("Permissions.Label.Users&Groups")).glue().add(this.refreshButton);
      this.displayer.add(var1.container(), "North");
      ContainerCanvas.decorate(this.userAndGroupList, this.displayer, "Center");
   }

   private static class HeaderItem implements UsersPanel.ListItem {
      private final Icon icon;
      private final String text;
      private final Border border;

      public HeaderItem(Icon var1, String var2, Border var3) {
         this.icon = var1;
         this.text = var2;
         this.border = var3;
      }

      @Override
      public Component decorateRenderer(JLabel var1) {
         Fonts.convertToBold(var1);
         var1.setIcon(this.icon);
         var1.setBorder(this.border);
         return var1;
      }

      @Override
      public String toString() {
         return this.text;
      }
   }

   private interface ListItem {
      Component decorateRenderer(JLabel var1);
   }

   private static class ListRenderer extends DefaultListCellRenderer {
      private ListRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         return var2 instanceof UsersPanel.ListItem ? ((UsersPanel.ListItem)var2).decorateRenderer(this) : this;
      }
   }

   private class ListSelectionHandler implements SelectionListener {
      private ListSelectionHandler() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         Object var2 = ((OrderedSelection)var1.getSelection()).head();
         if (this.shouldCurrentSelectionBeCached(var2)) {
            this.cacheCurrentSelection();
         }

         this.updateCurrentSelection(var2);
      }

      private boolean shouldCurrentSelectionBeCached(Object var1) {
         return UsersPanel.this.currentSelection != null && var1 != UsersPanel.this.currentSelection && UsersPanel.this.permissionsPanel.isDirty();
      }

      private void cacheCurrentSelection() {
         UsersPanel.this.cache.cachePermissions(UsersPanel.this.currentSelection, UsersPanel.this.permissionsPanel.getPermissions());
      }

      private void updateCurrentSelection(Object var1) {
         if (var1 instanceof UsersPanel.ServerItemListEntry) {
            Role var2 = ((UsersPanel.ServerItemListEntry)var1).getEntity();
            this.updateCurrentSelection(var2);
         } else {
            UsersPanel.this.currentSelection = null;
         }
      }

      private void updateCurrentSelection(Role var1) {
         UsersPanel.this.permissionsPanel.update(var1, UsersPanel.this.cache.getPermissions(var1));
         UsersPanel.this.currentSelection = var1;
      }
   }

   private static class ServerItemListEntry implements UsersPanel.ListItem {
      private static final Border BORDER = Empty.border(1, 30, 1, 1);
      private final Role item;

      public ServerItemListEntry(Role var1) {
         this.item = var1;
      }

      public Role getEntity() {
         return this.item;
      }

      @Override
      public Component decorateRenderer(JLabel var1) {
         var1.setBorder(BORDER);
         return var1;
      }

      @Override
      public String toString() {
         return this.item.getName();
      }
   }

   private class UpdateUserAndGroupCallback implements UpdateUserManagerCallback {
      private final WaitLock wait;

      public UpdateUserAndGroupCallback(WaitLock var2) {
         this.wait = var2;
      }

      @Override
      public void cancelled() {
         this.wait.release();
      }

      @Override
      public void failed(Throwable var1) {
         this.wait.release();
      }

      @Override
      public void updateCompleted(ServerInfo var1, List<User> var2, List<Group> var3) {
         this.wait.release();
         UsersPanel.this.updateList();
      }
   }
}
