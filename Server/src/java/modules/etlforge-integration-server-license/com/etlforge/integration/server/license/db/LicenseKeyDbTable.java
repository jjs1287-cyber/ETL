package com.etlforge.integration.server.license.db;

import com.google.common.base.Preconditions;
import com.etlforge.integration.server.license.LicenseKeyStore;
import com.etlforge.integration.server.license.ServerLicenseException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.axis.utils.StringUtils;
import org.etlforge.integration.server.db.trandb.KeyValuesTab;

public class LicenseKeyDbTable implements LicenseKeyStore {
   private static final String COOKIE_KEY = "LicenseCookie";
   private static final String LICENSE_KEY = "LicenseKey";
   private static final String TRIAL_LICENSE_KEY = "TrialLicenseKey";
   private final KeyValuesTab keyValuesTab;
   private boolean savedAutoCommitState;
   private int savedTransactionIsolation;

   public LicenseKeyDbTable(Connection var1) {
      Preconditions.checkNotNull(var1, "tranDb");
      this.keyValuesTab = new KeyValuesTab(var1);
      this.savedAutoCommitState = true;
   }

   @Override
   public String readLicenseKey() throws ServerLicenseException {
      try {
         String var1 = this.keyValuesTab.getLongValueForKey("LicenseKey");
         if (StringUtils.isEmpty(var1)) {
            var1 = this.keyValuesTab.getLongValueForKey("TrialLicenseKey");
         }

         return var1;
      } catch (SQLException var2) {
         throw new ServerLicenseException(var2);
      }
   }

   @Override
   public void writeLicenseKey(String var1) throws ServerLicenseException {
      try {
         this.keyValuesTab.setLongKeyValue("LicenseKey", var1);
      } catch (SQLException var3) {
         throw new ServerLicenseException(var3);
      }
   }

   @Override
   public String readCookie() throws ServerLicenseException {
      try {
         return this.keyValuesTab.getValueForKey("LicenseCookie");
      } catch (SQLException var2) {
         throw new ServerLicenseException(var2);
      }
   }

   @Override
   public void removeCookie() throws ServerLicenseException {
      this.blankOutField("LicenseCookie");
   }

   public void removeTrialLicense() throws ServerLicenseException {
      this.blankOutField("TrialLicenseKey");
   }

   public void blankOutField(String var1) throws ServerLicenseException {
      try {
         this.keyValuesTab.removeKey(var1);
      } catch (SQLException var3) {
         throw new ServerLicenseException(var3);
      }
   }

   public void tryLockingTheTable() {
      try {
         this.savedAutoCommitState = this.keyValuesTab.getConnection().getAutoCommit();
         this.savedTransactionIsolation = this.keyValuesTab.getConnection().getTransactionIsolation();
         this.keyValuesTab.getConnection().setTransactionIsolation(2);
         this.keyValuesTab.getConnection().setAutoCommit(false);
         this.keyValuesTab.lockTableForUpdate();
      } catch (SQLException var2) {
         var2.printStackTrace();
      }
   }

   public void unlockTheTable() {
      try {
         this.keyValuesTab.getConnection().setAutoCommit(this.savedAutoCommitState);
         this.keyValuesTab.getConnection().setTransactionIsolation(this.savedTransactionIsolation);
      } catch (SQLException var2) {
         var2.printStackTrace();
      }
   }
}
