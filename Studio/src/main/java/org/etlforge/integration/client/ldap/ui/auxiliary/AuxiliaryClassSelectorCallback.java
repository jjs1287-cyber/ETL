package org.etlforge.integration.client.ldap.ui.auxiliary;

import org.etlforge.integration.ldap.structure.ObjectClassStructure;

public interface AuxiliaryClassSelectorCallback {
   void completed(ObjectClassStructure[] var1);

   void failed(Exception var1);

   void cancelled();
}
