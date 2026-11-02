package org.etlforge.integration.data.entity;

import org.etlforge.integration.salesforce.StartAtRowProperty;

public interface SalesforceModifier extends SalesforceWizardEntity {
   String SOURCE_FILE_HAS_HEADER = "source_file_has_header";
   String START_AT_ROW = "start_at_row";
   String SOURCE_FILE_ENCODING = "source_encoding";
   long DEFAULT_START_AT_ROW = 1L;

   void setSourceFileHasHeader(boolean var1);

   boolean getSourceFileHasHeader();

   void setStartAtRow(StartAtRowProperty var1);

   StartAtRowProperty getStartAtRow();
}
