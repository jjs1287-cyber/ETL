package org.etlforge.integration.client.ui.interchange.locatable;

import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.location.TemporaryStorageLocation;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class DataLocationToolTipEntries {
   public static void addTo(IntegrationEntityToolTip var0, DataLocation var1, SourceTarget var2) {
      DataLocationType var3 = var1.getDataLocationType();
      var0.addEntry("Type:", var3);
      if (var1 instanceof LocalFileLocation) {
         addLocalFileEntries(var0, var1);
      } else if (var1 instanceof FileLocation) {
         addFileLocationEntries(var0, (FileLocation)var1, var2);
      } else if (var1 instanceof DatabaseLocation) {
         addDbLocationEntries(var0, (DatabaseLocation)var1);
      } else if (var1 instanceof HttpLocation) {
         addHttpLocationEntries(var0, (HttpLocation)var1);
      } else if (var1 instanceof LdapLocation) {
         addLdapLocationEntries(var0, (LdapLocation)var1);
      }
   }

   private static void addLocalFileEntries(IntegrationEntityToolTip var0, DataLocation var1) {
      var0.addEntry("File:", ((LocalFileLocation)var1).getFullPath());
   }

   private static void addFileLocationEntries(IntegrationEntityToolTip var0, FileLocation var1, SourceTarget var2) {
      if (var1 instanceof FtpLocation) {
         FtpLocation var3 = (FtpLocation)var1;
         var0.addEntry("Host:", var3.getHost());
         var0.addEntry("Path:", var3.getHostPath());
      } else if (var1 instanceof FileShareLocation) {
         FileShareLocation var4 = (FileShareLocation)var1;
         var0.addEntry("Path:", var4.getPath());
      } else if (var1 instanceof TemporaryStorageLocation) {
         TemporaryStorageLocation var5 = (TemporaryStorageLocation)var1;
         var0.addEntry("Path:", var5.getPath());
      }

      var0.addEntry(var2 == SourceTarget.Source ? "File filter:" : "Filename:", var1.getFileFilter().getFilter());
   }

   private static void addDbLocationEntries(IntegrationEntityToolTip var0, DatabaseLocation var1) {
      if (!var1.isManualMode()) {
         var0.addEntry("Server:", var1.getServer());
         var0.addEntry("Database:", var1.getDatabase());
      }
   }

   private static void addHttpLocationEntries(IntegrationEntityToolTip var0, HttpLocation var1) {
      var0.addEntry("URL:", var1.getUrl());
   }

   private static void addLdapLocationEntries(IntegrationEntityToolTip var0, LdapLocation var1) {
      var0.addEntry("Server:", var1.getServer());
   }

   private DataLocationToolTipEntries() {
   }
}
