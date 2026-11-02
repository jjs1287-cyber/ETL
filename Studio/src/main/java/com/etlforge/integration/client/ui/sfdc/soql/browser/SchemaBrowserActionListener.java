package com.etlforge.integration.client.ui.sfdc.soql.browser;

import com.google.common.base.Optional;
import java.util.List;

public interface SchemaBrowserActionListener {
   void objectSelected(String var1);

   void fieldsSelected(Optional<String> var1, List<String> var2);
}
