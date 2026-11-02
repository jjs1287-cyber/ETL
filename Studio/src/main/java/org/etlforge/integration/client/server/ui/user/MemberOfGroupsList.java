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
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

public class MemberOfGroupsList implements UiProvider {
   private final KongaList list;
   private User user;
   private boolean dropEnabled;

   public MemberOfGroupsList() {
      this(8);
   }

   public MemberOfGroupsList(int var1) {
      this.list = this.createList(this.createModel(), var1);
      this.list.setEnabled(false);
   }

   public void setUser(User var1) {
      this.user = var1;
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
            MemberOfGroupsList.this.list.setModel(var1);
            MemberOfGroupsList.this.list.setEnabled(MemberOfGroupsList.this.user != null);
         }
      });
   }

   public MutableSelectionSource asSelectionSource() {
      return this.list;
   }

   public void repaint() {
      this.list.repaint();
   }

   private ListModel createModel() {
      DefaultListModel var1 = new DefaultListModel();
      if (this.user != null) {
         for (Group var3 : this.user.getGroups()) {
            var1.addElement(new GroupListUi(var3));
         }
      }

      return var1;
   }

   private KongaList createList(ListModel var1, int var2) {
      KongaListFactory var3 = ComponentFactories.newKongaListFactory();
      var3.setVisibleRowCount(var2);
      var3.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXX");
      var3.setTransferHandler(new MemberOfGroupsList.DropHandler());
      return var3.newList(var1);
   }

   private class DropHandler extends TransferHandler {
      private DropHandler() {
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         return MemberOfGroupsList.this.dropEnabled && MemberOfGroupsList.this.user != null && var1.isDataFlavorSupported(TransferableGroups.FLAVOR);
      }

      @Override
      public boolean importData(TransferSupport var1) {
         try {
            Transferable var2 = var1.getTransferable();

            for (Object __etlItem105 : (List)var2.getTransferData(TransferableGroups.FLAVOR)) {
               GroupUi var5 = (GroupUi) __etlItem105;
               var5.getGroup().addMember(MemberOfGroupsList.this.user);
            }

            return true;
         } catch (Exception var6) {
            ErrorLog.attention(MemberOfGroupsList.class, "An unexpected error occurred when completing the drop: " + var6.getMessage(), var6);
            return false;
         }
      }
   }
}
