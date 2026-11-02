package org.etlforge.integration.mscrm.metadata;

import java.io.Serializable;

final class BasicEntityDetailsBooleans implements Serializable {
   private static final long serialVersionUID = 2040136752466812677L;
   public static final long AUTO_ROUTE_TO_OWNER_QUEUE = 1L;
   public static final long CAN_BE_IN_MANY_TO_MANY = 2L;
   public static final long CAN_BE_PRIMARY_ENTITY_IN_RELATIONSHIP = 4L;
   public static final long CAN_BE_RELATED_ENTITY_IN_RELATIONSHIP = 8L;
   public static final long CAN_CREATE_ATTRIBUTES = 16L;
   public static final long CAN_CREATE_CHARTS = 32L;
   public static final long CAN_CREATE_FORMS = 64L;
   public static final long CAN_CREATE_VIEWS = 128L;
   public static final long CAN_MODIFY_ADDITIONAL_SETTINGS = 256L;
   public static final long CAN_TRIGGER_WORKFLOW = 512L;
   public static final long IS_ACTIVITY = 1024L;
   public static final long IS_ACTIVITY_PARTY = 2048L;
   public static final long IS_AUDIT_ENABLED = 4096L;
   public static final long IS_AVAILABLE_OFFLINE = 8192L;
   public static final long IS_CHILD_ENTITY = 16384L;
   public static final long IS_CUSTOM_ENTITY = 32768L;
   public static final long IS_CUSTOMIZABLE = 65536L;
   public static final long IS_DOCUMENT_MANAGEMENT_ENABLED = 131072L;
   public static final long IS_DUPLICATE_DETECTION_ENABLED = 262144L;
   public static final long IS_ENABLED_FOR_CHARTS = 524288L;
   public static final long IS_IMPORTABLE = 1048576L;
   public static final long IS_INTERSECT = 2097152L;
   public static final long IS_MAIL_MERGE_ENABLED = 4194304L;
   public static final long IS_MANAGED = 8388608L;
   public static final long IS_MAPPABLE = 16777216L;
   public static final long IS_READING_PANE_ENABLED = 33554432L;
   public static final long IS_RENAMEABLE = 67108864L;
   public static final long IS_VALID_FOR_ADVANCED_FIND = 134217728L;
   public static final long IS_VALID_FOR_QUEUE = 268435456L;
   public static final long IS_VISIBLE_IN_MOBILE = 536870912L;

   private BasicEntityDetailsBooleans() {
   }
}
