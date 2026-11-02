package com.etlforge.integration.client.ui.sfdc.ucu.page;

import java.util.List;
import java.util.logging.Logger;

interface RequestMappingsModifier {
   void modifyMapList(List<String> var1);

   void setLogger(Logger var1);
}
