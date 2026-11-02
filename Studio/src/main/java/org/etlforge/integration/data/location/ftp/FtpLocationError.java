package org.etlforge.integration.data.location.ftp;

import org.etlforge.integration.data.location.DataLocationError;
import org.etlforge.integration.data.validation.ValidationIssue;

public final class FtpLocationError {
   public static final ValidationIssue MISSING_HOST = new DataLocationError("Validation.HostMissing");

   private FtpLocationError() {
   }
}
