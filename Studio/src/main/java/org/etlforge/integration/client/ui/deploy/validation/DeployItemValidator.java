package org.etlforge.integration.client.ui.deploy.validation;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.util.Collection;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.project.ProjectValidationContext;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.validation.DataValidationUtils;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.integration.data.validation.EntityCollectionValidationResultBuilder;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class DeployItemValidator {
   private final ValidationWarningDeployStrategy warningsStrategy;

   public DeployItemValidator(ValidationWarningDeployStrategy var1) {
      Preconditions.checkNotNull(var1, "warningsStrategy");
      this.warningsStrategy = var1;
   }

   public boolean validate(Collection<IntegrationEntity> var1) {
      if (var1 != null && !var1.isEmpty()) {
         ManagedProject var2 = this.getProject(var1);
         return this.validateItems(var2, var1);
      } else {
         return true;
      }
   }

   private ManagedProject getProject(Collection<IntegrationEntity> var1) {
      assert var1 != null && !var1.isEmpty();
      IntegrationEntity var2 = (IntegrationEntity)var1.iterator().next();
      return ProjectUtils.getManagedProject(var2);
   }

   private boolean validateItems(ManagedProject var1, Collection<IntegrationEntity> var2) {
      EntityCollectionValidationResult var3 = this.validateImpl(var1, var2);
      if (var3.isFatal()) {
         this.showInvalidItems(var3);
         return false;
      } else if (var3.status() == ValidationStatus.WARNING) {
         EntityCollectionValidationResult var4 = DataValidationUtils.extractWarningsOnly(var3);
         return this.warningsStrategy.allowDeploy(var4);
      } else {
         return true;
      }
   }

   private EntityCollectionValidationResult validateImpl(ManagedProject var1, Collection<IntegrationEntity> var2) {
      EntityCollectionValidationResultBuilder var3 = new EntityCollectionValidationResultBuilder();
      ProjectValidationContext var4 = new ProjectValidationContext(var1);

      for (IntegrationEntity var6 : var2) {
         ValidationResult var7 = var6.validate(var4);
         var3.add(var6, var7);
      }

      return var3.getResult();
   }

   private void showInvalidItems(final EntityCollectionValidationResult var1) {
      final String var2 = Strings.get("Deploy.Validation.Validator.ErrorTitle");
      final String var3 = Strings.get("Deploy.Validation.Validator.ErrorCaption");
      final String var4 = Strings.get("Deploy.Validation.Validator.ErrorMessage");
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ResultDisplayer var1x = new ResultDisplayer(var1, var2, var3, var4);
            var1x.showAsError();
         }
      });
   }
}
