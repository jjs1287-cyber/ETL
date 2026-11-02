package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import java.awt.event.FocusListener;
import java.util.List;
import org.etlforge.ui.UiProvider;

interface HeaderInput extends UiProvider {
   boolean isValid();

   List<String> getHeaders();

   void addFocusListener(FocusListener var1);
}
