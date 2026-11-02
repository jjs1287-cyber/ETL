package org.etlforge.integration.client.ui.core.window.action.resources;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.integration.client.info.ProductNames;

public final class Strings extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.name("action.core.Login"), "&Login"},
      {ActionDictionaryKeys.tooltip("action.core.Login"), "Login to a ETLForge server"},
      {ActionDictionaryKeys.accelerator("action.core.Login"), "ctrl pressed L"},
      {ActionDictionaryKeys.name("action.core.LoginDebug"), "Login To Debug Server"},
      {ActionDictionaryKeys.tooltip("action.core.LoginDebug"), "Login to a ETLForge server running in the debugger"},
      {ActionDictionaryKeys.name("action.core.Logout"), "L&ogout"},
      {ActionDictionaryKeys.tooltip("action.core.Logout"), "Logout from the current ETLForge server"},
      {ActionDictionaryKeys.name("action.core.KnownServers"), "Known &Servers"},
      {ActionDictionaryKeys.tooltip("action.core.KnownServers"), "Display information about the servers the client has been logged in to"},
      {ActionDictionaryKeys.name("action.core.UpdateUserProfile"), "&Update User Profile"},
      {ActionDictionaryKeys.tooltip("action.core.UpdateUserProfile"), "Update the profile for the logged in user"},
      {ActionDictionaryKeys.name("action.core.Forum"), ProductNames.COMPANY + " Support Forum"},
      {ActionDictionaryKeys.tooltip("action.core.Forum"), "Go to the ETLForge Support Forums on the web."},
      {ActionDictionaryKeys.name("action.core.Tutorial"), ProductNames.COMPANY + " Quick Start Tutorial"},
      {ActionDictionaryKeys.tooltip("action.core.Tutorial"), "Go to the ETLForge Quick Start Tutorial."}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
