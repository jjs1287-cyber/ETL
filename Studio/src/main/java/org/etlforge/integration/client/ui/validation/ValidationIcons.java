package org.etlforge.integration.client.ui.validation;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.data.validation.ValidationMessage;

public final class ValidationIcons {
   private ValidationIcons() {
   }

   public static final class Indicators {
      public static final Icon VALID = CommonIcons.SUCCESS_16;
      public static final Icon INVALID = CommonIcons.ERROR_16;
      public static final Icon WARNING = CommonIcons.WARNING_16;

      public static Icon fromMessage(ValidationMessage var0) {
         switch (var0.status()) {
            case INVALID:
               return INVALID;
            case WARNING:
               return WARNING;
            default:
               return VALID;
         }
      }

      private Indicators() {
      }
   }
}
