package com.etlforge.integration.client.sfdc.project.upgrade;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.integration.salesforce.wsdl.partner.crom.CromHeaderElement;
import org.etlforge.integration.salesforce.wsdl.partner.crom.SfdcCromUtils;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.mapping.CromBasedMappingTreeStructure;
import org.etlforge.integration.structure.mapping.CromToMappingStructureWriter;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public final class CromHeaderInserter {
   private final SalesforceWizardRole.Position position;
   private final ImmutableSet<EntityType> wizardTypes;
   private final ImmutableList<SoapHeader> newSoapHeaders;
   private final ImmutableList<CromHeaderElement> newCromHeaders;
   @Nullable
   private final String insertAfter;
   private Logger logger = Logger.getLogger("global");

   public CromHeaderInserter(
      SalesforceWizardRole.Position var1,
      Iterable<EntityType> var2,
      Iterable<? extends SoapHeader> var3,
      Iterable<? extends CromHeaderElement> var4,
      @Nullable String var5
   ) {
      this.position = Preconditions.checkNotNull(var1);
      this.wizardTypes = Sets.immutableEnumSet(var2);
      this.newSoapHeaders = ImmutableList.copyOf(var3);
      this.newCromHeaders = ImmutableList.copyOf(var4);
      this.insertAfter = var5;
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   public void updateTransformations(IntegrationProject var1) {
      this.logger
         .info(
            String.format(
               "Upgrading the %s Transformations of type(s) %s in the project %s with new SOAP headers", this.position, this.wizardTypes, var1.getName()
            )
         );
      this.updateTransformationsImpl(var1);
      this.logger.info(this.position + " Transformation update completed.");
   }

   private void updateTransformationsImpl(IntegrationProject var1) {
      ManagedProject var2 = var1.getExtensionObject(ManagedProject.class);

      for (SalesforceWizardEntity var5 : this.collectAffectedWizards(var1)) {
         this.updateWizard(var2, var5);
      }
   }

   private Iterable<SalesforceWizardEntity> collectAffectedWizards(IntegrationProject var1) {
      EntityFilter var2 = EntityFilters.oneOfTypes(this.wizardTypes);
      return FluentIterable.from(var1.getAllEntities()).filter(var2).filter(SalesforceWizardEntity.class);
   }

   private void updateWizard(ManagedProject var1, SalesforceWizardEntity var2) {
      this.logger.info(String.format("Upgrading the %s Transformation for %s with new SOAP headers.", this.position, var2));
      WizardItemBrowser var3 = new WizardItemBrowser(var2);
      Transformation var4 = var3.getTransformation(this.position);
      if (var4 == null) {
         this.logger.severe("No " + this.position + " Transformation found.");
      } else if (var4 instanceof BuiltInIntegrationEntity) {
         this.logger
            .info(String.format("The %s Transformation for %s is a built-in Transformation, which is not affected by this upgrade.", this.position, var2));
      } else {
         this.logger.info("Updating Transformation " + var4);
         boolean var5 = this.refreshMappingTree(var1, var4);
         if (var5) {
            this.addNewMappings(var4);
            var4.setDeployDirty(true);
         }
      }
   }

   private boolean refreshMappingTree(ManagedProject var1, Transformation var2) {
      try {
         InputAndOutputMappingTreeStructureCache var3 = var1.getTransformationMappingStructureCache();
         MappingTreeStructure var4 = this.position == SalesforceWizardRole.Position.REQUEST
            ? var3.getOutputStructure(var2.getID()).orNull()
            : var3.getInputStructure(var2.getID()).orNull();
         if (var4 == null) {
            this.logger.severe("The mapping tree structure was not in the project cache.");
            return false;
         }

         CROM_Tree var5 = this.loadCromTree(var4);
         CROM var6 = var5.getRoot();
         CROM var7 = this.findSoapHeaderRoot(var6);
         if (var7 == null) {
            this.logger.severe("Could not find or create the SOAP header root");
            return false;
         }

         if (this.insertAfter == null) {
            CromHeaderElement var8 = this.newCromHeaders.get(0);
            CROM var9 = var8.insertUnder(var7);

            for (CromHeaderElement var11 : Iterables.skip(this.newCromHeaders, 1)) {
               var9 = var11.insertAfter(var9);
            }
         } else {
            CROM var13 = var7.findDescendant(this.insertAfter);
            if (var13 == null) {
               this.logger.severe("Could not find the previous header: " + this.insertAfter);
               return false;
            }

            for (CromHeaderElement var15 : this.newCromHeaders) {
               var13 = var15.insertAfter(var13);
            }
         }

         this.writeStructureToCache(var2, var3, var5, var6);
         return true;
      } catch (Exception var12) {
         this.logger.throwing(this.getClass().getName(), "refreshMappingTree", var12);
         var12.printStackTrace();
         return false;
      }
   }

   @Nullable
   private CROM findSoapHeaderRoot(CROM var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.add("transaction");
      if (this.position == SalesforceWizardRole.Position.RESPONSE) {
         var2.add("response");
      }

      CROM var3 = var1.findDescendant(Iterables.toArray(var2, String.class));
      if (var3 == null) {
         return null;
      }

      CROM var4 = var3.findDescendant("header");
      if (var4 == null) {
         var4 = SfdcCromUtils.createSoapHeaderRoot(var3);
      }

      return var4;
   }

   private CROM_Tree loadCromTree(MappingTreeStructure var1) {
      if (var1 instanceof CromBasedMappingTreeStructure) {
         return ((CromBasedMappingTreeStructure)var1).toCromTree();
      }

      CROM var2 = var1.getRoot();
      return var2 == null ? CROM_Tree.parse(var1) : new CROM_Tree(var2);
   }

   private void writeStructureToCache(Transformation var1, InputAndOutputMappingTreeStructureCache var2, CROM_Tree var3, CROM var4) throws IOException {
      Writer var5 = this.position == SalesforceWizardRole.Position.REQUEST
         ? var2.getOutputStructureWriter(var1.getID())
         : var2.getInputStructureWriter(var1.getID());

      try {
         CromToMappingStructureWriter var6 = new CromToMappingStructureWriter(var4, CROMHeader.XML);
         var3.writeXml(var5, var6);
         this.logger.info("The " + this.position + " mapping tree has been updated.");
      } finally {
         var5.close();
      }
   }

   private void addNewMappings(Transformation var1) {
      synchronized (var1.getDataLock()) {
         List var3 = var1.getMapList();

         for (SoapHeader var5 : this.newSoapHeaders) {
            List var6 = var5.getMapList();
            var3.addAll(var6);
         }

         var1.setMapList(var3);
      }

      this.logger.info("The map list has been updated.");
   }
}
