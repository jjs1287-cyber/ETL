package org.etlforge.integration.structure.mapping;

import org.etlforge.integration.structure.crom.CROM;

public interface MappingStructureReadingHandler {
   void setRoot(CROM var1);

   void setCromType(String var1);

   String getCromType();

   void addElement(CROM var1);

   void addType(String var1, CROM var2);

   void checkDerivedTypeHolder(CROM var1);

   void setDBQuotes(String var1, String var2);
}
