package org.etlforge.integration.design.util;

public interface SchemaRenameStrategy {
   String newName(String var1);

   void checkLocation(String var1, String var2) throws Exception;
}
