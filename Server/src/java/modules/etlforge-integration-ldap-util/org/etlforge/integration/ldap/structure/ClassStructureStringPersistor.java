package org.etlforge.integration.ldap.structure;

public interface ClassStructureStringPersistor {
   String store(ObjectClassStructure var1);

   ObjectClassStructure load(String var1);
}
