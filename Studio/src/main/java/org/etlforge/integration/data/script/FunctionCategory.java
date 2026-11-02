package org.etlforge.integration.data.script;

import org.etlforge.util.version.Version;

public interface FunctionCategory {
   String getName();

   Version since();

   Iterable<Function> getFunctions();
}
