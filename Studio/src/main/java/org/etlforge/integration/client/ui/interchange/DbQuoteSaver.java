package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Supplier;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.EntityPageLocator;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.LocatableChangeEvent;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectItemPreparer;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiThreadSupplier;

class DbQuoteSaver implements Disposable {
   private final EntityPageLocator pageLocator;
   private final PropertyChangeListener listener;
   private final DbQuoteSaver.Registrator registrator;

   public DbQuoteSaver(IntegrationProject var1, EntityPageLocator var2) {
      this.pageLocator = var2;
      this.listener = new DbQuoteSaver.DbQuoteListener();
      this.registrator = new DbQuoteSaver.Registrator(var1);
      this.registrator.insertAll();
   }

   @Override
   public void dispose() {
      this.registrator.removeAll();
      this.registrator.dispose();
   }

   private class DbQuoteListener implements PropertyChangeListener {
      private DbQuoteListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         Locatable var2 = ((LocatableChangeEvent)var1).getLocatable();
         final IntegrationEntity var3 = (IntegrationEntity)var2;
         UiThreadSupplier.call(new Supplier<Void>() {
            public Void get() {
               DbQuoteListener.this.saveEntity(var3);
               return null;
            }
         });
      }

      private void saveEntity(IntegrationEntity var1) {
         Editor var2 = DbQuoteSaver.this.pageLocator.findOpenPage(var1);
         if (var2 != null && var2.isDirty()) {
            this.savePage(var1, var2);
         } else {
            try {
               this.getProjectPersistor(var1).save(var1);
            } catch (InterchangeSavingException var4) {
               ErrorLog.log(
                  DbQuoteSaver.class, "Failed to save updated database quote information in \"" + var1.getName() + "\" to disk: " + var4.getMessage(), var4
               );
            }
         }
      }

      private void savePage(IntegrationEntity var1, Editor var2) {
         try {
            var2.save();
         } catch (SaveEditorException var4) {
            ErrorLog.log(
               DbQuoteSaver.class,
               "Failed to automatically save the editor for \"" + var1.getName() + "\". This is probably due to invalid data in the editor.",
               var4
            );
         }
      }

      private ProjectPersistor getProjectPersistor(IntegrationEntity var1) {
         ManagedProject var2 = ProjectUtils.getManagedProject(var1);
         return var2.getProjectPersistor();
      }
   }

   private class Registrator extends ProjectItemPreparer {
      public Registrator(IntegrationProject var2) {
         super(var2, Arrays.asList(EntityType.Source, EntityType.Target));
      }

      @Override
      protected void inserted(IntegrationEntity var1) {
         ((Locatable)var1).addLocationListener("db_quotes", DbQuoteSaver.this.listener);
      }

      @Override
      protected void removed(IntegrationEntity var1) {
         ((Locatable)var1).removeLocationListener("db_quotes", DbQuoteSaver.this.listener);
      }
   }
}
