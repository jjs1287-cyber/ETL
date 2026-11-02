package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.location.DataLocationType;

public interface RequestSourceConfig {
   ImmutableSet<DataLocationType> getLocationTypes();

   ImmutableSet<RequestSourceType> getStructureTypes(DataLocationType var1);

   ImmutableSet<String> getFileExtensions();
}
