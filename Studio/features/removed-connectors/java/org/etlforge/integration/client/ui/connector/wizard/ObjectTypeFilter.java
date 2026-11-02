package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.util.name.Nameable;

public interface ObjectTypeFilter extends Predicate<ConnectorObject>, Nameable {
   ObjectTypeFilter ALL = new ObjectTypeFilter() {
      @Override
      public String getName() {
         return "All Objects";
      }

      public boolean apply(ConnectorObject var1) {
         Preconditions.checkNotNull(var1);
         return true;
      }
   };
}
