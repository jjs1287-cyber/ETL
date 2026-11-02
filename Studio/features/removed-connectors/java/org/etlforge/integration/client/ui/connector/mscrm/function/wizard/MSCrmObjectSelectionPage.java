package org.etlforge.integration.client.ui.connector.mscrm.function.wizard;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.Action;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.connector.wizard.NameOption;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionController;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionModel;
import org.etlforge.integration.client.ui.connector.wizard.ObjectSelectionPage;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.mscrm.metadata.BasicEntityDetails;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.ui.util.KeyValueToolTip;
import org.etlforge.util.string.StringLimiter;

public final class MSCrmObjectSelectionPage extends ObjectSelectionPage {
   private volatile MSCrmObjectLookup objectLookup;
   private volatile MSCrmObject selectedObject;

   public MSCrmObjectSelectionPage(Wizard var1, ObjectSelectionModel var2, ObjectSelectionController var3, Action var4) {
      super(var1, "Select an Object", var2, var3, var4);
      this.setToolTipProducer(new MSCrmObjectSelectionPage.ToolTipProducer(var2));
   }

   public void setObjectLookup(MSCrmObjectLookup var1) {
      this.objectLookup = Preconditions.checkNotNull(var1, "lookup");
   }

   public MSCrmObject getSelectedObject() {
      return this.selectedObject;
   }

   @Override
   public boolean prepareForForwardNavigation() {
      Preconditions.checkState(this.objectLookup != null, "Must provide an object lookup first");
      this.selectedObject = null;
      ObjectSelectionModel var1 = this.getModel();
      ConnectorObject var2 = var1.getSelectedObject();

      try {
         this.selectedObject = this.objectLookup.describeObject(var2.getName());
         return true;
      } catch (MSCrmMetaDataException var4) {
         ErrorLog.attention(this.getClass(), "Failed to retrieve the metadata for " + var2.getName() + ".", var4);
         return false;
      }
   }

   @Override
   protected String getForwardNavigationMessage() {
      return "Retrieving object metadata";
   }

   private static class ToolTipProducer implements Function<ConnectorObject, String> {
      private final ObjectSelectionModel model;

      public ToolTipProducer(ObjectSelectionModel var1) {
         this.model = var1;
      }

      public String apply(ConnectorObject var1) {
         BasicEntityDetails var2 = (BasicEntityDetails)var1;
         LinkedHashMap var3 = Maps.newLinkedHashMap();
         this.addAlternateName(var2, var3);
         var3.put("Description:", StringLimiter.limit(var2.getDescription(), 150));
         return KeyValueToolTip.createTable(var3);
      }

      private void addAlternateName(BasicEntityDetails var1, Map<Object, Object> var2) {
         NameOption var3 = this.model.getNameOption();
         if (var3 == NameOptions.LOGICAL_NAME) {
            var2.put("Display name:", var1.getDisplayName());
         } else if (var3 == NameOptions.DISPLAY_NAME) {
            var2.put("Logical name:", var1.getLogicalName());
         }
      }
   }
}
