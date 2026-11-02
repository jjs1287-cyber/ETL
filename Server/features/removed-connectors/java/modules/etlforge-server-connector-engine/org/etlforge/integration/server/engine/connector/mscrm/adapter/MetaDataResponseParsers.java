package org.etlforge.integration.server.engine.connector.mscrm.adapter;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.mscrm.metadata.AttributeRequiredLevel;
import org.etlforge.integration.mscrm.metadata.BasicEntityDetails;
import org.etlforge.integration.mscrm.metadata.LinkAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmDataType;
import org.etlforge.integration.mscrm.metadata.MSCrmDataTypeLookup;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.ManyToManyRelationship;
import org.etlforge.integration.mscrm.metadata.ManyToOneRelationship;
import org.etlforge.integration.mscrm.metadata.OneToManyRelationship;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;

public final class MetaDataResponseParsers {
   public static ArrayList<BasicEntityDetails> parseBasicEntityDetailsResponse(String var0) throws MSCrmMetaDataException {
      KongaSaxParser var1 = new KongaSaxParser();
      MetaDataResponseParsers.SuccessStatus var2 = new MetaDataResponseParsers.SuccessStatus();
      var1.registerHandler(var2);
      MetaDataResponseParsers.BasicEntityParser var3 = new MetaDataResponseParsers.BasicEntityParser();
      var3.register(var1);

      try {
         var1.parseString(var0);
      } catch (Exception var5) {
         throw new MSCrmMetaDataException("Failed to parse the returned metadata: " + var5.getMessage(), var5);
      }

      var2.checkSuccess();
      return var3.entities;
   }

   public static ArrayList<MSCrmObject> parseSpecificMetaDataResponse(String var0) throws MSCrmMetaDataException {
      KongaSaxParser var1 = new KongaSaxParser();
      MetaDataResponseParsers.SuccessStatus var2 = new MetaDataResponseParsers.SuccessStatus();
      var1.registerHandler(var2);
      MetaDataResponseParsers.SpecificEntityParser var3 = new MetaDataResponseParsers.SpecificEntityParser();
      var3.register(var1);

      try {
         var1.parseString(var0);
      } catch (Exception var5) {
         throw new MSCrmMetaDataException("Failed to parse the returned metadata: " + var5.getMessage(), var5);
      }

      var2.checkSuccess();
      return var3.entities;
   }

   private static boolean isTrue(String var0) {
      return "true".equalsIgnoreCase(var0);
   }

   private MetaDataResponseParsers() {
   }

   @RelativePathRoot("metadataResponses/metadataResponse/metadataResponse/Entity/Attributes/Attribute")
   private static class AttributeHandler {
      public MSCrmObject.Builder objectBuilder;
      private final MSCrmDataTypeLookup typeLookup = new MSCrmDataTypeLookup();
      private MSCrmAttribute.Builder attributeBuilder;

      private AttributeHandler() {
      }

      @StartElementHandler("/")
      public void start() {
         this.attributeBuilder = new MSCrmAttribute.Builder();
      }

      @EndElementHandler("/")
      public void end() {
         MSCrmAttribute var1 = this.attributeBuilder.build();
         this.attributeBuilder = null;
         this.objectBuilder.addAttribute(var1);
      }

      @EndElementHandler("LogicalName")
      public void name(String var1) {
         this.attributeBuilder.setLogicalName(var1);
      }

      @EndElementHandler("Description")
      public void description(String var1) {
         this.attributeBuilder.setDescription(var1);
      }

      @EndElementHandler("Type")
      public void type(String var1) {
         MSCrmDataType var2 = this.typeLookup.getDataType(var1);
         this.attributeBuilder.setType(var2);
      }

      @EndElementHandler("DisplayName")
      public void displayName(String var1) {
         this.attributeBuilder.setDisplayName(var1);
      }

      @EndElementHandler("SchemaName")
      public void schemaName(String var1) {
         this.attributeBuilder.setSchemaName(var1);
      }

      @EndElementHandler("RequiredLevel")
      public void requiredLevel(String var1) {
         AttributeRequiredLevel var2 = AttributeRequiredLevel.valueOf(var1);
         this.attributeBuilder.setRequiredLevel(var2);
      }

      @EndElementHandler("IsValidForCreate")
      public void validForCreate(String var1) {
         this.attributeBuilder.setValidForCreate(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler("IsValidForRead")
      public void validForRead(String var1) {
         this.attributeBuilder.setValidForRead(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler("IsValidForUpdate")
      public void validForUpdate(String var1) {
         this.attributeBuilder.setValidForUpdate(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler("IsManaged")
      public void managed(String var1) {
         this.attributeBuilder.setManaged(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler("IsPrimaryId")
      public void primaryId(String var1) {
         this.attributeBuilder.setPrimaryId(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler("IsPrimaryName")
      public void primaryName(String var1) {
         this.attributeBuilder.setPrimaryName(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler("IsValidForAdvancedFind")
      public void validForAdvancedFind(String var1) {
         this.attributeBuilder.setValidForAdvancedFind(MetaDataResponseParsers.isTrue(var1));
      }
   }

   private static class BasicEntityDetailsHandler {
      @RelativePathRoot
      private final String relativeRoot;
      private BasicEntityDetails.Builder builder;

      public BasicEntityDetailsHandler(String var1) {
         this.relativeRoot = var1;
      }

      public void newEntity() {
         this.builder = new BasicEntityDetails.Builder();
      }

      public BasicEntityDetails build() {
         BasicEntityDetails var1 = this.builder.build();
         this.builder = null;
         return var1;
      }

      @EndElementHandler
      public void EntityLogicalName(String var1) {
         this.builder.logicalName(var1);
      }

      @EndElementHandler
      public void OTC(Integer var1) {
         this.builder.entityTypeCode(var1);
      }

      @EndElementHandler
      public void OwnershipType(String var1) {
         this.builder.ownershipType(var1);
      }

      @EndElementHandler
      public void DisplayName(String var1) {
         this.builder.displayName(var1);
      }

      @EndElementHandler
      public void DisplayCollectionName(String var1) {
         this.builder.pluralName(var1);
      }

      @EndElementHandler
      public void AutoRouteToOwnerQueue(String var1) {
         this.builder.autoRouteToOwnerQueue(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanBeInManyToMany(String var1) {
         this.builder.canBeInManyToMany(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanBePrimaryEntityInRelationship(String var1) {
         this.builder.canBePrimaryEntityInRelationship(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanBeRelatedEntityInRelationship(String var1) {
         this.builder.canBeRelatedEntityInRelationship(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanCreateAttributes(String var1) {
         this.builder.canCreateAttributes(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanCreateCharts(String var1) {
         this.builder.canCreateCharts(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanCreateForms(String var1) {
         this.builder.canCreateForms(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanCreateViews(String var1) {
         this.builder.canCreateViews(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanModifyAdditionalSettings(String var1) {
         this.builder.canModifyAdditionalSettings(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void CanTriggerWorkflow(String var1) {
         this.builder.canTriggerWorkflow(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsActivity(String var1) {
         this.builder.activity(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsActivityParty(String var1) {
         this.builder.activityParty(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsAuditEnabled(String var1) {
         this.builder.auditEnabled(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsAvailableOffline(String var1) {
         this.builder.availableOffline(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsChildEntity(String var1) {
         this.builder.childEntity(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsCustomEntity(String var1) {
         this.builder.customEntity(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsCustomizable(String var1) {
         this.builder.customizable(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsDocumentManagementEnabled(String var1) {
         this.builder.documentManagementEnabled(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsDuplicateDetectionEnabled(String var1) {
         this.builder.duplicateDetectionEnabled(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsEnabledForCharts(String var1) {
         this.builder.enabledForCharts(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsImportable(String var1) {
         this.builder.importable(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsIntersect(String var1) {
         this.builder.intersect(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsMailMergeEnabled(String var1) {
         this.builder.mailMergeEnabled(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsManaged(String var1) {
         this.builder.managed(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsMappable(String var1) {
         this.builder.mappable(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsReadingPaneEnabled(String var1) {
         this.builder.readingPaneEnabled(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsRenameable(String var1) {
         this.builder.renameable(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsValidForAdvancedFind(String var1) {
         this.builder.validForAdvancedFind(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsValidForQueue(String var1) {
         this.builder.validForQueue(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void IsVisibleInMobile(String var1) {
         this.builder.visibleInMobile(MetaDataResponseParsers.isTrue(var1));
      }

      @EndElementHandler
      public void ReportViewName(String var1) {
         this.builder.reportViewName(var1);
      }

      @EndElementHandler
      public void PrimaryNameAttribute(String var1) {
         this.builder.primaryNameAttribute(var1);
      }

      @EndElementHandler
      public void PrimaryIdAttribute(String var1) {
         this.builder.primaryIdAttribute(var1);
      }

      @EndElementHandler
      public void Description(String var1) {
         this.builder.description(var1.trim());
      }
   }

   @RelativePathRoot("metadataResponses/metadataResponse/metadataResponse/Metadata/Entity")
   private static class BasicEntityParser {
      public final ArrayList<BasicEntityDetails> entities = Lists.newArrayList();
      private final MetaDataResponseParsers.BasicEntityDetailsHandler detailsHandler = new MetaDataResponseParsers.BasicEntityDetailsHandler(
         "metadataResponses/metadataResponse/metadataResponse/Metadata/Entity"
      );

      private BasicEntityParser() {
      }

      @StartElementHandler("/")
      public void startEntity() {
         this.detailsHandler.newEntity();
      }

      @EndElementHandler("/")
      public void endEntity() {
         BasicEntityDetails var1 = this.detailsHandler.build();
         this.entities.add(var1);
      }

      public void register(KongaSaxParser var1) {
         var1.registerHandler(this);
         var1.registerHandler(this.detailsHandler);
      }
   }

   @RelativePathRoot("metadataResponses/metadataResponse/metadataResponse/Entity/ManyToManyRelationships/ManyToManyRelationship")
   private static class ManyToManyRelationshipHandler {
      public MSCrmObject.Builder objectBuilder;
      private String fromAttribute;
      private String fromEntity;
      private String toAttribute;
      private String toEntity;
      private String name;

      private ManyToManyRelationshipHandler() {
      }

      @EndElementHandler("/")
      public void end() {
         ManyToManyRelationship var1 = new ManyToManyRelationship(
            new LinkAttribute(this.fromEntity, this.fromAttribute), new LinkAttribute(this.toEntity, this.toAttribute), this.name
         );
         this.objectBuilder.addManyToMany(var1);
      }

      @EndElementHandler("Entity1IntersectAttribute")
      public void fromAttribute(String var1) {
         this.fromAttribute = var1;
      }

      @EndElementHandler("Entity1LogicalName")
      public void fromEntity(String var1) {
         this.fromEntity = var1;
      }

      @EndElementHandler("Entity2IntersectAttribute")
      public void toAttribute(String var1) {
         this.toAttribute = var1;
      }

      @EndElementHandler("Entity2LogicalName")
      public void toEntity(String var1) {
         this.toEntity = var1;
      }

      @EndElementHandler("IntersectEntityName")
      public void name(String var1) {
         this.name = var1;
      }
   }

   private enum RelationshipType {
      ONE_TO_MANY("OneToManyRelationship") {
         @Override
         public void addToBuilder(LinkAttribute var1, LinkAttribute var2, MSCrmObject.Builder var3) {
            LinkAttribute var4 = var1;
            LinkAttribute var5 = var2;
            OneToManyRelationship var6 = new OneToManyRelationship(var4, var5);
            var3.addOneToMany(var6);
         }
      },
      MANY_TO_ONE("ManyToOneRelationship") {
         @Override
         public void addToBuilder(LinkAttribute var1, LinkAttribute var2, MSCrmObject.Builder var3) {
            LinkAttribute var4 = var2;
            LinkAttribute var5 = var1;
            ManyToOneRelationship var6 = new ManyToOneRelationship(var4, var5);
            var3.addManyToOne(var6);
         }
      };

      private String elementName;

      RelationshipType(String var3) {
         this.elementName = var3;
      }

      public String getElementName() {
         return this.elementName;
      }

      public abstract void addToBuilder(LinkAttribute var1, LinkAttribute var2, MSCrmObject.Builder var3);
   }

   private static class RelationshipsHandler {
      private final MetaDataResponseParsers.RelationshipType relationshipType;
      @RelativePathRoot
      public final String relativePathRoot;
      private String referencedAttribute;
      private String referencedEntity;
      private String referencingAttribute;
      private String referencingEntity;
      public MSCrmObject.Builder objectBuilder;

      public RelationshipsHandler(MetaDataResponseParsers.RelationshipType var1) {
         this.relationshipType = var1;
         this.relativePathRoot = "metadataResponses/metadataResponse/metadataResponse/Entity/" + var1.getElementName() + "s/" + var1.getElementName();
      }

      @EndElementHandler("/")
      public void end() {
         this.relationshipType
            .addToBuilder(
               new LinkAttribute(this.referencedEntity, this.referencedAttribute),
               new LinkAttribute(this.referencingEntity, this.referencingAttribute),
               this.objectBuilder
            );
      }

      @EndElementHandler
      public void ReferencedAttribute(String var1) {
         this.referencedAttribute = var1;
      }

      @EndElementHandler
      public void ReferencedEntity(String var1) {
         this.referencedEntity = var1;
      }

      @EndElementHandler
      public void ReferencingAttribute(String var1) {
         this.referencingAttribute = var1;
      }

      @EndElementHandler
      public void ReferencingEntity(String var1) {
         this.referencingEntity = var1;
      }
   }

   @RelativePathRoot("metadataResponses/metadataResponse/metadataResponse/Entity")
   private static class SpecificEntityParser {
      public final ArrayList<MSCrmObject> entities = Lists.newArrayList();
      private final MetaDataResponseParsers.BasicEntityDetailsHandler basicDetailsHandler = new MetaDataResponseParsers.BasicEntityDetailsHandler(
         "metadataResponses/metadataResponse/metadataResponse/Entity"
      );
      private final MetaDataResponseParsers.AttributeHandler attributeHandler = new MetaDataResponseParsers.AttributeHandler();
      private final MetaDataResponseParsers.ManyToManyRelationshipHandler manyToManyHandler = new MetaDataResponseParsers.ManyToManyRelationshipHandler();
      private final MetaDataResponseParsers.RelationshipsHandler oneToManyHandler = new MetaDataResponseParsers.RelationshipsHandler(
         MetaDataResponseParsers.RelationshipType.ONE_TO_MANY
      );
      private final MetaDataResponseParsers.RelationshipsHandler manyToOneHandler = new MetaDataResponseParsers.RelationshipsHandler(
         MetaDataResponseParsers.RelationshipType.MANY_TO_ONE
      );
      private MSCrmObject.Builder builder;

      private SpecificEntityParser() {
      }

      public void register(KongaSaxParser var1) {
         var1.registerHandler(this);
         var1.registerHandler(this.basicDetailsHandler);
         var1.registerHandler(this.attributeHandler);
         var1.registerHandler(this.manyToManyHandler);
         var1.registerHandler(this.oneToManyHandler);
         var1.registerHandler(this.manyToOneHandler);
      }

      @StartElementHandler("/")
      public void startEntity() {
         this.builder = new MSCrmObject.Builder();
         this.basicDetailsHandler.newEntity();
         this.attributeHandler.objectBuilder = this.builder;
         this.manyToManyHandler.objectBuilder = this.builder;
         this.oneToManyHandler.objectBuilder = this.builder;
         this.manyToOneHandler.objectBuilder = this.builder;
      }

      @EndElementHandler("/")
      public void endEntity() {
         BasicEntityDetails var1 = this.basicDetailsHandler.build();
         this.builder.withDetails(var1);
         MSCrmObject var2 = this.builder.build();
         this.entities.add(var2);
         this.builder = null;
      }
   }

   @RelativePathRoot("metadataResponses/metadataResponse")
   private static class SuccessStatus {
      private boolean success;
      private String message;

      private SuccessStatus() {
      }

      @EndElementHandler
      public void success(Boolean var1) {
         this.success = var1;
      }

      @EndElementHandler
      public void message(String var1) {
         this.message = var1;
      }

      public void checkSuccess() throws MSCrmMetaDataException {
         if (!this.success) {
            throw new MSCrmMetaDataException(this.message);
         }
      }
   }
}
