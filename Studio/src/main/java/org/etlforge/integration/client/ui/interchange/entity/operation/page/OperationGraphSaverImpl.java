package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.GraphLayoutStore;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.OperationFlow;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.util.name.IllegalNameException;

final class OperationGraphSaverImpl implements OperationGraphSaver {
   private final Operation operation;
   private final GraphSettingsSaver settingsSaver;
   private final EdiPluginSaver ediPluginSaver;

   public OperationGraphSaverImpl(Operation var1, GraphLayoutStore var2, EdiPluginSaver var3) {
      this.operation = var1;
      this.settingsSaver = new GraphSettingsSaver(var2);
      this.ediPluginSaver = var3;
   }

   @Override
   public void saveGraphLogic(OperationFlow var1) {
      var1.startSave();

      try {
         HashMap var2 = Maps.newHashMap();
         this.saveDirtyOperations(var1, var2);
         if (!var2.isEmpty()) {
            this.reportErrors(var2);
         }
      } finally {
         var1.saveComplete();
      }
   }

   @Override
   public void saveGraphSettings(OperationGraph2D var1) {
      this.settingsSaver.save(var1);
   }

   private void saveDirtyOperations(OperationFlow var1, Map<OperationWrapper, Exception> var2) {
      ProjectPersistor var3 = this.getProjectPersistor();

      for (OperationWrapper var5 : var1.getDirtyOperations()) {
         try {
            var5.applyToOperation();
            var3.save(var5.getActivity());
            this.ediPluginSaver.applyPluginIfNeeded(var5.getPipeline());
         } catch (IllegalNameException var7) {
            var2.put(var5, var7);
         } catch (InterchangeSavingException var8) {
            var2.put(var5, var8);
         }
      }

      this.ediPluginSaver.saveChanges();
   }

   private ProjectPersistor getProjectPersistor() {
      ManagedProject var1 = ProjectUtils.getManagedProject(this.operation);
      return var1.getProjectPersistor();
   }

   private void reportErrors(Map<OperationWrapper, Exception> var1) {
   }
}
