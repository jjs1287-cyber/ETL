package org.etlforge.integration.client.ui.core.window.action.resources;

import java.util.ListResourceBundle;
import javax.swing.Icon;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.util.ImageLoader;

public class Icons extends ListResourceBundle {
   private static final String ICON_PACKAGE = "org.etlforge.integration.client.ui.core.window.action.resources.icons";
   private static final Object[][] contents = new Object[][]{
      {"Delete.Icon.Small", CommonIcons.DELETE_16},
      {"Delete.Icon.Medium", CommonIcons.DELETE_24},
      {"Delete.Icon.Large", CommonIcons.DELETE_32},
      {ActionDictionaryKeys.smallIcon("action.core.Login"), getIcon("Login16_off.png")},
      {ActionDictionaryKeys.mediumIcon("action.core.Login"), getIcon("Login24_off.png")},
      {ActionDictionaryKeys.largeIcon("action.core.Login"), getIcon("Login32_off.gif")},
      {"action.core.LoginOn.Icon.Small", getIcon("Login16_on.png")},
      {"action.core.LoginOn.Icon.Medium", getIcon("Login24_on.png")},
      {"action.core.LoginOn.Icon.Large", getIcon("Login32_on.gif")},
      {"action.core.LoginOn.Https.Icon.Small", getIcon("Login16_on.png")},
      {"action.core.LoginOn.Https.Icon.Medium", getIcon("Login24_on.png")},
      {"action.core.LoginOn.Https.Icon.Large", getIcon("Login32_on.gif")},
      {ActionDictionaryKeys.smallIcon("action.core.LoginDebug"), getIcon("Login16_off.png")},
      {ActionDictionaryKeys.mediumIcon("action.core.LoginDebug"), getIcon("Login16_off.png")},
      {ActionDictionaryKeys.largeIcon("action.core.LoginDebug"), getIcon("Login32_off.gif")},
      {"action.core.LoginDebugOn.Icon.Small", getIcon("Login16_on.png")},
      {"action.core.LoginDebugOn.Icon.Medium", getIcon("Login24_on.png")},
      {"action.core.LoginDebugOn.Icon.Large", getIcon("Login32_on.gif")},
      {"action.core.LoginDebugOn.Https.Icon.Small", getIcon("Login16_on.png")},
      {"action.core.LoginDebugOn.Https.Icon.Medium", getIcon("Login24_on.png")},
      {"action.core.LoginDebugOn.Https.Icon.Large", getIcon("Login32_on.gif")},
      {ActionDictionaryKeys.smallIcon("action.core.KnownServers"), ClientIcons.SERVER_16},
      {ActionDictionaryKeys.smallIcon("action.core.UpdateUserProfile"), ClientIcons.USER_PROFILE_16},
      {ActionDictionaryKeys.mediumIcon("action.core.UpdateUserProfile"), ClientIcons.USER_PROFILE_24},
      {ActionDictionaryKeys.largeIcon("action.core.UpdateUserProfile"), ClientIcons.USER_PROFILE_32}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }

   private static Icon getIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.client.ui.core.window.action.resources.icons", var0);
   }
}
