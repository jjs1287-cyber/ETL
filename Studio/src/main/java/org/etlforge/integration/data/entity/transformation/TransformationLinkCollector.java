package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.LinkUtils;
import org.etlforge.integration.data.entity.link.StrongEntityLink;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.util.id.KongaID;

public final class TransformationLinkCollector {
   private final Transformation transformation;
   private final IntegrationEntityLookup dataLookup;
   private final List<EntityLink> collectedLinks;
   private DataStructure inputStructure;
   private DataStructure outputStructure;

   public TransformationLinkCollector(Transformation var1, IntegrationEntityLookup var2) {
      this.transformation = var1;
      this.dataLookup = var2;
      this.collectedLinks = Lists.newArrayList();
      this.collect();
   }

   private void collect() {
      this.collectSourceLink();
      this.collectTargetLink();
      this.collectDocumentLinks();
      this.collectFunctionCallLinks();
      this.collectLinksFromMappings();
   }

   public List<EntityLink> getLinks() {
      return this.collectedLinks;
   }

   public DataStructure getInputStructure() {
      return this.inputStructure;
   }

   public DataStructure getOutputStructure() {
      return this.outputStructure;
   }

   private void collectLinksFromMappings() {
      TransformationLinksFromScriptFactory var1 = new TransformationLinksFromScriptFactory(this.transformation, this.dataLookup);
      this.collectedLinks.addAll(var1.getLinks());
   }

   private void collectSourceLink() {
      SourceId var1 = this.transformation.getSourceId();
      if (var1 != null) {
         Source var2 = this.dataLookup.getEntity(var1, Source.class);
         if (var2 != null) {
            this.collectedLinks.add(new StrongEntityLink(var2, this.transformation));
            DataLocation var3 = var2.getLocation();
            if (var3 instanceof DatabaseLocation) {
               this.collectSourceDbLocationAndStructureLink(var2);
            } else if (var3 instanceof LdapLocation) {
               this.collectSourceLdapLocationAndStructureLink(var2);
            }
         } else {
            LinkUtils.logMissingLink(this.transformation, EntityType.Source, var1);
         }
      }
   }

   private void collectSourceDbLocationAndStructureLink(Source var1) {
      DataStructure var2 = this.transformation.getInputStructure();
      DataLocation var3 = var1.getLocation();
      this.dbStructureAndLocationSanityCheck(var2, var3, "input");
      DatabaseLocation var4 = (DatabaseLocation)var3;
      DatabaseStructure var5 = (DatabaseStructure)var2;
      var5.setDbLocation(var4);
      var5.setSourceId(var1.getID());
   }

   private void collectSourceLdapLocationAndStructureLink(Source var1) {
      LdapLocation var2 = (LdapLocation)var1.getLocation();
      LdapStructure var3 = (LdapStructure)this.transformation.getInputStructure();
      var3.setLdapLocation(var2);
      var2.setSourceId(var1.getID());
   }

   private void collectTargetLink() {
      TargetId var1 = this.transformation.getTargetId();
      if (var1 != null) {
         Target var2 = this.dataLookup.getEntity(var1, Target.class);
         if (var2 != null) {
            this.collectedLinks.add(new StrongEntityLink(var2, this.transformation));
            DataLocation var3 = var2.getLocation();
            if (var3 instanceof DatabaseLocation) {
               this.collectTargetDbLocationAndStructureLink(var2);
            } else if (var3 instanceof LdapLocation) {
               this.collectTargetLdapLocationAndStructureLink(var2);
            }
         } else {
            LinkUtils.logMissingLink(this.transformation, EntityType.Target, var1);
         }
      }
   }

   private void collectTargetDbLocationAndStructureLink(Target var1) {
      DataStructure var2 = this.transformation.getOutputStructure();
      DataLocation var3 = var1.getLocation();
      this.dbStructureAndLocationSanityCheck(var2, var3, "output");
      DatabaseLocation var4 = (DatabaseLocation)var3;
      DatabaseStructure var5 = (DatabaseStructure)var2;
      var5.setDbLocation(var4);
      var5.setTargetId(var1.getID());
   }

   private void collectTargetLdapLocationAndStructureLink(Target var1) {
      LdapLocation var2 = (LdapLocation)var1.getLocation();
      LdapStructure var3 = (LdapStructure)this.transformation.getOutputStructure();
      var3.setLdapLocation(var2);
   }

   private void dbStructureAndLocationSanityCheck(DataStructure var1, DataLocation var2, String var3) {
      Preconditions.checkState(var1 != null, this.appendTransformationInfoToErrorMessage("No " + var3 + " structure defined."));
      Preconditions.checkState(
         var1.getStructureType() == DataStructureType.Database,
         this.appendTransformationInfoToErrorMessage("Unexpected structure; expected Database but found " + var1.getStructureType() + ".")
      );
      Preconditions.checkState(
         var2.getDataLocationType() == DataLocationType.Database,
         this.appendTransformationInfoToErrorMessage("Unexpected location; expected Database but found " + var2.getDataLocationType() + ".")
      );
   }

   private String appendTransformationInfoToErrorMessage(String var1) {
      return var1 + " Transformation: \"" + this.transformation.getName() + "\" [GUID: " + this.transformation.getID() + "]";
   }

   private void collectFunctionCallLinks() {
      TransformationFunctionCallLinkFactory var1 = new TransformationFunctionCallLinkFactory(this.transformation, this.dataLookup);
      List var2 = var1.getLinks();
      if (var2 != null) {
         this.collectedLinks.addAll(var2);
      }
   }

   private void collectDocumentLinks() {
      TextDocumentId var1 = this.transformation.getSourceTextDocumentId();
      TextDocumentId var2 = this.transformation.getTargetTextDocumentId();
      if (var1 != null || var2 != null) {
         TransformationDocumentLinkFactory var3 = new TransformationDocumentLinkFactory(this.transformation, this.dataLookup);
         if (KongaID.areEqual(var1, var2)) {
            this.onSameSourceAndTargetDocument(var3);
         } else {
            this.collectSourceDocumentLink(var3);
            this.collectTargetDocumentLink(var3);
         }
      }
   }

   private void onSameSourceAndTargetDocument(TransformationDocumentLinkFactory var1) {
      EntityLink var2 = var1.getInputAndOutputLink();
      if (var2 != null) {
         this.collectedLinks.add(var2);
         TextDocument var3 = (TextDocument)var2.getMainObject();
         this.inputStructure = var3.getDataStructure();
         this.outputStructure = var3.getDataStructure();
      }
   }

   private void collectSourceDocumentLink(TransformationDocumentLinkFactory var1) {
      EntityLink var2 = var1.getInputLink();
      if (var2 != null) {
         this.collectedLinks.add(var2);
         TextDocument var3 = (TextDocument)var2.getMainObject();
         this.inputStructure = var3.getDataStructure();
      }
   }

   private void collectTargetDocumentLink(TransformationDocumentLinkFactory var1) {
      EntityLink var2 = var1.getOutputLink();
      if (var2 != null) {
         this.collectedLinks.add(var2);
         TextDocument var3 = (TextDocument)var2.getMainObject();
         this.outputStructure = var3.getDataStructure();
      }
   }
}
