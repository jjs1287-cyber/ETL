package org.etlforge.integration.data.project;

public interface EntityRevivorCallback {
   void warning(String var1);

   void error(String var1, Exception var2);
}
