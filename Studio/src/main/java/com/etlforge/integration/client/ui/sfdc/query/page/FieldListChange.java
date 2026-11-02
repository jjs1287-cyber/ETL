package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.mapping.text.TextMappingStructureFactory;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.project.copy.EntityCopyMachine;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.project.ProjectSearch;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

final class FieldListChange implements Callable<Void> {
   private final QueryStructure newQuery;
   private final FieldListChange.MappingDiff mappingDiff;
   private final IntegrationProject project;
   private final Transformation response;
   private TextDocument document;

   protected FieldListChange(SalesforceQuery var1, QueryStructure var2, FieldListChange.MappingDiff var3) {
      this.newQuery = var2;
      this.mappingDiff = var3;
      this.project = ProjectUtils.getProject(var1);
      WizardItemBrowser var4 = new WizardItemBrowser(var1);
      this.response = var4.getResponseTransformation();
   }

   public Void call() throws Exception {
      this.document = this.locateTextDocument();
      this.createNewTextStructure();
      this.updateTransformation();
      this.save();
      return null;
   }

   private TextDocument locateTextDocument() {
      TextDocument var1 = this.project.getItemLookup().getEntity(this.response.getTargetTextDocumentId(), TextDocument.class);
      assert var1 != null;
      if (this.isCloneRequired(var1)) {
         var1 = this.clone(var1);
      }

      return var1;
   }

   private boolean isCloneRequired(final TextDocument var1) {
      Predicate var2 = new Predicate<IntegrationEntity>() {
         private final TextDocumentId docId = var1.getID();

         public boolean apply(IntegrationEntity var1x) {
            Transformation var2x = (Transformation)var1x;
            return this.docId.equals(var2x.getSourceTextDocumentId()) || this.docId.equals(var2x.getTargetTextDocumentId());
         }
      };
      int var3 = ProjectSearch.in(this.project).withFilter(var2).searchFor(Transformation.class).count();
      return var3 > 1;
   }

   private TextDocument clone(TextDocument var1) {
      EntityCopyMachine var2 = new EntityCopyMachine();
      return (TextDocument)var2.createAndInsertCopy(var1);
   }

   private TextStructure createNewTextStructure() {
      TextStructure var1 = this.newQuery.buildTextStructure();
      this.document.setStructure(var1);
      return var1;
   }

   private void updateTransformation() {
      WaitLock var1 = this.putResponsePageInWaitStateIfOpen();
      List var2 = this.response.getMappings();
      boolean var3 = false;

      try {
         this.response.setOutputStructure(this.document.getDataStructure());
         this.response.setTargetTextDocumentId(this.document.getID());
         this.response.setMappings(new ArrayList<>());
         this.refreshTransformation();
         List var4 = this.mappingDiff.apply(var2);
         this.response.setMappings(var4);
         var3 = true;
      } finally {
         var1.release();
         if (!var3) {
            this.response.setMappings(var2);
         }
      }
   }

   private WaitLock putResponsePageInWaitStateIfOpen() {
      TransformationPage var1 = (TransformationPage)UiCompanion.of(this.response).findOpenPage();
      WaitService var2 = var1 == null ? WaitService.NONE : var1.getWaitService();
      return var2.startWait();
   }

   private void refreshTransformation() {
      try {
         MappingStructureRefresher var1 = new MappingStructureRefresher(new TextMappingStructureFactory());
         ManagedProject var2 = ProjectUtils.getManagedProject(this.project);
         var1.setCache(var2.getTransformationMappingStructureCache());
         var1.refreshOutputStructure(this.response);
      } catch (MappingTreeStructureException var3) {
         ErrorLog.attention(this.getClass(), null, var3);
      }
   }

   private void save() {
      IntegrationProjectLock.acquire();

      try {
         ManagedProject var1 = ProjectUtils.getManagedProject(this.project);
         var1.saveProjectRelationships();
         this.document.save();
         this.response.save();
      } catch (InterchangeSavingException var5) {
         ErrorLog.attention(FieldListChangeConsequence.class, null, var5);
      } finally {
         IntegrationProjectLock.release();
      }
   }

   public interface MappingDiff {
      List<Mapping> apply(List<Mapping> var1);
   }
}
