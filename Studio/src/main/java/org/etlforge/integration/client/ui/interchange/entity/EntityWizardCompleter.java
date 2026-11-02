package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Preconditions;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardAdapter;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;

public class EntityWizardCompleter<T extends IntegrationEntity> extends WizardAdapter<T> {
   private final Wizard<T> wizard;
   private final IntegrationProject project;
   private final ProjectPersistor persistor;
   private final IntegrationEntity parent;
   private volatile T itemCreated;

   public EntityWizardCompleter(Wizard<T> var1, IntegrationProject var2, ProjectPersistor var3, IntegrationEntity var4) {
      this.wizard = Preconditions.checkNotNull(var1, "wizard");
      this.project = Preconditions.checkNotNull(var2, "project");
      this.persistor = Preconditions.checkNotNull(var3, "persistor");
      this.parent = Preconditions.checkNotNull(var4, "parent");
   }

   protected final Wizard<T> getWizard() {
      return this.wizard;
   }

   protected final IntegrationProject getProject() {
      return this.project;
   }

   protected final T getItemCreated() {
      return this.itemCreated;
   }

   public final void wizardCompleted(T var1) {
      this.itemCreated = (T)var1;
      this.insertObjectIntoProject((T)var1);
      this.doPreSaveWork();
      this.save((T)var1);
      this.saveAdditionalItems(this.persistor);
      this.doPostSaveWork();
   }

   private void insertObjectIntoProject(T var1) {
      if (var1.getParent() == null) {
         IntegrationProjectLock.acquire();

         try {
            String var2 = EntityUtils.getAvailableName(var1, this.parent, var1.getName());
            var1.setName(var2);
            this.project.insertEntity(var1, this.parent);
         } finally {
            IntegrationProjectLock.release();
         }
      }
   }

   protected void doPreSaveWork() {
   }

   private void save(T var1) {
      try {
         this.persistor.save(var1);
      } catch (InterchangeSavingException var3) {
         var1.delete();
         ErrorLog.attention(this.getClass(), "Failed to save the new " + var1.getEntityType().getDisplayName(), var3);
      }
   }

   protected void saveAdditionalItems(ProjectPersistor var1) {
   }

   protected void doPostSaveWork() {
   }
}
