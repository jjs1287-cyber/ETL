package org.etlforge.integration.data.entity.sfdc;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import org.etlforge.collections.Iterables2;
import org.etlforge.functors.function.KongaFunctions;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public abstract class WizardLookup {
   private final IntegrationEntity implementingObject;
   private final IntegrationProject project;
   private EntityFilter wizardFilter = EntityFilters.ACCEPTS_ALL;

   protected WizardLookup(IntegrationEntity var1) {
      this.implementingObject = var1;
      this.project = ProjectUtils.getProject(var1);
   }

   public final void setWizardFilter(EntityFilter var1) {
      this.wizardFilter = var1;
   }

   public final FluentIterable<SalesforceWizardEntity> findAllWizards() {
      return this.findAllWizardsInProject().filter(new Predicate<SalesforceWizardEntity>() {
         public boolean apply(SalesforceWizardEntity var1) {
            WizardItemBrowser var2 = new WizardItemBrowser(var1);
            return WizardLookup.this.isMatch(var2, WizardLookup.this.implementingObject);
         }
      });
   }

   public final Optional<SalesforceWizardEntity> findWizard() {
      return this.findAllWizards().first();
   }

   protected abstract boolean isMatch(WizardItemBrowser var1, IntegrationEntity var2);

   private FluentIterable<SalesforceWizardEntity> findAllWizardsInProject() {
      return this.project == null
         ? Iterables2.empty()
         : FluentIterable.from(this.project.getAllEntities())
            .filter(Predicates.instanceOf(SalesforceWizardEntity.class))
            .filter(this.wizardFilter)
            .transform(KongaFunctions.cast(SalesforceWizardEntity.class));
   }

   public final Optional<SalesforceConnector> findConnector() {
      Optional var1 = this.findWizard();
      if (!var1.isPresent()) {
         return Optional.absent();
      }

      SalesforceConnectorId var2 = ((SalesforceWizardEntity)var1.get()).getConnectorId();
      return Optional.fromNullable(this.project.getItemLookup().getEntity(var2, SalesforceConnector.class));
   }

   public static WizardLookup fromOperation(Operation var0) {
      return new WizardLookup.FromOperation(var0);
   }

   public static WizardLookup fromRequest(Transformation var0) {
      return new WizardLookup.FromRequest(var0);
   }

   public static WizardLookup fromResponse(Transformation var0) {
      return new WizardLookup.FromResponse(var0);
   }

   private static class FromOperation extends WizardLookup {
      public FromOperation(Operation var1) {
         super(var1);
      }

      @Override
      protected boolean isMatch(WizardItemBrowser var1, IntegrationEntity var2) {
         return var1.getImplementingOperation() == var2;
      }
   }

   private static class FromRequest extends WizardLookup {
      public FromRequest(Transformation var1) {
         super(var1);
      }

      @Override
      protected boolean isMatch(WizardItemBrowser var1, IntegrationEntity var2) {
         return var1.getRequestTransformation() == var2;
      }
   }

   private static class FromResponse extends WizardLookup {
      public FromResponse(Transformation var1) {
         super(var1);
      }

      @Override
      protected boolean isMatch(WizardItemBrowser var1, IntegrationEntity var2) {
         return var1.getResponseTransformation() == var2;
      }
   }
}
