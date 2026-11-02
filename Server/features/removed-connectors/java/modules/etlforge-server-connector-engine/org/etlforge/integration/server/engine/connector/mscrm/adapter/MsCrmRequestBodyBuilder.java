package org.etlforge.integration.server.engine.connector.mscrm.adapter;

public class MsCrmRequestBodyBuilder {
   static String TestConnectionBody() {
      return "<testconnection/>";
   }

   static String TestConnectionOperation() {
      return "TestConnection";
   }

   static String BasicEntityDetailsOperation() {
      return "BasicEntityDetails";
   }

   static String BasicEntityDetailsBody() {
      String var0 = "basicentitydetails";
      return "<" + var0 + "></" + var0 + ">";
   }

   static String SpecificMetadataOperation() {
      return "SpecificMetadata";
   }

   static String SpecificMetadataBody(String var0) {
      return "<specificmetadata><" + var0 + "/></specificmetadata>";
   }

   static String savedQueryOperation() {
      return "SavedQuery";
   }

   static String savedQueryBody(String var0) {
      return "<savedquery><" + var0 + "/></savedquery>";
   }
}
