package org.etlforge.integration.server.engine.connector.sap;

import com.google.common.base.Strings;
import java.util.List;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public class ConnectorInputValidatorSap {
   public static final String RFC = "RFC";
   public static final String BAPI = "BAPI";
   public static final String IDOC = "IDOC";
   final boolean isIDOC;
   final boolean isRfc;
   final boolean isBapi;
   private final String sapCategory;
   final String functionName;
   final String messageType;
   final String idocType;

   ConnectorInputValidatorSap(List<String> var1) {
      this.sapCategory = var1.size() == 0 ? "" : ((String)var1.get(0)).toUpperCase();
      this.isIDOC = this.sapCategory.equals("IDOC");
      if (this.isIDOC) {
         this.isRfc = this.isBapi = false;
      } else {
         this.isRfc = this.sapCategory.equals("RFC");
         this.isBapi = !this.isRfc && this.sapCategory.equals("BAPI");
      }

      if (this.isIDOC) {
         this.functionName = "";
         this.messageType = var1.size() < 2 ? "" : (String)var1.get(1);
         this.idocType = var1.size() < 3 ? "" : (String)var1.get(2);
      } else {
         this.functionName = var1.size() < 2 ? "" : (var1.size() > 2 ? (String)var1.get(2) : (String)var1.get(1));
         this.messageType = "";
         this.idocType = "";
      }
   }

   void validateIncomplete() {
      this.validateSapCategory();
   }

   void validatecomplete() {
      this.validateSapCategory();
      if (this.isIDOC) {
         this.checkEmpty(this.messageType, "messageType");
         this.checkEmpty(this.idocType, "idocType");
      } else {
         this.checkEmpty(this.functionName, "functionName");
      }
   }

   void validateSapCategory() {
      boolean var1 = this.isIDOC || this.isRfc || this.isBapi;
      if (!var1) {
         throw new ConnectorInvalidInputRuntimeException(this.sapCategory + " is invalid or not supported");
      }
   }

   void checkEmpty(String var1, String var2) {
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorInvalidInputRuntimeException(var2 + " can not be empty");
      }
   }
}
