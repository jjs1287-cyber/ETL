package org.etlforge.integration.client.ui.project.trashcan;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.DoNotShowAgainQuestion;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class RevivalInspector {
   private final Collection<?> toRevive;
   private final Collection<?> remaining;
   private final ProjectDependencies dependencies;
   private Set<InspectionUtils.Key> initialObjects;

   public RevivalInspector(Collection<?> var1, Collection<?> var2, ProjectDependencies var3) {
      this.toRevive = var1;
      this.remaining = var2;
      this.dependencies = var3;
   }

   public Object[] inspect() {
      if (this.toRevive.isEmpty()) {
         return this.toRevive.toArray();
      } else {
         this.initialObjects = InspectionUtils.getSet(this.toRevive);
         Set var1 = this.getRequiredObjects();
         if (var1.isEmpty()) {
            return this.toRevive.toArray();
         } else {
            Set var2 = this.getAdditionalObjectsToRevive(var1);
            if (var2.isEmpty()) {
               return this.toRevive.toArray();
            } else if (this.askUserAboutAdditionalObjects(var2)) {
               this.initialObjects.addAll(var2);
               return InspectionUtils.fromKeys(this.initialObjects).toArray();
            } else {
               return new Object[0];
            }
         }
      }
   }

   private Set<InspectionUtils.Key> getRequiredObjects() {
      HashSet var1 = Sets.newHashSet();

      for (InspectionUtils.Key var3 : this.initialObjects) {
         IntegrationEntity var4 = var3.getObject();
         Set var5 = new RevivalInspector.RequiredObjectsCollector().collect(var4);
         var1.addAll(var5);
      }

      return var1;
   }

   private Set<InspectionUtils.Key> getAdditionalObjectsToRevive(Set<InspectionUtils.Key> var1) {
      HashSet var2 = Sets.newHashSet();
      Set var3 = InspectionUtils.getSet(this.remaining);

      for (InspectionUtils.Key var5 : var1) {
         if (var3.contains(var5) && !this.initialObjects.contains(var5)) {
            var2.add(var5);
         }
      }

      return var2;
   }

   private boolean askUserAboutAdditionalObjects(Set<InspectionUtils.Key> var1) {
      RevivalInspector.Question var2 = new RevivalInspector.Question(InspectionUtils.fromKeys(var1));
      return var2.ask();
   }

   private static class Question implements UiProvider {
      private final IntegrationEntityList list;

      public Question(List<IntegrationEntity> var1) {
         this.list = new IntegrationEntityList(var1);
      }

      public boolean ask() {
         DoNotShowAgainQuestion var1 = new DoNotShowAgainQuestion(
            this,
            ProjectUiResources.getString("Trashcan.DependenciesDetected"),
            ReviveDependenciesWarningPreference.instance(),
            Alert.Option.CONTINUE_CANCEL,
            Alert.Option.CONTINUE,
            Alert.Type.QUESTION
         );
         return var1.ask(null);
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
         var2.northToSouth(ProjectUiResources.getString("Trashcan.RevivalInspector.Message"), this.list, "Do you want to continue?");
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
      }
   }

   private class RequiredObjectsCollector {
      private final Set<InspectionUtils.Key> required = Sets.newHashSet();

      private RequiredObjectsCollector() {
      }

      public Set<InspectionUtils.Key> collect(IntegrationEntity var1) {
         for (IntegrationEntity var3 : RevivalInspector.this.dependencies.getRequiredObjects(var1)) {
            this.process(var3);
         }

         this.processParents(var1);
         return this.required;
      }

      private void process(IntegrationEntity var1) {
         if (this.required.add(new InspectionUtils.Key(var1))) {
            this.collect(var1);
         }
      }

      private void processParents(IntegrationEntity var1) {
         for (IntegrationEntity var2 = var1.getParent(); var2 != null && !(var2 instanceof RootFolder); var2 = var2.getParent()) {
            this.process(var2);
         }
      }
   }
}
