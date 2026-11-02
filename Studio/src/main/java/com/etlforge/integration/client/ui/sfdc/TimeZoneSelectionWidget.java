package com.etlforge.integration.client.ui.sfdc;

import java.awt.event.FocusListener;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;

interface TimeZoneSelectionWidget extends UiProvider {
   void setSelectedTimeZoneId(TimeZoneId var1);

   TimeZoneId getSelectedTimeZoneId();

   void addFocusListener(FocusListener var1);

   void setEnabled(boolean var1);

   void registerWithParent(InputPanel var1);
}
