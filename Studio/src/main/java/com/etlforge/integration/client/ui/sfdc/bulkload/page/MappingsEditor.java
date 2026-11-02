package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.DefaultSourceHeaderUpdater;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.DefaultTargetFieldsUpdater;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.HeaderMappingsController;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.HeaderMappingsModel;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.HeaderMappingsUi;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.MappingsValidator;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.ValidationErrorDisplayer;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.AbstractEntityPageSaveJob;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.bulkload.HeaderMappings;
import org.etlforge.lang.Objects;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.dialog.KongaDialog;

public final class MappingsEditor {
   private final SalesforceBulkLoad bulkLoad;
   private final DefaultTargetFieldsUpdater targetFieldsUpdater;
   private Receiver<EntityPageSaveJob> callback = NoReceiver.create();
   private HeaderMappings currentMappings;

   public MappingsEditor(SalesforceBulkLoad var1, SalesforceFacade var2) {
      this.bulkLoad = var1;
      BulkAction var3 = var1.getAction();
      if (var3 != BulkAction.DELETE && var3 != BulkAction.HARD_DELETE) {
         this.targetFieldsUpdater = new DefaultTargetFieldsUpdater(var2, var1.getObjectName());
         this.targetFieldsUpdater.setUpdateRelatedObjectsInfo(var3.supportsForeignKeys());
      } else {
         this.targetFieldsUpdater = null;
      }

      this.currentMappings = var1.getHeaderMappings();
   }

   public void setConnectorSupplier(Supplier<SalesforceConnector> var1) {
      if (this.targetFieldsUpdater != null) {
         this.targetFieldsUpdater.setConnectorSupplier(var1);
      }
   }

   public void setCallback(Receiver<EntityPageSaveJob> var1) {
      this.callback = var1;
   }

   public Action getLaunchAction() {
      return new MappingsEditor.OpenInDialogAction();
   }

   public void openInDialog(Component var1) {
      HeaderMappingsUi var2 = this.createMappingsUi(var1);
      KongaDialog var3 = this.createDialog(var1, var2);
      var3.setVisible(true);
      if (var3.wasOkPressed()) {
         HeaderMappings var4 = var2.getHeaderMappings();
         if (!Objects.equal(this.currentMappings, var4)) {
            this.callback.handle(new MappingsEditor.SaveJobImpl(this.currentMappings, var4));
            this.currentMappings = var4;
         }
      }
   }

   private HeaderMappingsUi createMappingsUi(Component var1) {
      HeaderMappingsModel var2 = new HeaderMappingsModel(this.bulkLoad.getAction());
      var2.setHeaderMappings(this.currentMappings);
      HeaderMappingsController var3 = new HeaderMappingsController(var2);
      if (this.targetFieldsUpdater != null) {
         var3.installTargetFieldUpdater(this.targetFieldsUpdater);
      }

      HeaderMappingsUi var4 = new HeaderMappingsUi(var2, var3);
      DefaultSourceHeaderUpdater var5 = new DefaultSourceHeaderUpdater();
      var5.setInvoker(var1);
      var3.installSourceHeaderUpdater(var5);
      return var4;
   }

   private KongaDialog createDialog(final Component var1, final HeaderMappingsUi var2) {
      JComponent var3 = WizardStyleDialog.wrapUi(var2);
      Window var4 = var1 == null ? ApplicationUi.getAppWinFrame() : SwingUtilities.getWindowAncestor(var1);
      WizardStyleDialog var5 = new WizardStyleDialog(var4, "Mappings", var3) {
         @Override
         protected void onOk() {
            boolean var1x = MappingsEditor.this.validateMappings(var1, var2);
            if (var1x) {
               super.onOk();
            }
         }
      };
      var2.setWaitService(var5);
      var5.manageLocation(MappingsEditor.class);
      var5.setOkButtonAsDefault();
      this.installUndoRedo(var2, var5);
      return var5;
   }

   private boolean validateMappings(Component var1, HeaderMappingsUi var2) {
      ValidationErrorDisplayer var3 = new ValidationErrorDisplayer(var1);
      MappingsValidator var4 = new MappingsValidator(var3);
      HeaderMappings var5 = var2.getHeaderMappings();
      return var4.validate(var5);
   }

   private void installUndoRedo(HeaderMappingsUi var1, KongaDialog var2) {
   }

   private class OpenInDialogAction extends AbstractAction {
      private OpenInDialogAction() {
         super("Edit");
         this.putValue("ShortDescription", "Edit the mappings for this " + MappingsEditor.this.bulkLoad.getAction());
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               OpenInDialogAction.this.openDialog(var1);
            }
         });
      }

      private void openDialog(ActionEvent var1) {
         Object var2 = var1.getSource();
         Component var3 = var2 instanceof Component ? (Component)var2 : null;
         MappingsEditor.this.openInDialog(var3);
      }
   }

   private class SaveJobImpl extends AbstractEntityPageSaveJob<HeaderMappings> {
      public SaveJobImpl(HeaderMappings var2, HeaderMappings var3) {
         super("bulkload.header_mappings", var2, var3);
         this.setDescription("Mappings");
         this.setUndoRedoStateReceiver(new Receiver<HeaderMappings>() {
            public void handle(HeaderMappings var1) {
               MappingsEditor.this.currentMappings = var1;
            }
         });
      }

      protected void apply(HeaderMappings var1) {
         MappingsEditor.this.bulkLoad.setHeaderMappings(var1);
      }
   }
}
