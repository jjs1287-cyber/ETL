package org.etlforge.integration.client.ui.deploy;

import org.etlforge.application.ui.Alert;

public final class ReadOnlyProjectMessage {
   public static void show() {
      Alert.info(
         "This project is marked as read-only. It cannot be deployed. Please contact the person who gave you the jitterpak if you need to deploy changes made to this project.",
         "Read Only Project"
      );
   }

   private ReadOnlyProjectMessage() {
   }
}
