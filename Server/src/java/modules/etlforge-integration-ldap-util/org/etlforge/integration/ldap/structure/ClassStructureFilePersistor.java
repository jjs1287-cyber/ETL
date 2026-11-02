package org.etlforge.integration.ldap.structure;

import java.io.File;

public interface ClassStructureFilePersistor {
   void store(ObjectClassStructure var1, File var2);

   ObjectClassStructure load(File var1);
}
