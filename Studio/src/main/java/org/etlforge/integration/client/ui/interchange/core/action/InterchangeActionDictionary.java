package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.integration.activity.ui.core.action.ActivityActionDictionary;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.StringResources;

public final class InterchangeActionDictionary {
   private static ResourceLoader loader = new ResourceLoader();
   private static final StringResources stringResources;
   private static final IconProvider iconProvider;
   public static final ActionDictionary DICTIONARY;

   private InterchangeActionDictionary() {
   }

   static {
      stringResources = loader.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.interchange.core.action.resources.Strings");
      iconProvider = loader.loadIconsFromResourceBundle("org.etlforge.integration.client.ui.interchange.core.action.resources.Icons");
      loader = null;
      DICTIONARY = new InterchangeActionDictionary.DictionaryImpl();
   }

   private static class DictionaryImpl implements ActionDictionary {
      private final ActionDictionary[] dictionaries = new ActionDictionary[]{
         new InterchangeActionDictionary.NewEntityActionDictionary(),
         new NewResourceBasedActionDictionary(InterchangeActionDictionary.stringResources, InterchangeActionDictionary.iconProvider, null),
         ActivityActionDictionary.DICTIONARY
      };

      public DictionaryImpl() {
      }

      @Override
      public Icon getLargeIcon(String var1) {
         for (ActionDictionary var5 : this.dictionaries) {
            Icon var6 = var5.getLargeIcon(var1);
            if (var6 != null) {
               return var6;
            }
         }

         return null;
      }

      @Override
      public Icon getMediumIcon(String var1) {
         for (ActionDictionary var5 : this.dictionaries) {
            Icon var6 = var5.getMediumIcon(var1);
            if (var6 != null) {
               return var6;
            }
         }

         return null;
      }

      @Override
      public Icon getSmallIcon(String var1) {
         for (ActionDictionary var5 : this.dictionaries) {
            Icon var6 = var5.getSmallIcon(var1);
            if (var6 != null) {
               return var6;
            }
         }

         return null;
      }

      @Override
      public MnemonicString getName(String var1) {
         for (ActionDictionary var5 : this.dictionaries) {
            MnemonicString var6 = var5.getName(var1);
            if (var6 != null) {
               return var6;
            }
         }

         return null;
      }

      @Override
      public ActionDictionary getParent() {
         return null;
      }

      @Override
      public String getToolTip(String var1) {
         for (ActionDictionary var5 : this.dictionaries) {
            String var6 = var5.getToolTip(var1);
            if (var6 != null) {
               return var6;
            }
         }

         return null;
      }

      @Override
      public KeyStroke getAccelerator(String var1) {
         for (ActionDictionary var5 : this.dictionaries) {
            KeyStroke var6 = var5.getAccelerator(var1);
            if (var6 != null) {
               return var6;
            }
         }

         return null;
      }
   }

   private static class NewEntityActionDictionary implements ActionDictionary {
      private final Map<String, Icon> smallIcons = Maps.newHashMap();
      private final Map<String, Icon> mediumIcons = Maps.newHashMap();
      private final Map<String, Icon> largeIcons = Maps.newHashMap();
      private final Map<String, MnemonicString> names = Maps.newHashMap();

      public NewEntityActionDictionary() {
         for (EntityType var2 : types()) {
            EntityIcons var3 = EntityIcons.forType(var2);
            String var4 = "New." + var2;
            this.names.put(var4, this.createName(var2));
            this.smallIcons.put(var4, var3.getIcon(EntityIcons.Category.VALID, EntityIcons.Size.SMALL));
            this.mediumIcons.put(var4, var3.getIcon(EntityIcons.Category.VALID, EntityIcons.Size.MEDIUM));
            this.largeIcons.put(var4, var3.getIcon(EntityIcons.Category.VALID, EntityIcons.Size.LARGE));
         }
      }

      private MnemonicString createName(EntityType var1) {
         String var2 = var1.getMnemonicString();
         if (ClientInfo.isDataLoader() && var1 != EntityType.SalesforceConnector && var2.startsWith("Salesforce ")) {
            var2 = StringUtils.substringAfter(var2, "Salesforce ");
         }

         return ApplicationResources.MNEMONIC_FACTORY.fromString("New " + var2);
      }

      private static Iterable<EntityType> types() {
         HashSet var0 = Sets.newHashSet(EntityType.values());
         var0.remove(EntityType.RootFolder);
         return var0;
      }

      @Override
      public Icon getLargeIcon(String var1) {
         return this.largeIcons.get(var1);
      }

      @Override
      public Icon getMediumIcon(String var1) {
         return this.mediumIcons.get(var1);
      }

      @Override
      public MnemonicString getName(String var1) {
         return this.names.get(var1);
      }

      @Override
      public ActionDictionary getParent() {
         return null;
      }

      @Override
      public Icon getSmallIcon(String var1) {
         return this.smallIcons.get(var1);
      }

      @Override
      public String getToolTip(String var1) {
         return null;
      }

      @Override
      public KeyStroke getAccelerator(String var1) {
         return null;
      }
   }
}
