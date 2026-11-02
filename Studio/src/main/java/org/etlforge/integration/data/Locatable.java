package org.etlforge.integration.data;

import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;

public interface Locatable {
   IntegrationEntityId getID();

   String getName();

   DataLocation getLocation();

   void setLocation(DataLocation var1);

   boolean isLocationTypeAllowed(DataLocationType var1);

   void setAllowedLocationTypes(EnumSet<DataLocationType> var1);

   void setPartOfSalesforceWizard(boolean var1);

   void addLocationListener(PropertyChangeListener var1);

   void addLocationListener(String var1, PropertyChangeListener var2);

   void removeLocationListener(PropertyChangeListener var1);

   void removeLocationListener(String var1, PropertyChangeListener var2);
}
