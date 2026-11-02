package org.etlforge.integration.database.driver;

public interface DatabaseDriverQuoteHandler {
   boolean isNameWrapped(String var1);

   String wrapName(String var1);

   String unwrapName(String var1);
}
