import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.etlforge.integration.client.server.*;
import org.etlforge.integration.client.server.login.*;
import org.etlforge.integration.client.server.webservice.*;
import org.etlforge.integration.user.User;
import com.etlforge.integration.server.implementation.webservice.login.impl.EnterpriseAuthenticateWsImpl;

/** Runs the same authentication wrapper, version check and callbacks as the Studio UI. */
public final class StudioLicenseFreeLoginSmoke {
 public static void main(String[] args) throws Exception {
  System.clearProperty("konga.skip_license_check");
  if(!"42.0.0.0".equals(org.etlforge.integration.client.info.ClientInfo.getCurrentVersion().toString())) throw new AssertionError("Unexpected Studio product version");
  ServerPorts ports = new ClientServerPorts();
  if (ports.getDefaultPort() != 47100) throw new AssertionError("Wrong default native API port");
  ServerManager manager = new ServerManager(ServerInformationStore.EMPTY, ports, null);
  CppWebServiceConfiguration config = new CppWebServiceConfiguration(manager, ports, callback -> true);
  manager.setLoginApi(new EnterpriseAuthenticateWsImpl(config, manager));
  String password = new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")), StandardCharsets.UTF_8).trim();
  boolean[] authenticated = {false};
  manager.getLoginService().addCallback(new LoginCallback.Adapter() {
   public void loginSucceeded(ServerInfo server, LoginCredentials credentials, User user) { authenticated[0] = true; }
   public void invalidCredentials(InvalidCredentialsException error) { throw new AssertionError("Valid password rejected"); }
   public void caught(Throwable error) { throw new AssertionError("Studio login failed", error); }
   public void cancelled() { throw new AssertionError("Studio login cancelled"); }
  });
  manager.getLoginService().login(new LoginCredentials(manager.getAddressFactory(), "127.0.0.1", "Admin", password, false, false));
  if (!authenticated[0] || manager.getConnectionStatus() != ConnectionStatus.CONNECTED) throw new AssertionError("Studio did not become connected");
  manager.getLoginService().logout();
  boolean[] rejected = {false};
  manager.getLoginService().addCallback(new LoginCallback.Adapter() {
   public void invalidCredentials(InvalidCredentialsException error) { rejected[0] = true; }
   public void loginSucceeded(ServerInfo server, LoginCredentials credentials, User user) { throw new AssertionError("Incorrect password accepted"); }
   public void caught(Throwable error) { throw new AssertionError("Unexpected authentication error", error); }
  });
  manager.getLoginService().login(new LoginCredentials(manager.getAddressFactory(), "127.0.0.1", "Admin", password + "-incorrect", false, false));
  if (!rejected[0]) throw new AssertionError("Incorrect password was not rejected");
  System.out.println("PASS: complete Studio login and version check without license metadata or bypass flags; incorrect password remains rejected");
 }
}
