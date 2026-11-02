package org.etlforge.integration.client.ui.operation.prefs;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;
import org.etlforge.integration.data.entity.operation.options.SourceHistoryOption;

public final class ClientOperationPreferences implements OperationPreferences {
   @Override
   public void apply(Operation var1) {
      this.applySchedulePolicyPreference(var1);
      this.applyHistoryPreference(var1);
   }

   private void applySchedulePolicyPreference(Operation var1) {
      SchedulePolicy var2 = DefaultSchedulePolicyPreference.get();
      var1.setSchedulePolicy(var2);
   }

   private void applyHistoryPreference(Operation var1) {
      SourceHistoryOption var2 = new SourceHistoryOption(DefaultSourceHistorySettingPreference.INSTANCE.get());
      var2.applyTo(var1);
   }
}
