package org.etlforge.integration.client.ui.interchange.location.export;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.name.NameSorter;

final class SourceTargetExportHelper {
   private final ProjectPersistor persistor;
   private WaitService waitService = WaitService.NONE;

   public SourceTargetExportHelper(ProjectPersistor var1) {
      this.persistor = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void runWhileWaiting(Runnable var1) {
      KongaSwingWorker.runWhileWaiting(var1, this.waitService);
   }

   public List<IntegrationEntity> getAvailableExportDestinations(Source var1) {
      DataLocationType var2 = var1.getLocation().getDataLocationType();
      return this.getAvailableDestinations(var1, var2);
   }

   public List<IntegrationEntity> getAvailableExportDestinations(Target var1) {
      DataLocationType var2 = var1.getLocation().getDataLocationType();
      return this.getAvailableDestinations(var1, var2);
   }

   public void save(final List<? extends IntegrationEntity> var1, final CallbackResult var2) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SourceTargetExportHelper.this.saveImpl(var1, var2);
         }
      });
   }

   private void saveImpl(List<? extends IntegrationEntity> var1, CallbackResult var2) {
      IntegrationProjectLock.acquire();

      try {
         for (IntegrationEntity var4 : var1) {
            this.persistor.save(var4);
         }

         var2.succeeded(null);
      } catch (InterchangeSavingException var8) {
         var2.failed(var8);
      } finally {
         IntegrationProjectLock.release();
      }
   }

   public void doExport(DataLocation var1, List<? extends IntegrationEntity> var2) {
      ExportSettingsRunner var3 = ExportSettingsDialog.display(null, var1, var2);
      if (var3 != null) {
         try {
            var3.doExport(this.persistor);
         } catch (InterchangeSavingException var5) {
         }
      }
   }

   private List<IntegrationEntity> getAvailableDestinations(IntegrationEntity var1, DataLocationType var2) {
      IntegrationProject var3 = ProjectUtils.getProject(var1);
      ArrayList var4 = Lists.newArrayList();
      var4.addAll(this.getLocatables(var3, Source.class, var2));
      var4.addAll(this.getLocatables(var3, Target.class, var2));
      var4.remove(var1);
      return var4;
   }

   private <T extends IntegrationEntity> List<T> getLocatables(IntegrationProject var1, Class<T> var2, DataLocationType var3) {
      return Iterables2.sortedCopy(Iterables.filter(var1.getAllEntitiesOfClass(var2), new LocatableFilter(var3)), NameSorter.ignoreCase());
   }
}
