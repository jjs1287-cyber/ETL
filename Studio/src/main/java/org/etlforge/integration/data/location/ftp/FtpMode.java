package org.etlforge.integration.data.location.ftp;

public enum FtpMode {
   UNSECURE("Unsecure FTP"),
   EXPLICIT_FTPS("Explicit FTPS"),
   IMPLICIT_FTPS("Implicit FTPS"),
   SFTP("SFTP");

   private final String display;

   FtpMode(String var3) {
      this.display = var3;
   }

   @Override
   public String toString() {
      return this.display;
   }
}
