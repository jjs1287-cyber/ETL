package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Supplier;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.ui.entity.EntityPageLocator;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.ui.UiThreadSupplier;

public final class EditorAwarePersistor implements ProjectPersistor {
   private final EntityPageLocator pageLocator;
   private final ProjectPersistor saveWorker;
   private boolean saveToDiskEvenIfNotDirty;
   private volatile boolean writeToDisk;

   public EditorAwarePersistor(EntityPageLocator var1, ProjectPersistor var2) {
      this.pageLocator = var1;
      this.saveWorker = var2;
      this.saveToDiskEvenIfNotDirty = true;
   }

   public void setSaveToDiskEvenIfNotDirty(boolean var1) {
      this.saveToDiskEvenIfNotDirty = var1;
   }

   @Override
   public void save(IntegrationEntity var1) throws InterchangeSavingException {
      this.writeToDisk = true;
      SaveEditorException var2 = this.savePageIfOpen(var1);
      if (var2 != null) {
         throw new InterchangeSavingException(var2.getMessage(), var2);
      }

      if (this.writeToDisk) {
         this.saveWorker.save(var1);
      }
   }

   private SaveEditorException savePageIfOpen(final IntegrationEntity var1) {
      return UiThreadSupplier.call(new Supplier<SaveEditorException>() {
         public SaveEditorException get() {
            Editor var1x = EditorAwarePersistor.this.pageLocator.findOpenPage(var1);
            if (var1x != null && var1x.isDirty()) {
               try {
                  var1x.save();
               } catch (SaveEditorException var3) {
                  return var3;
               }
            } else {
               EditorAwarePersistor.this.writeToDisk = EditorAwarePersistor.this.saveToDiskEvenIfNotDirty;
            }

            return null;
         }
      });
   }

   @Override
   public void saveProjectRelations() throws InterchangeSavingException {
      this.saveWorker.saveProjectRelations();
   }
}
