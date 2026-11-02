package org.etlforge.integration.client.server.ui.user;

import java.awt.datatransfer.Transferable;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

public class GroupMemberList implements UiProvider {
   private final KongaList list;
   private Group group;
   private boolean dropEnabled;

   public GroupMemberList() {
      this(5);
   }

   public GroupMemberList(int var1) {
      this.list = this.createList(this.createModel(), var1);
      this.list.setEnabled(false);
   }

   public void setGroup(Group var1) {
      this.group = var1;
      this.update();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   public void setDropEnabled(boolean var1) {
      this.dropEnabled = var1;
   }

   public void update() {
      final ListModel var1 = this.createModel();
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            GroupMemberList.this.list.setModel(var1);
            GroupMemberList.this.list.setEnabled(GroupMemberList.this.group != null);
         }
      });
   }

   public void repaint() {
      this.list.repaint();
   }

   public MutableSelectionSource asSelectionSource() {
      return this.list;
   }

   private ListModel createModel() {
      DefaultListModel var1 = new DefaultListModel();
      if (this.group != null) {
         for (User var3 : this.group.getMembers()) {
            var1.addElement(new UserListUi(var3));
         }
      }

      return var1;
   }

   private KongaList createList(ListModel var1, int var2) {
      KongaListFactory var3 = ComponentFactories.newKongaListFactory();
      var3.setVisibleRowCount(var2);
      var3.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXX");
      var3.setTransferHandler(new GroupMemberList.DropHandler());
      var3.setSelectionConverter(new SelectionConverter() {
         @Override
         public Object fromSelection(Object var1) {
            return ((UserListUi)var1).getUser();
         }

         @Override
         public Object toSelection(Object var1) {
            return var1 instanceof User ? new UserListUi((User)var1) : null;
         }
      });
      return var3.newList(var1);
   }

   private class DropHandler extends TransferHandler {
      private DropHandler() {
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         return GroupMemberList.this.dropEnabled && GroupMemberList.this.group != null && var1.isDataFlavorSupported(TransferableUsers.FLAVOR);
      }

      @Override
      public boolean importData(TransferSupport var1) {
         try {
            Transferable var2 = var1.getTransferable();

            for (Object __etlItem117 : (List)var2.getTransferData(TransferableUsers.FLAVOR)) {
               UserUi var5 = (UserUi) __etlItem117;
               GroupMemberList.this.group.addMember(var5.getUser());
            }

            return true;
         } catch (Exception var6) {
            ErrorLog.attention(GroupMemberList.class, "An unexpected error occurred when completing the drop: " + var6.getMessage(), var6);
            return false;
         }
      }
   }
}
