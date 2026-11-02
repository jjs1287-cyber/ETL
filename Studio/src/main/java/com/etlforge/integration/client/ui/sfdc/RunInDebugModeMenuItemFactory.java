package com.etlforge.integration.client.ui.sfdc;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.ui.widget.menu.KongaCheckBoxMenuItem;

public final class RunInDebugModeMenuItemFactory extends AbstractPopupMenuItemFactory {
   @Override
   protected PopupMenuItem createItem() {
      return new RunInDebugModeMenuItemFactory.PopupMenuItemImpl();
   }

   private static class CheckBox extends KongaCheckBoxMenuItem implements ActionListener {
      private final Operation op;
      private final boolean on;

      public CheckBox(Operation var1) {
         super("Debug Mode");
         this.op = var1;
         OperationOptions var2 = var1.getOperationOptions();
         this.on = var2.getDebugOption().isDebugModeEnabled();
         this.setSelected(this.on);
         String var3 = "Turn " + (this.on ? "off" : "on") + " debug mode when running this operation.";
         this.setToolTipText(var3);
         this.addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckBox.this.toggleDebugMode();
            }
         });
      }

      private void toggleDebugMode() {
         OperationOptions var1 = this.op.getOperationOptions();

         try {
            this.applyNewOptions(var1);
            this.save();
         } catch (InterchangeSavingException var3) {
            Alert.error(ApplicationUi.getAppWinFrame(), var3.getMessage(), "Error");
            this.op.setOperationOptions(var1);
         }
      }

      private void applyNewOptions(OperationOptions var1) {
         OperationOptions var2 = var1.withDebugOption(!this.on);
         this.op.setOperationOptions(var2);
      }

      private void save() throws InterchangeSavingException {
         boolean var1 = this.op.save();
         if (!var1) {
            throw new InterchangeSavingException("The operation could not be saved due to a configuration error. This is a bug.");
         }
      }
   }

   private static class PopupMenuItemImpl implements PopupMenuItem {
      private Operation op;

      private PopupMenuItemImpl() {
      }

      @Override
      public void addToPopupMenu(JPopupMenu var1) {
         RunInDebugModeMenuItemFactory.CheckBox var2 = new RunInDebugModeMenuItemFactory.CheckBox(this.op);
         var1.add(var2);
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         this.op = null;
         if (var2.size() == 1) {
            Object var3 = var2.get(0);
            if (var3 instanceof SalesforceWizardEntity) {
               SalesforceWizardEntity var4 = (SalesforceWizardEntity)var3;
               WizardItemBrowser var5 = new WizardItemBrowser(var4);
               this.op = var5.getImplementingOperation();
            }
         }
      }

      @Override
      public boolean isVisible() {
         return this.op != null;
      }
   }
}
