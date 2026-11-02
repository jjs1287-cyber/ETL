import com.etlforge.integration.server.license.db.EnterpriseGate;

public final class ServerLicenseFreeSmoke {
 public static void main(String[] args) throws Exception {
  // No database, license key, edition, activation file or licensing JNI is needed.
  new EnterpriseGate(null).ensureServerIsEnterprise();
  if(!"42.0.0.0".equals(org.etlforge.integration.server.version.ServerVersion.getServerVersion().toString()))throw new AssertionError("Unexpected Java backend product version");
  System.out.println("PASS: project-backup edition gate requires no license or licensing database");
 }
}
