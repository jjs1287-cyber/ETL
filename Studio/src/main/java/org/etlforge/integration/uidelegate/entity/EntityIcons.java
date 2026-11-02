package org.etlforge.integration.uidelegate.entity;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.etlforge.integration.client.ui.icons.RandomIcon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.ui.Empty;
import org.etlforge.ui.util.ImageLoader;

public final class EntityIcons {
   static final String PACKAGE = "org.etlforge.integration.uidelegate.entity.icons";
   private static ImmutableMap<EntityType, String> NAME_OVERRIDES = loadNameOverrides();
   private static final Map<EntityType, EntityIcons> INSTANCES = Maps.newHashMap();
   private final EntityType type;
   private final Map<IconKey, Icon> icons;
   private static final ImmutableMap<EntityType, Icon> wizardFolderIcons = loadWizardFolderIcons();

   private static ImmutableMap<EntityType, String> loadNameOverrides() {
      Builder var0 = ImmutableMap.builder();
      var0.put(EntityType.ProjectVariable, "Script");
      String var1 = "Autodesk";
      var0.put(EntityType.AutoDeskEndpoint, var1);
      var0.put(EntityType.AutoDeskFunction, var1);
      var0.put(EntityType.AutoDeskCreate, var1);
      var0.put(EntityType.AutoDeskDelete, var1);
      var0.put(EntityType.AutoDeskGet, var1);
      var0.put(EntityType.AutoDeskUpdate, var1);
      var0.put(EntityType.AutoDeskUpsert, var1);
      String var2 = "MSFTAX";
      var0.put(EntityType.MSAXEndpoint, var2);
      var0.put(EntityType.MSAXFunction, var2);
      String var3 = "MSFTCRM";
      var0.put(EntityType.MSCrmEndpoint, var3);
      var0.put(EntityType.MSCrmFunction, var3);
      var0.put(EntityType.MSCrmCreate, var3);
      var0.put(EntityType.MSCrmDelete, var3);
      var0.put(EntityType.MSCrmQuery, var3);
      var0.put(EntityType.MSCrmUpdate, var3);
      var0.put(EntityType.MSCrmUpsert, var3);
      String var4 = "MSFTGP";
      var0.put(EntityType.MSGPEndpoint, var4);
      var0.put(EntityType.MSGPFunction, var4);
      var0.put(EntityType.MSGPCreate, var4);
      var0.put(EntityType.MSGPDelete, var4);
      var0.put(EntityType.MSGPUpdate, var4);
      String var5 = "NetSuite";
      var0.put(EntityType.NetSuiteEndpoint, var5);
      var0.put(EntityType.NetSuiteFunction, var5);
      var0.put(EntityType.NetSuiteCreate, var5);
      var0.put(EntityType.NetSuiteDelete, var5);
      var0.put(EntityType.NetSuiteQuery, var5);
      var0.put(EntityType.NetSuiteUpdate, var5);
      var0.put(EntityType.NetSuiteUpsert, var5);
      String var6 = "Oracle";
      var0.put(EntityType.EBSEndpoint, var6);
      var0.put(EntityType.EBSFunction, var6);
      String var7 = "SAP";
      var0.put(EntityType.SapEndpoint, var7);
      var0.put(EntityType.SapFunction, var7);
      String var8 = "Salesforce";
      var0.put(EntityType.SalesforceConnector, var8);
      var0.put(EntityType.SalesforceBulkLoad, var8);
      var0.put(EntityType.SalesforceCreate, var8);
      var0.put(EntityType.SalesforceDelete, var8);
      var0.put(EntityType.SalesforceQuery, var8);
      var0.put(EntityType.SalesforceUpdate, var8);
      var0.put(EntityType.SalesforceUpsert, var8);
      return var0.build();
   }

   public static Icon loadIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.uidelegate.entity.icons", var0);
   }

   public EntityIcons(EntityType var1) {
      this.type = var1;
      this.icons = Maps.newHashMap();
      this.load();
      synchronized (EntityIcons.class) {
         INSTANCES.put(var1, this);
      }
   }

   public static synchronized EntityIcons forType(EntityType var0) {
      EntityIcons var1 = INSTANCES.get(var0);
      if (var1 == null) {
         var1 = new EntityIcons(var0);
         INSTANCES.put(var0, var1);
      }

      return var1;
   }

   public Icon getDefaultIcon(EntityIcons.Size var1) {
      return this.getIcon(EntityIcons.Category.VALID, var1);
   }

   public Icon getIcon(EntityIcons.Category var1, EntityIcons.Size var2) {
      return this.icons.get(new IconKey(var2, var1));
   }

   public void decorateAction(Action var1) {
      var1.putValue("SmallIcon", this.getDefaultIcon(EntityIcons.Size.SMALL));
      var1.putValue("KongaMediumIconKey", this.getDefaultIcon(EntityIcons.Size.MEDIUM));
      var1.putValue("SwingLargeIconKey", this.getDefaultIcon(EntityIcons.Size.SMALL));
   }

   private void load() {
      for (EntityIcons.Category var4 : EntityIcons.Category.values()) {
         for (EntityIcons.Size var8 : EntityIcons.Size.values()) {
            Icon var9 = this.loadIcon(var4, var8);
            if (var9 == null) {
               if (var4 != EntityIcons.Category.VALID) {
                  var9 = this.loadIcon(EntityIcons.Category.VALID, var8);
               }

               if (var9 == null) {
                  var9 = EntityIcons.Substitutes.get(this.type, var4, var8);
               }
            }

            this.icons.put(new IconKey(var8, var4), var9);
         }
      }
   }

   private Icon loadIcon(EntityIcons.Category var1, EntityIcons.Size var2) {
      String var3 = Objects.firstNonNull(NAME_OVERRIDES.get(this.type), this.type.name());
      String var4 = var3 + this.getCategoryFileNamePart(var1) + this.getSizeFileNamePart(var2) + ".png";
      return ImageLoader.getImageIcon("org.etlforge.integration.uidelegate.entity.icons", var4);
   }

   private String getCategoryFileNamePart(EntityIcons.Category var1) {
      switch (var1) {
         case VALID:
            return "";
         case INVALID:
            return ".Error";
         case WARNING:
            return ".Warning";
         case NEW:
            return "";
         default:
            throw new RuntimeException("This should not happen");
      }
   }

   private String getSizeFileNamePart(EntityIcons.Size var1) {
      switch (var1) {
         case MEDIUM:
            return ".24";
         case LARGE:
            return ".32";
         case SMALL:
            return ".16";
         default:
            throw new RuntimeException("This should not happen");
      }
   }

   private static ImmutableMap<EntityType, Icon> loadWizardFolderIcons() {
      Builder var0 = ImmutableMap.builder();

      loadWizardFolderIconForTypes(
         var0, "ETLForgeConnect.Folder.Db.16.png", EntityType.DatabaseToDatabaseConnect, EntityType.DatabaseToTextConnect, EntityType.DatabaseToXmlConnect
      );
      loadWizardFolderIconForTypes(var0, "ETLForgeConnect.Folder.Text.16.png", EntityType.TextToDatabaseConnect);
      loadWizardFolderIconForTypes(var0, "ETLForgeConnect.Folder.Xml.16.png", EntityType.XmlToDatabaseConnect);
      return var0.build();
   }

   private static void loadWizardFolderIconForTypes(Builder<EntityType, Icon> var0, String var1, EntityType... var2) {
      ImageIcon var3 = ImageLoader.getImageIcon("org.etlforge.integration.uidelegate.entity.icons", var1);

      for (EntityType var7 : var2) {
         var0.put(var7, var3);
      }
   }

   public static Icon getSalesforceWizardFolderIcon() {
      return getWizardFolderIcon(EntityType.SalesforceQuery);
   }

   public static synchronized Icon getWizardFolderIcon(EntityType var0) {
      Preconditions.checkArgument(var0.isWizardType(), "type is not a wizard type");
      Icon var1 = wizardFolderIcons.get(var0);
      if (var1 == null) {
         var1 = forType(EntityType.Folder).getDefaultIcon(EntityIcons.Size.SMALL);
      }

      return var1;
   }

   public static Icon getSalesforceWizardIcon() {
      return forType(EntityType.SalesforceQuery).getDefaultIcon(EntityIcons.Size.SMALL);
   }

   public enum Category {
      VALID,
      INVALID,
      WARNING,
      NEW;

      public static EntityIcons.Category fromValidationStatus(ValidationStatus var0) {
         switch (var0) {
            case VALID:
               return VALID;
            case INVALID:
               return INVALID;
            case WARNING:
               return WARNING;
            default:
               assert false : var0;
               throw new RuntimeException("Unexpected ValidationStatus: " + var0);
         }
      }
   }

   public enum Size {
      SMALL,
      MEDIUM,
      LARGE;

      public Icon emptyIcon() {
         int var1 = this.pixelSize();
         return Empty.icon(var1, var1);
      }

      private int pixelSize() {
         switch (this) {
            case MEDIUM:
               return 24;
            case LARGE:
               return 32;
            case SMALL:
               return 16;
            default:
               throw new RuntimeException("Unexpected size: " + this);
         }
      }
   }

   private static final class Substitutes {
      private static final Icon VALID_FOLDER = EntityIcons.loadIcon("Folder.16.png");
      private static final Icon INVALID_FOLDER = EntityIcons.loadIcon("Folder.Error.16.png");

      public static Icon get(EntityType var0, EntityIcons.Category var1, EntityIcons.Size var2) {
         boolean var3 = var0 == EntityType.Folder || var0 == EntityType.RootFolder;
         if (var3) {
            return var1 == EntityIcons.Category.INVALID ? INVALID_FOLDER : VALID_FOLDER;
         }

         switch (var2) {
            case MEDIUM:
               return RandomIcon.medium();
            case LARGE:
               return RandomIcon.large();
            default:
               return RandomIcon.small();
         }
      }
   }
}
