package org.etlforge.integration.data;

public interface ExportableProperty {
   String getName();

   boolean isExportedByDefault();

   boolean exportTo(IntegrationData var1);
}
