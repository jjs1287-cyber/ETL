package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;
import org.etlforge.util.PropertyDependency;

public final class QuerySelectionModel extends AbstractPropertyBasedModel {
   public static final String AVAILABLE_QUERIES = "queries";
   public static final String SELECTED_QUERY = "selected";
   public static final String XML = "xml";
   public static final String STATE = "state";
   private final ListProperty<FetchXml> availableQueries = this.newListProperty("queries");
   private final Property<FetchXml> selectedQuery = this.newProperty("selected");
   private final Property<String> xml = this.newProperty("xml", "");
   private final Property<QuerySelectionModel.State> state = this.newProperty("state", QuerySelectionModel.State.UNKNOWN);
   private volatile MSCrmEndpoint endpoint;
   private volatile String objectName;

   public QuerySelectionModel() {
      PropertyDependency.install(this.selectedQuery, this.xml, new Function<FetchXml, String>() {
         public String apply(@Nullable FetchXml var1) {
            return var1 == null ? "" : var1.getXml();
         }
      });
   }

   public MSCrmEndpoint getEndpoint() {
      return this.endpoint;
   }

   public void setEndpoint(MSCrmEndpoint var1) {
      this.endpoint = Preconditions.checkNotNull(var1, "endpoint");
   }

   public String getObjectName() {
      return this.objectName;
   }

   public void setObjectName(String var1) {
      String var2 = this.objectName;
      this.objectName = ArgCheck.checkNotEmpty(var1);
      if (!var1.equals(var2)) {
         this.state.set(QuerySelectionModel.State.UNKNOWN);
         this.availableQueries.clear();
         this.selectedQuery.set(null);
      }
   }

   public void setAvailableQueries(List<FetchXml> var1) {
      this.availableQueries.set(var1);
      this.state.set(QuerySelectionModel.State.HAS_RESULT);
      this.selectedQuery.set(null);
   }

   public void setSelectedQuery(FetchXml var1) {
      this.selectedQuery.set(var1);
   }

   public Property<FetchXml> getSelectedQueryProperty() {
      return this.selectedQuery;
   }

   public enum State {
      UNKNOWN,
      HAS_RESULT;
   }
}
