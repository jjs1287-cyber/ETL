package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ChoiceMessage;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.mapping.AbstractMappingStructureFactory;
import org.etlforge.integration.client.mapping.text.ComplexTextMappingStructureFactory;
import org.etlforge.integration.client.mapping.text.SimpleTextMappingStructureFactory;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.client.ui.interchange.entity.transformation.TransformationsRefresher;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.label.HtmlLabel;

final class StructureChangeSaveConsequence implements SaveConsequence {
   private final TextDocumentPage page;
   private final TextDocument documentToSave;
   private TextStructure existingStructure;
   private TextStructure newStructure;
   private ImmutableList<TransformationsRefresher.TransformationToRefresh> affectedTransformations;
   private StructureChangeSaveConsequence.Choice usersDecision;

   public StructureChangeSaveConsequence(TextDocumentPage var1) {
      this.page = var1;
      this.documentToSave = var1.getObject();
      this.existingStructure = this.getExistingStructure();
   }

   private TextStructure getExistingStructure() {
      TextStructure var1 = this.documentToSave.getDataStructure();
      return var1 != null ? var1.clone() : null;
   }

   @Override
   public boolean isAllowed() {
      this.usersDecision = null;
      this.affectedTransformations = this.collectAffectedTransformations();
      if (!this.isStructureChange()) {
         return true;
      }

      if (this.affectedTransformations.isEmpty()) {
         return true;
      }

      this.usersDecision = this.getUsersDecision();
      return this.usersDecision != StructureChangeSaveConsequence.Choice.CANCEL;
   }

   @Override
   public void apply() {
      if (this.usersDecision == StructureChangeSaveConsequence.Choice.SAVE_AND_REFRESH) {
         this.refreshTransformations();
      }

      this.existingStructure = this.newStructure != null ? this.newStructure.clone() : null;
   }

   private void refreshTransformations() {
      ApplicationWindow var1 = this.page.getWindow();
      AbstractMappingStructureFactory var2 = this.newStructure.isFlat() ? new SimpleTextMappingStructureFactory() : new ComplexTextMappingStructureFactory();
      TransformationsRefresher var3 = new TransformationsRefresher(var1, var2);
      ManagedProject var4 = ProjectUtils.getManagedProject(this.documentToSave);
      var3.setCache(var4.getTransformationMappingStructureCache());
      StructureChangeSaveConsequence.RefreshJob var5 = new StructureChangeSaveConsequence.RefreshJob(var3, this.affectedTransformations);
      Application.getWorker().submitForParallel(var5);
   }

   private ImmutableList<TransformationsRefresher.TransformationToRefresh> collectAffectedTransformations() {
      IntegrationProject var1 = ProjectUtils.getProject(this.documentToSave);
      if (var1 == null) {
         return ImmutableList.of();
      }

      DependencyStore var2 = var1.getCurrentDependencies();

      try {
         return var2.getDependingObjects(this.documentToSave)
            .filter(EntityFilters.allOfType(EntityType.Transformation))
            .transform(new StructureChangeSaveConsequence.Transform(this.documentToSave))
            .toImmutableList();
      } finally {
         var2.dispose();
      }
   }

   private boolean isStructureChange() {
      try {
         this.newStructure = this.createNewStructure();
         return !this.newStructure.mappingStructureEquals(this.existingStructure);
      } catch (TextDocumentDefinitionException var2) {
         return false;
      }
   }

   private TextStructure createNewStructure() throws TextDocumentDefinitionException {
      TextDocument var1 = new TextDocument("Dummy");
      var1.setDocType(this.documentToSave.getDocType());
      var1.createTextStructure();
      this.page.applyStructure(var1);
      return var1.getDataStructure();
   }

   private StructureChangeSaveConsequence.Choice getUsersDecision() {
      return UiThreadSupplier.call(new StructureChangeSaveConsequence.UserPrompt(this.page, this.affectedTransformations));
   }

   private enum Choice {
      SAVE_AND_REFRESH("Save and Refresh"),
      JUST_SAVE("Just Save"),
      CANCEL("Cancel");

      private final String display;

      Choice(String var3) {
         this.display = var3;
      }

      @Override
      public String toString() {
         return this.display;
      }
   }

   private static class RefreshJob implements ApplicationWorker.SafeRunnable {
      private final TransformationsRefresher refresher;
      private final List<TransformationsRefresher.TransformationToRefresh> transformations;

      public RefreshJob(TransformationsRefresher var1, List<TransformationsRefresher.TransformationToRefresh> var2) {
         this.refresher = var1;
         this.transformations = var2;
      }

      @Override
      public void run() {
         this.refresher.refresh(this.transformations);
      }
   }

   private static class Transform implements Function<IntegrationEntity, TransformationsRefresher.TransformationToRefresh> {
      private final TextDocumentId docId;

      public Transform(TextDocument var1) {
         this.docId = var1.getID();
      }

      public TransformationsRefresher.TransformationToRefresh apply(IntegrationEntity var1) {
         Transformation var2 = (Transformation)var1;
         boolean var3 = this.docId.equals(var2.getSourceTextDocumentId());
         boolean var4 = this.docId.equals(var2.getTargetTextDocumentId());
         return new TransformationsRefresher.TransformationToRefresh(var2, var3, var4);
      }
   }

   private static class UserPrompt implements Supplier<StructureChangeSaveConsequence.Choice> {
      private final TextDocumentPage page;
      private final ImmutableList<Transformation> transformations;

      public UserPrompt(TextDocumentPage var1, ImmutableList<TransformationsRefresher.TransformationToRefresh> var2) {
         this.page = var1;
         this.transformations = FluentIterable.from(var2).transform(TransformationsRefresher.TransformationToRefresh.toTransformation()).toImmutableList();
      }

      public StructureChangeSaveConsequence.Choice get() {
         UiProvider var1 = this.createLayout();
         ChoiceMessage var2 = new ChoiceMessage<>(var1, "Refresh Transformations?", StructureChangeSaveConsequence.Choice.values());
         var2.setType(Alert.Type.WARNING);
         var2.setDefaultChoice(StructureChangeSaveConsequence.Choice.SAVE_AND_REFRESH);
         var2.setChoiceIfCancelled(StructureChangeSaveConsequence.Choice.CANCEL);
         return (StructureChangeSaveConsequence.Choice)var2.show(this.page.getWindow().getFrame());
      }

      private UiProvider createLayout() {
         IntegrationEntityList var1 = new IntegrationEntityList(this.transformations);
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
         String var3 = "You are about to change the structure of this text document. The following transformations must be refreshed in order for the new structure to be applied.";
         var2.north(new HtmlLabel(var3, 400)).center(var1);
         return var2;
      }
   }
}
