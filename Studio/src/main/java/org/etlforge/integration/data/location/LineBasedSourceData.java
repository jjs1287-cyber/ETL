package org.etlforge.integration.data.location;

import org.etlforge.integration.data.location.file.IgnoredLines;

public interface LineBasedSourceData {
   int DEFAULT_IGNORE_FIRST_LINES = 0;
   int DEFAULT_IGNORE_LAST_LINES = 0;

   IgnoredLines getLinesToIgnore();

   void setLinesToIgnore(IgnoredLines var1);
}
