package org.etlforge.integration.client.connect.wizard;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardDialogDisplayer;
import org.etlforge.integration.client.connect.mapper.AutoMappingJob;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.wait.WaitLock;

public abstract class AbstractConnectWizardFactory extends EntityWizardFactory {
   private final EntityType connectType;
   private final String wizardName;

   public AbstractConnectWizardFactory(EntityType var1, String var2) {
      this.connectType = var1;
      this.wizardName = var2;
   }

   @Override
   public final Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      Operation var4 = (Operation)var2;
      Wizard var5 = this.createWizardImpl(var1, var4);
      AbstractConnectWizardFactory.Completer var6 = new AbstractConnectWizardFactory.Completer(var5, var1.getProject(), var1.getProjectPersistor(), var3);
      var6.setWaitService(var1.getWindow());
      var5.addWizardListener(var6);
      this.createWizardDisplayer(var5, var1, this.connectType, ClientIcons.ETLFORGE_CONNECT_16);
      return var5;
   }

   protected abstract Wizard<Operation> createWizardImpl(EntityWizardContext var1, Operation var2);

   @Override
   protected final WizardDialogDisplayer createEmptyDialogDisplayer(WaitLock var1) {
      return new WizardDialogDisplayer(var1) {
         @Override
         protected String constructDialogTitle(String var1, String var2) {
            return AbstractConnectWizardFactory.this.wizardName + " - " + super.constructDialogTitle(var1, var2);
         }
      };
   }

   @Override
   protected final String[] getSections() {
      return WizardSections.all();
   }

   private static class Completer extends AbstractConnectWizardCompleter<Operation> {
      public Completer(Wizard<Operation> var1, IntegrationProject var2, ProjectPersistor var3, IntegrationEntity var4) {
         super(var1, var2, var3, var4);
      }

      @Override
      protected Transformation getMappingTransformation() {
         AbstractConnectWizard var1 = (AbstractConnectWizard)this.getWizard();
         return var1.getTransformation();
      }

      protected List<IntegrationEntity> getAdditionalItemsToSave() {
         ArrayList var1 = Lists.newArrayList();
         var1.add(this.getMappingTransformation());
         var1.addAll(this.getCreatedFolders());
         return var1;
      }

      private List<Folder> getCreatedFolders() {
         AbstractConnectWizard var1 = (AbstractConnectWizard)this.getWizard();
         return var1.getCreatedFolders();
      }

      @Override
      protected boolean isAutoMap() {
         AbstractConnectWizard var1 = (AbstractConnectWizard)this.getWizard();
         return var1.isAutoMapSelected();
      }

      @Override
      protected AutoMappingJob createAutoMappingJob(TransformationPage var1) {
         return new AutoMapper(var1);
      }
   }
}
