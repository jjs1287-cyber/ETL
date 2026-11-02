package org.etlforge.integration.client.ui.connector.mscrm.function.wizard;

import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import org.etlforge.integration.client.ui.connector.mscrm.prefs.NameOptionPreference;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.util.Property;

public class ObjectSelectionModelFactory {
   public static ObjectSelectionModel newModel() {
      ImmutableList var0 = ImmutableList.of();
      ObjectSelectionModel var1 = new ObjectSelectionModel(EnumSet.allOf(NameOptions.class), var0);
      Property var2 = var1.getProperty("name_option");
      AbstractBinding var3 = new AbstractBinding<NameOptions>(var2) {
         protected NameOptions getValueFromUi() {
            return NameOptionPreference.INSTANCE.get();
         }

         protected void updateUiWithValue(NameOptions var1) {
            NameOptionPreference.INSTANCE.set(var1);
         }
      };
      var3.syncModel();
      return var1;
   }

   private ObjectSelectionModelFactory() {
   }
}
