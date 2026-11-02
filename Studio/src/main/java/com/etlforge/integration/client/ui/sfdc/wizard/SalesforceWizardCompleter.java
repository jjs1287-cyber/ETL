package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Supplier;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.connect.mapper.AutoMappingJob;
import org.etlforge.integration.client.connect.wizard.AbstractConnectWizardCompleter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiThreadSupplier;

public class SalesforceWizardCompleter<T extends IntegrationEntity & SalesforceWizardEntity> extends AbstractConnectWizardCompleter<T> {
   public SalesforceWizardCompleter(SalesforceWizard<T> var1, IntegrationProject var2, ProjectPersistor var3, IntegrationEntity var4) {
      super(var1, var2, var3, var4);
      this.setOpenMappingTransformationAutomatically(false);
   }

   @Override
   protected final Iterable<IntegrationEntity> getAdditionalItemsToSave() {
      SalesforceWizard var1 = (SalesforceWizard)this.getWizard();
      return var1.getCreatedItems();
   }

   @Override
   protected final boolean isAutoMap() {
      SalesforceWizard var1 = (SalesforceWizard)this.getWizard();
      return var1.isAutoMap();
   }

   @Override
   protected Transformation getMappingTransformation() {
      return null;
   }

   @Override
   protected AutoMappingJob createAutoMappingJob(TransformationPage var1) {
      return null;
   }

   @Override
   protected void doAdditionalPostSaveWork() {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            Editor var1 = SalesforceWizardCompleter.this.openWizardItem();
            Editor var2 = SalesforceWizardCompleter.this.openMappingTransformation();
            if (var2 instanceof TransformationPage) {
               SalesforceWizardCompleter.this.decorateTransformationPageWithFinishAndCloseButtons(var1, var2);
            }

            return null;
         }
      });
   }

   private void decorateTransformationPageWithFinishAndCloseButtons(Editor var1, Editor var2) {
      FinishTransformationControlPanel var3 = new FinishTransformationControlPanel((TransformationPage)var2, var1);
      var3.addToTransformationPage();
   }
}
