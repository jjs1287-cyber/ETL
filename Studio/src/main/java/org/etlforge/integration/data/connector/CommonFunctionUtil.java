package org.etlforge.integration.data.connector;

import com.google.common.annotations.VisibleForTesting;

public final class CommonFunctionUtil {
   public static final String MSCRM_TYPE = "mscrm_type";
   public static final String OBJECT_NAME = "object_name";
   public static final String INPUT_XSD_FILENAME = "input_xsd_filename";
   public static final String INPUT_XSD_ROOT = "input_xsd_root";
   public static final String OUTPUT_XSD_FILENAME = "output_xsd_filename";
   public static final String OUTPUT_XSD_ROOT = "output_xsd_root";
   public static final String INPUT_SCHEMA = "input_schema";
   public static final String OUTPUT_SCHEMA = "output_schema";
   public static final String QUERY_XML = "query_xml";
   public static final String MSCRM_UPSERT_KEYS = "mscrmupsert_keys";
   @VisibleForTesting
   static final String MS_CRM_ENDPOINT_ID = "mscrmendpoint_id";
   public static final String MSGP_TYPE = "msgp_type";
   @VisibleForTesting
   static final String MS_GP_ENDPOINT_ID = "msgpendpoint_id";
   public static final String AUTODESK_TYPE = "autodesk_type";
   @VisibleForTesting
   static final String AUTODESK_ENDPOINT_ID = "autodeskendpoint_id";
   public static final String NETSUITE_OPERATION = "netsuite_operation";
   @VisibleForTesting
   static final String NETSUITE_ENDPOINT_ID = "netsuiteendpoint_id";

   private CommonFunctionUtil() {
   }
}
