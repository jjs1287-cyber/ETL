package org.etlforge.integration.client.ui.interchange.location.search;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.TemporaryStorageLocation;

public final class LocationSearchValuesCollector {
   public static List<String> collect(Locatable var0) {
      DataLocation var1 = var0.getLocation();
      if (var1 != null) {
         switch (var1.getDataLocationType()) {
            case AmazonS3:
               org.etlforge.integration.data.location.S3Location s3=(org.etlforge.integration.data.location.S3Location)var1;
               return Lists.newArrayList(s3.getBucket(),s3.getRegion(),s3.getPrefix(),s3.getFileFilter().getFilter());
            case GlobalVariable:
               return Lists.newArrayList(((org.etlforge.integration.data.location.GlobalVariableLocation)var1).getVariableName());
            case FileShare:
               FileShareLocation var2 = (FileShareLocation)var1;
               return Lists.newArrayList(var2.getLogin(), var2.getPath(), var2.getFileFilter().getFilter(), var2.getRenameFileTo().getFileName());
            case TemporaryStorage:
               TemporaryStorageLocation var3 = (TemporaryStorageLocation)var1;
               return Lists.newArrayList(var3.getPath(), var3.getFileFilter().getFilter(), var3.getRenameFileTo().getFileName());
            case FTP:
               FtpLocation var4 = (FtpLocation)var1;
               return Lists.newArrayList(
                  var4.getLogin(), var4.getHost(), var4.getHostPath(), var4.getFileFilter().getFilter(), var4.getRenameFileTo().getFileName()
               );
            case HTTP:
               HttpLocation var5 = (HttpLocation)var1;
               return Lists.newArrayList(var5.getUrl(), var5.getLogin());
            case LDAP:
               LdapLocation var6 = (LdapLocation)var1;
               return Lists.newArrayList(var6.getLogin(), var6.getBaseName(), var6.getServer());
            case Database:
               DatabaseLocation var7 = (DatabaseLocation)var1;
               return Lists.newArrayList(
                  var7.getDriverName(), var7.getServer(), var7.getDatabase(), var7.getConnectionString(), var7.getManualConnectionString(), var7.getLogin()
               );
         }
      }

      return Lists.newArrayList();
   }

   private LocationSearchValuesCollector() {
   }
}
