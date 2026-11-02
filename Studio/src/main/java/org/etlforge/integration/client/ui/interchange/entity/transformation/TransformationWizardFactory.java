package org.etlforge.integration.client.ui.interchange.entity.transformation;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardConfiguration;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardSections;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;

public final class TransformationWizardFactory extends EntityWizardFactory {
   private final WizardConfiguration config;

   public TransformationWizardFactory(WizardConfiguration var1) {
      this.config = var1;
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      TransformationWizard var4 = this.createWizardImpl(var1, (Transformation)var2, var3);
      this.createWizardDisplayer(var4, var1, EntityType.Transformation);
      return var4;
   }

   private TransformationWizard createWizardImpl(EntityWizardContext var1, Transformation var2, Folder var3) {
      TransformationWizard var4 = new TransformationWizard(this.config, var2, var3, var1.getProjectExplorer(), var1.getFileStore());
      var4.addWizardListener(new EntityWizardCompleter<>(var4, var1.getProject(), var1.getProjectPersistor(), var3));
      return var4;
   }

   @Override
   protected String[] getSections() {
      return WizardSections.all();
   }
}
