package org.etlforge.integration.client.ui.admin;

import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;

final class PackageResources {
   static final String TITLE_APPENDIX = getString("Title.Appendix");

   private PackageResources() {
      throw new UnsupportedOperationException();
   }

   private static String getString(String var0) {
      return AdminViewResources.STRINGS.getString(var0);
   }
}
