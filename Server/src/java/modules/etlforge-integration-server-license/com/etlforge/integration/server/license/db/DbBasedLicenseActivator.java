package com.etlforge.integration.server.license.db;

import com.etlforge.integration.server.license.LicenseActivator;
import com.etlforge.integration.server.license.LicenseFolder;
import com.etlforge.integration.server.license.ServerLicenseException;
import com.vs.ezlicrun.EzLicenseExtendedAPI;
import java.sql.Connection;
import java.sql.SQLException;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.db.ServerDbInfo;

public class DbBasedLicenseActivator implements LicenseActivator {
   private Connection tranDb;
   private LicenseKeyDbTable table;

   @Override
   public void activateLicense(String var1) throws ServerLicenseException {
      try {
         this.connectToDb();
         this.deactivateTrialLicenseIfPresent();
         this.writeNewLicenseKeyToDb(var1);
         this.commit();
      } catch (Exception var6) {
         throw new ServerLicenseException("The license could not be activated because an error occurred: " + var6.getMessage(), var6);
      } finally {
         this.closeConnection();
      }
   }

   private void connectToDb() throws SQLException {
      this.tranDb = ServerDbInfo.getTranDbConnectionInfo().getConnection();
      this.table = new LicenseKeyDbTable(this.tranDb);
      this.table.tryLockingTheTable();
   }

   private void deactivateTrialLicenseIfPresent() {
      this.removeTrialCookie();
      this.removeTrialLicenseKey();
   }

   private void removeTrialCookie() {
      try {
         String var1 = this.table.readCookie();
         if (var1 != null) {
            boolean var2 = false;
            EzLicenseExtendedAPI.deactivateLicense(
               var1, "ETLForge Integration Server", "bLFJFMdcYrVqd9NvbuxKV+FAFNtuY9A1", new LicenseFolder().getAbsolutePath(), var2
            );
            this.table.removeCookie();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   private void removeTrialLicenseKey() {
      try {
         this.table.removeTrialLicense();
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private void writeNewLicenseKeyToDb(String var1) throws ServerLicenseException {
      this.table.writeLicenseKey(var1);
   }

   private void commit() throws SQLException {
      this.tranDb.commit();
   }

   private void closeConnection() {
      KongaDbUtils.close(this.tranDb);
      this.tranDb = null;
      this.table = null;
   }
}
