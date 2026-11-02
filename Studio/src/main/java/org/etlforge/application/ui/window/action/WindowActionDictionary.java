package org.etlforge.application.ui.window.action;

import java.text.MessageFormat;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.ResourceBundleIconProvider;
import org.etlforge.ui.ResourceBundleStringResources;

public final class WindowActionDictionary {
   private static final String STRINGS = "org.etlforge.application.ui.window.action.resources.Strings";
   private static final String ICONS = "org.etlforge.application.ui.window.action.resources.Icons";
   public static final ActionDictionary DICTIONARY = new WindowActionDictionary.DictionaryImpl();

   private WindowActionDictionary() {
   }

   private static final class DictionaryImpl extends NewResourceBasedActionDictionary {
      public DictionaryImpl() {
         super(
            new ResourceBundleStringResources("org.etlforge.application.ui.window.action.resources.Strings", ApplicationResources.MNEMONIC_FACTORY),
            new ResourceBundleIconProvider("org.etlforge.application.ui.window.action.resources.Icons"),
            null
         );
      }

      @Override
      public MnemonicString getName(String var1) {
         MnemonicString var2 = super.getName(var1);
         if ("action.About".equals(var1)) {
            MnemonicStringFactory var3 = ApplicationResources.MNEMONIC_FACTORY;
            String var4 = ApplicationConfiguration.getShortProductName();
            String var5 = var3.escape(var4);
            String var6 = var2.getText();
            String var7 = MessageFormat.format(var6, var5);
            return var3.fromString(var7);
         } else {
            return var2;
         }
      }
   }
}
