package org.etlforge.integration.client.ui.interchange.location.panel;

import java.util.EnumMap;
import org.etlforge.integration.client.ui.interchange.location.DataLocationFactory;
import org.etlforge.integration.client.ui.interchange.location.db.DatabaseLocationPanelFactory;
import org.etlforge.integration.client.ui.interchange.location.fileshare.FileShareLocationPanelFactory;
import org.etlforge.integration.client.ui.interchange.location.ftp.FtpLocationPanelFactory;
import org.etlforge.integration.client.ui.interchange.location.http.HttpLocationPanelFactory;
import org.etlforge.integration.client.ui.interchange.location.ldap.LdapLocationPanelFactory;
import org.etlforge.integration.client.ui.interchange.location.local.LocalFileLocationPanelFactory;
import org.etlforge.integration.client.ui.interchange.location.tempstore.TemporaryStorageLocationPanelFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.location.TemporaryStorageLocation;

public class DataLocationPanelFactory {
   private final IntegrationEntity locatable;
   private final SourceTarget type;
   private final EnumMap<DataLocationType, DataLocationPanel> panels;

   public DataLocationPanelFactory(IntegrationEntity var1, SourceTarget var2) {
      this.locatable = var1;
      this.type = var2;
      this.panels = new EnumMap<>(DataLocationType.class);
   }

   public DataLocationPanel getPanel(DataLocation var1) {
      DataLocationPanel var2 = this.panels.get(var1.getDataLocationType());
      if (var2 == null) {
         var2 = this.createNewPanel(var1);
      }

      return var2;
   }

   public DataLocationPanel getPanel(DataLocationType var1) {
      DataLocationPanel var2 = this.panels.get(var1);
      if (var2 == null) {
         DataLocation var3 = DataLocationFactory.newLocation(var1);
         var2 = this.getPanel(var3);
      }

      return var2;
   }

   protected void configurePanel(DataLocationPanel var1) {
   }

   private DataLocationPanel createNewPanel(DataLocation var1) {
      DataLocationPanelFactory.PanelCreator var2 = new DataLocationPanelFactory.PanelCreator(this.locatable, this.type);
      DataLocationPanel var3 = var2.create(var1);
      this.configurePanel(var3);
      this.panels.put(var1.getDataLocationType(), var3);
      return var3;
   }

   private static final class PanelCreator {
      private final IntegrationEntity locatable;
      private final SourceTarget type;

      public PanelCreator(IntegrationEntity var1, SourceTarget var2) {
         this.locatable = var1;
         this.type = var2;
      }

      public DataLocationPanel create(DataLocation var1) {
         switch (var1.getDataLocationType()) {
            case AmazonS3:
               return new S3LocationPanel((org.etlforge.integration.data.location.S3Location)var1, this.type);
            case GlobalVariable:
               return new GlobalVariableLocationPanel((org.etlforge.integration.data.location.GlobalVariableLocation)var1, this.type);
            case Database:
               return DatabaseLocationPanelFactory.newPanel(this.locatable, (DatabaseLocation)var1, this.type);
            case FileShare:
               return FileShareLocationPanelFactory.newPanel((FileShareLocation)var1, this.type);
            case TemporaryStorage:
               return TemporaryStorageLocationPanelFactory.newPanel((TemporaryStorageLocation)var1, this.type);
            case LocalFile:
               return LocalFileLocationPanelFactory.newPanel((LocalFileLocation)var1, this.type);
            case FTP:
               return FtpLocationPanelFactory.newPanel((FtpLocation)var1, this.type);
            case HTTP:
               return HttpLocationPanelFactory.newPanel((HttpLocation)var1, this.type);
            case LDAP:
               return LdapLocationPanelFactory.newPanel((LdapLocation)var1, this.type);
            default:
               throw new RuntimeException("Unexpected location type: " + var1.getDataLocationType());
         }
      }
   }
}
