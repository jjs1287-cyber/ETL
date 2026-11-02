package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public final class Icons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.smallIcon("action.ic.TestConnection"), ClientIcons.TEST_CONNECTION_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.TestConnection"), ClientIcons.TEST_CONNECTION_24},
      {ActionDictionaryKeys.largeIcon("action.ic.TestConnection"), ClientIcons.TEST_CONNECTION_32}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
