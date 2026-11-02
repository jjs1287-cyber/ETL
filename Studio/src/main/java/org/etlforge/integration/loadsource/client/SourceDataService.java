package org.etlforge.integration.loadsource.client;

import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataResult;
import org.etlforge.system.callback.CallbackResult;

public interface SourceDataService {
   void loadSourceData(CallbackResult var1);

   void loadSourceData(LoadSourceDataInput var1, CallbackResult var2);

   void storeSourceData(LoadSourceDataResult var1);

   SourceDataIdentifier getSourceDataToken();
}
