package org.etlforge.integration.client.project;

import com.google.common.collect.FluentIterable;
import java.io.File;
import java.util.List;
import org.etlforge.application.prefs.Preference;
import org.etlforge.application.prefs.PreferenceListener;
import org.etlforge.integration.client.project.prefs.TestHistoryAgePreference;
import org.etlforge.integration.client.project.test.TestResultHistoryFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.test.LoadSourceHistory;
import org.etlforge.integration.data.test.TestConnectionHistory;
import org.etlforge.integration.data.test.TestOperationHistory;
import org.etlforge.integration.data.test.TestTransformationHistory;
import org.etlforge.io.Folder;
import org.etlforge.system.Disposable;

final class ProjectTestHistoryImpl implements ProjectTestHistory, Disposable {
   private final ManagedProject project;
   private final TestTransformationHistory testTransformationHistory;
   private final TestOperationHistory testOperationHistory;
   private final LoadSourceHistory loadSourceHistory;
   private final TestConnectionHistory testConnectionHistory;
   private final ProjectTestHistoryImpl.AgePreferenceListener agePrefListener;

   public ProjectTestHistoryImpl(ManagedProject var1) {
      assert var1 != null : "project cannot be null";
      this.project = var1;
      Folder var2 = this.getRootDirectory(var1);
      TestResultHistoryFactory var3 = TestResultHistoryFactory.newFileBasedHistoryFactory(var2);
      this.testTransformationHistory = var3.newTestTransformationHistory();
      this.testOperationHistory = var3.newTestOperationHistory();
      this.loadSourceHistory = var3.newLoadSourceHistory();
      this.testConnectionHistory = var3.newTestConnectionHistory();
      this.agePrefListener = new ProjectTestHistoryImpl.AgePreferenceListener();
      TestHistoryAgePreference.INSTANCE.addListener(this.agePrefListener);
      this.applyAgePreference();
   }

   private Folder getRootDirectory(ManagedProject var1) {
      File var2 = var1.getLocation();
      return ProjectFolders.getTestHistoryFolder(var2);
   }

   private void applyAgePreference() {
      new ProjectTestHistoryImpl.AgeSetter().runInBackground();
   }

   @Override
   public void dispose() {
      TestHistoryAgePreference.INSTANCE.removeListener(this.agePrefListener);
   }

   @Override
   public LoadSourceHistory getLoadSourceHistory() {
      return this.loadSourceHistory;
   }

   @Override
   public TestOperationHistory getTestOperationHistory() {
      return this.testOperationHistory;
   }

   @Override
   public TestTransformationHistory getTestTransformationHistory() {
      return this.testTransformationHistory;
   }

   @Override
   public TestConnectionHistory getTestConnectionHistory() {
      return this.testConnectionHistory;
   }

   void onProjectClosed() {
      TestHistoryAgePreference.INSTANCE.removeListener(this.agePrefListener);
      this.removeDeletedTransformations();
      this.removeDeletedOperations();
      this.removeDeletedSources();
      this.removeDeletedTargets();
   }

   private void removeDeletedTransformations() {
      for (Transformation var2 : this.getDeletedEntities(Transformation.class)) {
         this.testTransformationHistory.removeAll(var2);
         this.loadSourceHistory.removeAll(var2);
      }
   }

   private void removeDeletedOperations() {
      for (Operation var2 : this.getDeletedEntities(Operation.class)) {
         this.testOperationHistory.removeAll(var2);
      }
   }

   private void removeDeletedSources() {
      for (Source var2 : this.getDeletedEntities(Source.class)) {
         this.testConnectionHistory.removeAll(var2);
      }
   }

   private void removeDeletedTargets() {
      for (Target var2 : this.getDeletedEntities(Target.class)) {
         this.testConnectionHistory.removeAll(var2);
      }
   }

   private <T extends IntegrationEntity> Iterable<T> getDeletedEntities(Class<T> var1) {
      List var2 = this.project.getTrashCan().getContents();
      return FluentIterable.from(var2).filter(var1);
   }

   private class AgePreferenceListener implements PreferenceListener<Integer> {
      private AgePreferenceListener() {
      }

      public void valueChanged(Preference<Integer> var1, Integer var2, Integer var3) {
         ProjectTestHistoryImpl.this.applyAgePreference();
      }
   }

   private final class AgeSetter implements Runnable {
      private AgeSetter() {
      }

      @Override
      public void run() {
         int var1 = TestHistoryAgePreference.get();
         ProjectTestHistoryImpl.this.testConnectionHistory.setMaximumResultAge(var1);
         ProjectTestHistoryImpl.this.testOperationHistory.setMaximumResultAge(var1);
         ProjectTestHistoryImpl.this.testTransformationHistory.setMaximumResultAge(var1);
         ProjectTestHistoryImpl.this.loadSourceHistory.setMaximumResultAge(var1);
      }

      public void runInBackground() {
         Thread var1 = new Thread(this);
         var1.setPriority(2);
         var1.start();
      }
   }
}
