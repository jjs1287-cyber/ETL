# Studio single-port gateway

Installed and verified 2026-10-30. Studio now uses the same selected host/port for native and Java APIs. No database migration or JDBC/Salesforce implementation change was required.

## Routing

| Caller URL | Destination |
|---|---|
| http://127.0.0.1:47100/axis/konga_authenticate and other native /axis/konga_* services | Existing native Apache/Axis service |
| http://127.0.0.1:47100/axis/services/<allowed-service> | Apache HTTP reverse proxy → http://127.0.0.1:47112/axis/services/<allowed-service> |

47100 is the only Studio-facing HTTP port. 47112 still exists as an internal Java listener for the gateway and native server components; it must not be exposed for Studio. PostgreSQL 6543, PgBouncer 5432 and service control ports remain separate internal/server infrastructure ports.

Both HTTP listeners still bind to 127.0.0.1. No firewall rule, remote listener, SSH service or HTTPS certificate was installed by this change. Existing local-only access rules remain active.

The Java gateway accepts exactly the 12 deployed ETLForge konga_* services listed in the template. Axis administration, service listing, Version, unknown services, extra path segments and Tomcat Manager are not forwarded. This is a reverse proxy to a fixed loopback destination; ProxyRequests is Off. Existing application credential validation remains in place.

PreserveHost and reverse response-header mapping keep generated SOAP addresses on the gateway. Direct and proxied WSDL contracts were compared after normalizing the expected origin difference. Request bodies are not transcoded. UTF-8, chunked requests and session identity were verified. The proxy timeout is 600 seconds, with a 5-second backend connection timeout; existing frontend request-size/access limits remain in force. This does not introduce response caching or reduce the number of application-level Java calls.

## Studio changes

- JavaWebServiceConfiguration retains the address selected at login instead of changing its port to 47112.
- The native and Java service configurations use the same ws.CppPorts provider.
- Separate TOMCAT_PORT_NON_SSL/TOMCAT_PORT_SSL Studio settings and the unused JavaWebServicePorts class were removed.
- The public replaceJavaPort method remains as an identity adapter for an existing legacy caller. It no longer changes an address.
- Java UI/client glue is necessary because Studio is a Swing Java application; no server feature was implemented in Java for this task.

An explicitly entered custom port is preserved for both API families. HTTPS URL construction also uses the selected gateway address; an actual TLS listener/handshake is not part of this installation.

## Configuration and packaging

- Source template: C:\ETL\Server\src\resources\apache\studio-java-proxy.conf.in.
- Instance configuration: C:\ETL\Server\instances\dev\apache\conf\studio-java-proxy.conf, included by httpd.conf.
- Deployment helper: C:\ETL\Tools\Configure-StudioSinglePort.ps1. Parameters: InstanceHome, BackendPort and ServiceName. It fixes the backend host to 127.0.0.1, validates the port, backs up configuration, runs Apache syntax validation, and restarts only the previously running NativeAPI service. Configuration/restart failures restore the prior files.
- Required Apache modules now include mod_proxy.so and mod_proxy_http.so. Keep them and their binary dependencies in the server installer payload.
- Installed Studio JARs: etlforge-client-config.jar, etlforge-integration-client-server.jar and etlforge-integration-client.jar. Updated client.properties removes the private Java port settings.
- Server configuration rollback archive: C:\ETL\Archives\studio-single-port-20261030-153658.
- Studio source/JAR/configuration rollback archive: C:\ETL\Archives\studio-single-port-client-20261030-153658.

The installer must render the template per instance using that instance's backend port, install the proxy modules, and expose only the chosen gateway endpoint. Do not publish the internal backend port in Studio configuration. Revalidate this include when changing the deployed Java service allowlist. Do not automatically forward every newly deployed service.

## Verification

75 checks passed:

- C:\ETL\Reports\studio-single-port-proxy-tests.txt: 58 Apache/Java checks. All deployed Java WSDLs, gateway-advertised addresses, contract equivalence, blocked management/unknown paths, authenticated session creation/cleanup across direct and gateway routes, UTF-8 error parity, chunked POST, bounded unavailable-backend response and fixed-upstream routing. Failure simulation used a separate temporary Apache process, not an outage of the running Tomcat service.
- C:\ETL\Reports\studio-single-port\studio-results.txt: 14 checks using installed Studio JARs. Typed JDBC driver responses match direct Tomcat, invalid credentials remain rejected, real Studio login/driver retrieval works, both API families traverse one arbitrary local TCP forward, legacy address handling preserves custom ports, HTTPS URL construction is correct, and no new client errors remain after waiting for background requests.
- C:\ETL\Reports\studio-single-port-native-regression.txt: 3 existing native API checks for login/version/GUID, project retrieval and processing/scheduler status.
- Initial two typed SOAP checks also appear in C:\ETL\Reports\studio-single-port-studio-tests.txt before the application redirects its console.
- C:\ETL\Reports\studio-single-port\wsdl-timings.json records direct/gateway WSDL timings for inspection; it is not a production throughput benchmark.
- C:\ETL\Reports\studio-single-port-package.txt records the successful installed JAR build.

The forwarding test used a local TCP relay. It did not test a real SSH handshake, SSH account, host-key validation, second computer or remote network. Tests did not make Salesforce external requests or change business JDBC/Salesforce code. Native login and background project listing were exercised by the real Studio fixture.

The legacy native handler returns HTTP 200 with 'Url not available' for one unknown servlet-style path; the test verifies that it does not reach Tomcat administration. Changing that pre-existing generic status behavior is outside this routing change.

## Remote access / SSH follow-up

Recommended first remote-access mode: a supported SSH server on the ETLForge machine, with a key-authenticated account permitted to forward only to 127.0.0.1:47100. Keep the gateway and Tomcat loopback-only. Studio uses its normal server address field; no second Java tunnel or special Tomcat setting is required.

Example for a future configured SSH server:

    ssh -N -o ExitOnForwardFailure=yes -L 127.0.0.1:48100:127.0.0.1:47100 user@server

Studio server address:

    127.0.0.1:48100

For this plain-HTTP gateway inside an SSH tunnel, leave Studio's HTTPS option unchecked. The SSH tunnel encrypts the remote hop.

The local port may be different from 47100. For a nondefault SSH listener, add the configured -p option. The example does not install or configure SSH. Do not assume the local SFTP test service on port 2022 permits TCP forwarding.

Remote-access checklist:

- [x] One HTTP entry point for native and Java APIs.
- [x] Preserve arbitrary forwarded local ports in Studio.
- [x] Retain loopback-only Java and frontend bindings during preparation.
- [ ] Choose/configure supported Windows SSH server and scoped tunnel accounts/keys.
- [ ] Verify server host keys and key rotation; configure firewall access for the SSH listener.
- [ ] Test an actual SSH tunnel from the second physical laptop, including reconnect, interrupted requests and idle timeout behavior.
- [ ] Decide whether Studio should manage tunnel profiles or initially use an external SSH client.
- [ ] Document direct HTTPS deployment separately; do not expose plain HTTP directly to an untrusted network.
- [ ] Include representative simultaneous Studio/native/Java traffic and gateway capacity in the remote-access load tests.

References:
- Apache reverse-proxy configuration: https://httpd.apache.org/docs/2.4/mod/mod_proxy.html
- Apache HTTP proxy transport: https://httpd.apache.org/docs/2.4/mod/mod_proxy_http.html
- OpenSSH local forwarding: https://man.openbsd.org/ssh#L
