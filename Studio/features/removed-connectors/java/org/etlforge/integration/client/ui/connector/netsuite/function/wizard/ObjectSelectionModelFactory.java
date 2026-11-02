package org.etlforge.integration.client.ui.connector.netsuite.function.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.etlforge.integration.client.ui.connector.wizard.DefaultNameOption;
import org.etlforge.integration.client.ui.connector.wizard.NameOption;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectTypeFilter;

public class ObjectSelectionModelFactory {
   public static ObjectSelectionModel createObjectSelectionModel(List<ObjectTypeFilter> var0) {
      ObjectSelectionModel var1 = new ObjectSelectionModel(var0);
      installNameOptionStrategy(var1);
      return var1;
   }

   private static void installNameOptionStrategy(ObjectSelectionModel var0) {
      ObjectSelectionModelFactory.NameOptionInstaller var1 = new ObjectSelectionModelFactory.NameOptionInstaller(var0);
      var1.setInitialOption();
      var0.addPropertyChangeListener("type_filter", var1);
   }

   private static final class NameOptionInstaller implements PropertyChangeListener {
      private final ObjectSelectionModel model;
      private final NameOption defaultOption = new DefaultNameOption("Name");
      private final NameOption optionForAll = new AllObjectsNameOption();

      private NameOptionInstaller(ObjectSelectionModel var1) {
         this.model = var1;
      }

      public void setInitialOption() {
         ObjectTypeFilter var1 = this.model.getSelectedType();
         this.setNameOption(var1);
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         ObjectTypeFilter var2 = (ObjectTypeFilter)var1.getNewValue();
         this.setNameOption(var2);
      }

      private void setNameOption(ObjectTypeFilter var1) {
         NameOption var2 = var1 == ObjectTypeFilter.ALL ? this.optionForAll : this.defaultOption;
         this.model.setNameOption(var2);
      }
   }
}
