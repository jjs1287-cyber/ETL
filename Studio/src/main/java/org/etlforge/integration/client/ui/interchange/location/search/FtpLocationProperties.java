package org.etlforge.integration.client.ui.interchange.location.search;

import java.util.List;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueBooleanProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueIntegerProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.ftp.FtpSecurityOption;
import org.etlforge.integration.data.location.ftp.FtpTransferType;

final class FtpLocationProperties {
   public static void collect(SourceTarget var0, List<SearchableProperty> var1) {
      var1.add(stringProperty("Host", "locator"));
      var1.add(stringProperty("Path", "path"));
      FileLocationProperties.collect(var0, var1);
      var1.add(new FtpLocationProperties.TransferTypeProperty());
      var1.add(booleanProperty("Passive mode", "passive_mode", true));
      var1.add(new FtpLocationProperties.SecurityOptionProperty());
      switch (var0) {
         case Source:
            var1.add(booleanProperty("List only file names", "ftp.nlst", false));
            break;
         case Target:
            var1.add(new FtpLocationProperties.FtpRenameProperty());
            var1.add(intProperty("Transfer Timeout", "transfer_timeout", 14400));
      }
   }

   private static SearchableProperty<String> stringProperty(String var0, String var1) {
      return new ReplaceableKeyValueStringProperty(var0, var1, TargetFunctions.TO_LOCATION);
   }

   private static SearchableProperty<Boolean> booleanProperty(String var0, String var1, Boolean var2) {
      return new ReplaceableKeyValueBooleanProperty(var0, var1, var2, TargetFunctions.TO_LOCATION);
   }

   private static final SearchableProperty<Integer> intProperty(String var0, String var1, int var2) {
      return new ReplaceableKeyValueIntegerProperty(var0, var1, var2, TargetFunctions.TO_LOCATION);
   }

   private FtpLocationProperties() {
   }

   private static final class FtpRenameProperty implements ReplaceableProperty<Boolean> {
      private FtpRenameProperty() {
      }

      @Override
      public String getName() {
         return "Use FTP Rename";
      }

      @Override
      public Class<Boolean> getValueType() {
         return Boolean.class;
      }

      public Boolean getValue(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         return ((FtpLocation)var2).isFtpRenameEnabled();
      }

      public void setValue(IntegrationEntity var1, Boolean var2) {
         DataLocation var3 = ((Locatable)var1).getLocation();
         ((FtpLocation)var3).setFtpRenameEnabled(var2);
      }
   }

   private static final class SecurityOptionProperty implements ReplaceableProperty<FtpSecurityOption> {
      private SecurityOptionProperty() {
      }

      @Override
      public String getName() {
         return "Security option";
      }

      @Override
      public Class<FtpSecurityOption> getValueType() {
         return FtpSecurityOption.class;
      }

      public FtpSecurityOption getValue(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         return ((FtpLocation)var2).getSecurityOption();
      }

      public void setValue(IntegrationEntity var1, FtpSecurityOption var2) {
         DataLocation var3 = ((Locatable)var1).getLocation();
         ((FtpLocation)var3).setSecurityOption(var2);
      }
   }

   private static final class TransferTypeProperty implements ReplaceableProperty<FtpTransferType> {
      private TransferTypeProperty() {
      }

      @Override
      public String getName() {
         return "Transfer type";
      }

      @Override
      public Class<FtpTransferType> getValueType() {
         return FtpTransferType.class;
      }

      public FtpTransferType getValue(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         return ((FtpLocation)var2).getTransferType();
      }

      public void setValue(IntegrationEntity var1, FtpTransferType var2) {
         DataLocation var3 = ((Locatable)var1).getLocation();
         ((FtpLocation)var3).setTransferType(var2);
      }
   }
}
