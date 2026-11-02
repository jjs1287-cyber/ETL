package com.etlforge.integration.client.ui.sfdc.page;

import org.etlforge.ui.layout.TwoColumnPanel;

public interface OptionsPanelSection {
   String OPERATION_OPTIONS_TITLE = "Operation Options";
   String FILE_OPTIONS_TITLE = "File Options";

   String getTitle();

   void apply();

   void layoutIn(TwoColumnPanel var1);

   void requestFocus();
}
