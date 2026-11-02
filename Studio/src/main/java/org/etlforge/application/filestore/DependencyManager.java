package org.etlforge.application.filestore;

import java.util.Map;
import java.util.Set;

interface DependencyManager {
   void add(String var1, String var2);

   Set<String> getRequiredFiles(String var1);

   void deleteAll();

   void copy(Map<String, String> var1);
}
