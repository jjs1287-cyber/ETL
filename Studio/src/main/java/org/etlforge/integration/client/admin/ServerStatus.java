package org.etlforge.integration.client.admin;

public enum ServerStatus {
   RUNNING(2, "Running"),
   STOPPED(1, "Stopped"),
   STARTING(512, "Starting"),
   STOPPING(4, "Stopping"),
   SINGLE_THREAD(16, "SingleThread"),
   RESOURCE_DISTRIBUTION(32, "ResourceDistribution"),
   RESOURCE_DISTRIBUTION_ERROR(64, "ResourceDistributionError"),
   DISABLED(128, "Disabled"),
   UNDEFINED(0, "Undefined"),
   UNREACHABLE(256, "Unreachable"),
   ERROR(8, "Error");

   private static final String RESOURCE_PREFIX = "ServerStatus.";
   private static final String SHORT_SUFFIX = ".Short";
   private static final String DESCRIPTION_SUFFIX = ".Description";
   private final int code;
   private final String shortString;
   private final String description;

   ServerStatus(int var3, String var4) {
      this.code = var3;
      this.shortString = AdminMessages.getString("ServerStatus." + var4 + ".Short");
      this.description = AdminMessages.getString("ServerStatus." + var4 + ".Description");
   }

   @Override
   public String toString() {
      return this.shortString;
   }

   public String getDescription() {
      return this.description;
   }

   public int getCode() {
      return this.code;
   }
}
