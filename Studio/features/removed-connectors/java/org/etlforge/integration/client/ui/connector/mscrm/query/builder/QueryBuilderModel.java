package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.collect.ImmutableList;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;

public interface QueryBuilderModel {
   MSCrmObject getObject();

   String getTitle();

   ImmutableList<MSCrmObject> getPath();

   AttributeSelectionModel getAttributeSelectionModel();

   AttributeOrderModel getAttributeOrderModel();

   FilterDefinitionModel getFilterDefinitionModel();

   LinkEntitiesModel getLinkEntitiesModel();

   String buildXml();

   void listenToAllChanges(PropertyChangeListener var1);

   void stopListeningToAllChanges(PropertyChangeListener var1);
}
