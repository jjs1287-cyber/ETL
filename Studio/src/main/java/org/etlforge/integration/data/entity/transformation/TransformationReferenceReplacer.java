package org.etlforge.integration.data.entity.transformation;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.script.ExpressionReferenceReplacer;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.script.GrammarUtils;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.system.callback.Receiver;

public final class TransformationReferenceReplacer {
   private final Transformation tx;
   private final ReferenceReplacementService service;

   public TransformationReferenceReplacer(Transformation var1, ReferenceReplacementService var2) {
      this.tx = var1;
      this.service = var2;
   }

   public void replaceReferences() {
      this.replaceTextDocuments();
      this.processStructures();
      this.processMappings();
      this.processSfdcSessionIdMapping();
   }

   private void replaceTextDocuments() {
      TextDocumentId var1 = this.tx.getSourceTextDocumentId();
      if (var1 != null) {
         TextDocument var2 = this.service.getReplacement(var1, TextDocument.class);
         if (var2 != null) {
            this.tx.setSourceTextDocumentId(var2.getID());
            this.tx.setInputStructure(var2.getDataStructure());
         }
      }

      var1 = this.tx.getTargetTextDocumentId();
      if (var1 != null) {
         TextDocument var4 = this.service.getReplacement(var1, TextDocument.class);
         if (var4 != null) {
            this.tx.setTargetTextDocumentId(var4.getID());
            this.tx.setOutputStructure(var4.getDataStructure());
         }
      }
   }

   private void processStructures() {
      this.processInputStructure();
      this.processOutputStructure();
   }

   private void processInputStructure() {
      DataStructure var1 = this.tx.getInputStructure();
      if (var1 != null) {
         switch (var1.getStructureType()) {
            case LDAP:
               LdapStructure var6 = (LdapStructure)var1;
               SourceId var7 = var6.getLdapLocation().getSourceId();
               Source var8 = this.service.getReplacement(var7, Source.class);
               if (var8 != null) {
                  this.tx.setSourceId(var8.getID());
                  var6.setLdapLocation((LdapLocation)var8.getLocation());
               }
               break;
            case Database:
               DatabaseStructure var5 = (DatabaseStructure)var1;
               SourceId var3 = var5.getSourceId();
               Source var4 = this.service.getReplacement(var3, Source.class);
               if (var4 != null) {
                  this.tx.setSourceId(var4.getID());
                  var5.setSourceId(var4.getID());
                  var5.setDbLocation((DatabaseLocation)var4.getLocation());
               }
               break;
            default:
               if (var1 instanceof FunctionCallStructure) {
                  FunctionCallStructure var2 = (FunctionCallStructure)var1;
                  this.replaceFunctionCall(var2, TransformationStructureSetter.inputSetter().asReceiver(this.tx));
               }
         }
      }
   }

   private void replaceFunctionCall(FunctionCallStructure var1, Receiver<DataStructure> var2) {
      Function var3 = this.getFunctionReplacement(var1);
      FunctionStructureGetter var4 = FunctionStructureGetter.getter(var1);
      DataStructure var5 = var4.getStructure(var3);
      var2.handle(var5);
   }

   private Function getFunctionReplacement(FunctionCallStructure var1) {
      IntegrationEntityId var2 = var1.getFunctionCallId();
      IntegrationEntity var3 = this.service.getReplacement(var2);
      return (Function)var3;
   }

   private void processOutputStructure() {
      DataStructure var1 = this.tx.getOutputStructure();
      if (var1 != null) {
         switch (var1.getStructureType()) {
            case LDAP:
               LdapStructure var6 = (LdapStructure)var1;
               TargetId var7 = var6.getLdapLocation().getTargetId();
               Target var8 = this.service.getReplacement(var7, Target.class);
               if (var8 != null) {
                  this.tx.setTargetId(var8.getID());
                  var6.setLdapLocation((LdapLocation)var8.getLocation());
               }
               break;
            case Database:
               DatabaseStructure var5 = (DatabaseStructure)var1;
               TargetId var3 = var5.getTargetId();
               Target var4 = this.service.getReplacement(var3, Target.class);
               if (var4 != null) {
                  this.tx.setTargetId(var4.getID());
                  var5.setTargetId(var4.getID());
                  var5.setDbLocation((DatabaseLocation)var4.getLocation());
               }
               break;
            default:
               if (var1 instanceof FunctionCallStructure) {
                  FunctionCallStructure var2 = (FunctionCallStructure)var1;
                  this.replaceFunctionCall(var2, TransformationStructureSetter.outputSetter().asReceiver(this.tx));
               }
         }
      }
   }

   private void processMappings() {
      TransformationReferenceReplacer.MappingReplacer var1 = new TransformationReferenceReplacer.MappingReplacer();
      var1.process();
   }

   private void processSfdcSessionIdMapping() {
      SalesforceWizardRole var1 = this.tx.getSalesforceWizardRole();
      if (var1 != null && var1.position == SalesforceWizardRole.Position.REQUEST) {
         SfdcSessionIdReplacer var2 = new SfdcSessionIdReplacer(this.tx, this.service);
         var2.run();
      }
   }

   private final class MappingReplacer {
      private final ExpressionReferenceReplacer replacer = new ExpressionReferenceReplacer(TransformationReferenceReplacer.this.tx);
      private final List<String> originalMapList = TransformationReferenceReplacer.this.tx.getMapList();
      private final List<String> newMapList = Lists.newArrayList();
      private final Transform transform = new Transform();

      public MappingReplacer() {
      }

      public void process() {
         for (String var2 : this.originalMapList) {
            this.processMapping(var2);
         }

         this.applyNewMapList();
      }

      private void processMapping(String var1) {
         Mapping var2 = MapStringFormat.parse(var1);
         String var3 = this.replaceReferencesInExpression(var2.getExpression());
         this.newMapList.add(MapStringFormat.format(var2.getTargetPath().toString(), var3));
      }

      private String replaceReferencesInExpression(String var1) {
         String var2 = var1;

         for (IntegrationEntityId var4 : GrammarUtils.getAllIdsFromScript(this.transform, var1, "")) {
            IntegrationEntity var5 = TransformationReferenceReplacer.this.service.getItemLookup().getEntity(var4);
            if (var5 != null) {
               IntegrationEntity var6 = TransformationReferenceReplacer.this.service.getReplacement(var4);
               if (var6 != null) {
                  var2 = this.replacer.replace(var2, var5, var6);
               }
            }
         }

         return var2;
      }

      private void applyNewMapList() {
         if (!this.originalMapList.equals(this.newMapList)) {
            TransformationReferenceReplacer.this.tx.setMapList(this.newMapList);
         }
      }
   }
}
