package com.etlforge.integration.client.ui.sfdc.wizard;

import com.etlforge.integration.client.ui.sfdc.wizard.prefs.WriteFilesAsUTF8Preference;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocationType;

public final class TargetLocationPage extends LocationPage<Target> {
   public TargetLocationPage(SalesforceWizard<?> var1, Iterable<DataLocationType> var2, EditorPanel var3) {
      super(var1, "Select Where You Want To Put Your Data", createModel(var2), var3);
      this.suggestFileName("extract.csv");
   }

   private static LocationPageModel<Target> createModel(Iterable<DataLocationType> var0) {
      LocationPageModel var1 = LocationPageModel.forTargets(var0);
      boolean var2 = WriteFilesAsUTF8Preference.INSTANCE.get();
      var1.setUTF8(var2);
      return var1;
   }
}
