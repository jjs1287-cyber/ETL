package org.etlforge.integration.server.engine.connector.netsuite.ws;

import java.io.File;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassportReq;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteSoapBodyExtractor;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.io.FileUtils;

public class NetSuiteSoapServiceExecution {
   private final NetSuitePassportReq passportReq;
   private final NetSuitePassport passport;
   private final String header;
   private static final String soapSuffix = "_soap";

   public NetSuiteSoapServiceExecution(NetSuitePassport var1, String var2) {
      this.passport = var1;
      this.passportReq = new NetSuitePassportReq(var1);
      this.header = this.passportReq.toXmlString() + var2;
   }

   public NetSuiteSoapServiceExecution(NetSuitePassport var1, boolean var2) {
      this.passport = var1;
      this.passportReq = new NetSuitePassportReq(var1);
      this.header = var2 ? this.passportReq.toXmlString() : null;
   }

   public NetSuiteSoapServiceExecution(NetSuitePassport var1) {
      this(var1, true);
   }

   public HttpCallResponse execute(String var1, String var2, boolean var3, boolean var4) {
      String var5 = NetSuiteSoapEnvelope.getSoapEnvelope(this.header, var2);
      String var6 = NetSuiteExecutionContext.INSTANCE.getDataCenterUrl(this.passport);
      return NetSuiteSoapServiceHandler.execute(this.passport.getProxySettings(), var6, var1, var5, var3, var4);
   }

   public String executeString(String var1, String var2) {
      return this.executeString(var1, var2, false);
   }

   public String executeString(String var1, String var2, boolean var3) {
      return this.executeString(var1, var2, var3, false);
   }

   public String executeString(String var1, String var2, boolean var3, boolean var4) {
      HttpCallResponse var5 = this.execute(var1, var2, var3, var4);
      return var5.getResponse();
   }

   public void executeFile(String var1, String var2, String var3) {
      String var4 = FileUtils.getPostfixFilenameXml(var2, "_soap");
      String var5 = FileUtils.getPostfixFilenameXml(var3, "_soap");
      NetSuiteSoapEnvelope.getSoapEnvelopeFile(this.header, new File(var2), new File(var4));
      this.executeSoapFile(var1, var4, var5);
      NetSuiteSoapBodyExtractor var6 = new NetSuiteSoapBodyExtractor();
      var6.transform(var5, var3);
   }

   private void executeSoapFile(String var1, String var2, String var3) {
      String var4 = NetSuiteExecutionContext.INSTANCE.getDataCenterUrl(this.passport);
      HttpCallResponse var5 = NetSuiteSoapServiceHandler.executeFile(this.passport.getProxySettings(), var4, var1, var2, var3);
   }
}
