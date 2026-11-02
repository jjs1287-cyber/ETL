package org.etlforge.integration.data.location;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.HashSet;

public enum DataLocationType {
   FTP(1, FtpLocation.class),
   Database(2, DatabaseLocation.class),
   FileShare(3, FileShareLocation.class),
   HTTP(9, HttpLocation.class),
   LDAP(14, LdapLocation.class),
   TemporaryStorage(15, TemporaryStorageLocation.class),
   LocalFile(16, LocalFileLocation.class),
   GlobalVariable(17, GlobalVariableLocation.class),
   AmazonS3(18, S3Location.class),
   None(-99, null);

   private static final ImmutableSet<Integer> RESERVED_IDS = ImmutableSet.of(0, 4, 5, 6, 7, 8, 10, 11, 12, 13);
   private final int id;
   private final Class<? extends DataLocation> locationClass;

   public static DataLocationType fromId(int var0) {
      for (DataLocationType var4 : values()) {
         if (var4.id == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid DataLocationType ID: " + var0);
   }

   DataLocationType(int var3, Class<? extends DataLocation> var4) {
      this.id = var3;
      this.locationClass = var4;
   }

   public int getId() {
      return this.id;
   }

   @Override
   public String toString() {
      switch (this) {
         case Database:
            return "Database (ODBC/JDBC)";
         case HTTP:
            return "HTTP/HTTPS";
         case FTP:
            return "FTP/SFTP/FTPS";
         case LocalFile:
            return "Local File on Server";
         case FileShare:
            return "File Share (Windows SMB)";
         case AmazonS3:
            return "Amazon S3";
         case GlobalVariable:
            return "Global Variable";
         case TemporaryStorage:
            return "Temporary Storage";
         default:
            return super.toString();
      }
   }

   public DataLocation createLocation() {
      if (this == None) {
         throw new UnsupportedOperationException("DataLocationType.None does not have a corresponding DataLocation");
      }

      try {
         return this.getLocationClass().newInstance();
      } catch (Exception var2) {
         throw Throwables.propagate(var2);
      }
   }

   public Class<? extends DataLocation> getLocationClass() {
      return this.locationClass;
   }

   public boolean isFileBased() {
      switch (this) {
         case AmazonS3:
         case GlobalVariable:
         case FTP:
         case LocalFile:
         case FileShare:
         case TemporaryStorage:
         case HTTP:
            return true;
         default:
            return false;
      }
   }

   public static EnumSet<DataLocationType> getFileBasedTypes() {
      EnumSet var0 = EnumSet.noneOf(DataLocationType.class);

      for (DataLocationType var4 : values()) {
         if (var4.isFileBased()) {
            var0.add(var4);
         }
      }

      return var0;
   }

   private static void sanityCheck() {
      HashSet var0 = Sets.newHashSet(RESERVED_IDS);

      for (DataLocationType var4 : values()) {
         if (var0.contains(var4.id)) {
            throw new AssertionError("Duplicate DataLocationType ID: " + var4.id);
         }

         var0.add(var4.id);
      }
   }

   static {
      sanityCheck();
   }
}
