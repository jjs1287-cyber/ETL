package org.etlforge.integration.client.database;

import org.etlforge.integration.database.driver.DriverDescriptor;

public interface DriverDescriptorFilter {
   boolean accepts(DriverDescriptor var1);
}
