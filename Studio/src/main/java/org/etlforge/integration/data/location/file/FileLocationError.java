package org.etlforge.integration.data.location.file;

import org.etlforge.integration.data.location.DataLocationError;
import org.etlforge.integration.data.validation.ValidationIssue;

public final class FileLocationError {
   public static final ValidationIssue MISSING_PATH = new DataLocationError("FileShareLocationValidator.Path");
   public static final ValidationIssue MISSING_FILTER = new DataLocationError("Validation.FileNameMissing");

   private FileLocationError() {
   }
}
