package com.etlforge.integration.client.ui.interchange.project.share;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.project.share.ConflictResolution;
import com.etlforge.integration.client.project.share.IdConflict;
import com.etlforge.integration.client.project.share.history.ImportHistoryEntry;
import com.etlforge.integration.client.project.share.history.ImportedEntity;
import java.awt.Dialog.ModalityType;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;

class IdConflictResolver {
   private final EntityOverwriter overwriter;
   private IdConflictResolver.Choice permanentChoice;
   private int numberOfRemainigConflicts;
   private ImportHistoryEntry historyEntry;

   public IdConflictResolver(EntityOverwriter var1, int var2) {
      this.overwriter = var1;
      this.numberOfRemainigConflicts = var2;
   }

   public void setHistoryEntry(ImportHistoryEntry var1) {
      this.historyEntry = var1;
   }

   public ConflictResolution resolve(IdConflict var1) {
      if (!(var1.getCopiedEntity() instanceof Folder)) {
         this.resolveImpl(var1);
      }

      return ConflictResolution.SKIP;
   }

   private void resolveImpl(IdConflict var1) {
      if (this.permanentChoice == IdConflictResolver.Choice.REPLACE) {
         this.replace(var1);
      } else if (this.permanentChoice != IdConflictResolver.Choice.KEEP) {
         this.processUserInput(var1);
      }
   }

   private void processUserInput(IdConflict var1) {
      IdConflictResolver.Choice var2 = this.getUsersChoice(var1);
      if (var2 == IdConflictResolver.Choice.REPLACE) {
         this.replace(var1);
         if (this.historyEntry != null) {
            ImportedEntity var3 = this.historyEntry.addEntity(var1.getCopiedEntity(), var1.getExistingEntity());
            if (!this.wasNameOverwritten(var1)) {
               var3.addNote("The name was not overwritten since that name was already taken.");
            }
         }
      }
   }

   private boolean wasNameOverwritten(IdConflict var1) {
      return var1.getExistingEntity().getName().equals(var1.getCopiedEntity().getName());
   }

   private void replace(IdConflict var1) {
      this.overwriter.overwrite(var1.getCopiedEntity(), var1.getExistingEntity());
   }

   private IdConflictResolver.Choice getUsersChoice(final IdConflict var1) {
      Supplier var2 = new Supplier<IdConflictResolver.Choice>() {
         public IdConflictResolver.Choice get() {
            return IdConflictResolver.this.new Prompt(var1).ask();
         }
      };
      UiThreadSupplier var3 = UiThreadSupplier.wrap(var2);
      var3.setErrorHandler(new LoggingExceptionHandler(IdConflictResolver.class, "An unexpected error occurred"));
      return (IdConflictResolver.Choice)var3.get();
   }

   public enum Choice {
      REPLACE,
      KEEP;
   }

   private class Prompt implements Receiver<IdConflictResolver.Choice> {
      private final IdConflictResolverUi ui;
      private final KongaDialog dlg;
      private IdConflictResolver.Choice selection = IdConflictResolver.Choice.KEEP;

      public Prompt(IdConflict var2) {
         this.ui = new IdConflictResolverUi(var2, this);
         this.ui.setNumberOfRemainingConflicts(--IdConflictResolver.this.numberOfRemainigConflicts);
         this.dlg = new KongaDialog(UiUtils.getActiveWindow(), "ID collision", ModalityType.APPLICATION_MODAL) {
            @Override
            protected void onDialogClose() {
            }
         };
         this.dlg.standardLayout(this.ui, null);
         this.dlg.setDefaultCloseOperation(0);
         this.dlg.setCancelOnEscape(false);
         this.dlg.setResizable(false);
         this.dlg.manageLocation(IdConflictResolver.class);
      }

      public void handle(IdConflictResolver.Choice var1) {
         this.selection = var1;
         if (this.ui.isApplyToAllSelected()) {
            IdConflictResolver.this.permanentChoice = this.selection;
         }

         this.dlg.dispose();
      }

      public IdConflictResolver.Choice ask() {
         this.dlg.setVisible(true);
         return this.selection;
      }
   }
}
