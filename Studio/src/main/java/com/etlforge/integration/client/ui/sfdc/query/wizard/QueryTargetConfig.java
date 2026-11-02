package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.location.DataLocationType;

public interface QueryTargetConfig {
   ImmutableSet<DataLocationType> getLocationTypes();

   ImmutableSet<QueryTargetType> getStructureTypes(DataLocationType var1);

   ImmutableSet<String> getFileExtensions();
}
