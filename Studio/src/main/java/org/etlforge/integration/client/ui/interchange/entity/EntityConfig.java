package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.ui.entity.ProjectItemTransform;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactory;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public interface EntityConfig {
   EntityConfig UNCONFIGURED = new EntityConfig() {
      @Override
      public EntityType getType() {
         return null;
      }

      @Override
      public EntityPageFactory getPageFactory() {
         return EntityPageFactory.NONE;
      }

      @Override
      public IntegrationEntityFactory getFactory() {
         return null;
      }

      @Override
      public ApplicationAction newEntityAction() {
         return null;
      }

      @Override
      public EntityWizardFactory getEntityWizardFactory() {
         return null;
      }

      @Override
      public EntityRenamer getRenamer() {
         return new EntityRenamer() {
            @Override
            public boolean setNewName(IntegrationEntity var1, String var2) {
               return false;
            }
         };
      }

      @Override
      public List<? extends ProjectItemTransform> getTransformGenerators() {
         return ImmutableList.of();
      }

      @Override
      public SearchValuesCollector getSearchValuesCollector() {
         return SearchValuesCollector.EMPTY;
      }

      @Override
      public SearchAndReplaceMetaData getSearchAndReplaceMetaData() {
         return SearchAndReplaceMetaData.EMPTY;
      }

      @Override
      public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
         return new IntegrationEntityToolTip(var1);
      }

      @Override
      public void updateValueCache(IntegrationProject var1) {
      }
   };

   EntityType getType();

   EntityPageFactory getPageFactory();

   IntegrationEntityFactory getFactory();

   EntityWizardFactory getEntityWizardFactory();

   EntityRenamer getRenamer();

   ApplicationAction newEntityAction();

   List<? extends ProjectItemTransform> getTransformGenerators();

   SearchValuesCollector getSearchValuesCollector();

   SearchAndReplaceMetaData getSearchAndReplaceMetaData();

   IntegrationEntityToolTip getToolTip(IntegrationEntity var1);

   void updateValueCache(IntegrationProject var1);
}
