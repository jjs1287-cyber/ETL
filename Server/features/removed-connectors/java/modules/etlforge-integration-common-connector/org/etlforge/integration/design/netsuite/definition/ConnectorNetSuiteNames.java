package org.etlforge.integration.design.netsuite.definition;

public class ConnectorNetSuiteNames {
   public static final String BaseRefElementName = "baseRef";
   public static final String RecordElementName = "record";
   public static final String RecordResponseElementName = "record";
   public static final String SearchRowResponseElementName = "searchRow";
   public static final String SearchListResponseElementName = "searchRowList";
   public static final String RecordListResponseElementName = "recordList";
   public static final String SearchRecordTypeName = "SearchRecord";
   public static final String SearchRecordBasicTypeName = "SearchRecordBasic";
   public static final String SearchRecordRowTypeName = "SearchRow";
   public static final String SearchRecordRowBasicTypeName = "SearchRowBasic";
   public static final String RecordBaseTypeName = "Record";
   public static final String RecordRefBaseTypeName = "RecordRef";
   public static final String RecordRefBaseTypeNameETLForge = "RecordRef_JB";
   public static final String CustomizationRefTypeName = "CustomizationRef";
   public static final String BaseRefBaseTypeName = "BaseRef";
   public static final String CustomObjectTypeName = "CustomRecord";
   private static final String baseRefElements = "<element name=\"name\" type=\"xsd:string\" minOccurs=\"0\"/>";

   public static String getBaseRefElements() {
      return "<element name=\"name\" type=\"xsd:string\" minOccurs=\"0\"/>";
   }

   public static boolean isRecordElement(String var0) {
      return "record".equals(var0);
   }
}
