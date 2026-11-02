# ETLForge Salesforce OAuth 2.0

Salesforce connections in ETLForge 42 use OAuth 2.0 Client Credentials exclusively. The editor has Instance URL, Client ID and masked Client Secret fields; no username/password/security-token inputs or authentication toggle. Salesforce API61 is used for new connectors.

## Salesforce app setup
Create/configure an External Client App in Salesforce. Enable Client Credentials flow, grant API access, and assign the Run As integration user. That user needs the relevant object and field permissions. Copy the app Client ID and Client Secret using your normal secure process.
Use the org API My Domain URL ending in .my.salesforce.com, including the appropriate sandbox/developer subdomain. ETLForge also accepts the matching root Lightning URL ending in .lightning.force.com and automatically converts its hostname to .my.salesforce.com before requesting a token. Page paths, queries and embedded credentials are rejected. The token client accepts an HTTPS Salesforce host with no path, query, embedded credentials or nonstandard port.

## ETLForge setup
Open the Salesforce Org editor, enter Instance URL, Client ID and Client Secret, save, then select Test Salesforce Login. Connect Studio to the local ETLForge server first. Deploy the connector together with the Salesforce operation. Existing legacy connectors remain readable for migration but must be configured with OAuth; password-based Salesforce login is rejected on both Studio and server paths.
Runtime [variable] substitution is supported for the URL, client ID and client secret, including secrets stored through the existing protected credential codec. Configure corresponding values in the operation/server environment. Missing variables are invalid. A root Lightning UI hostname is normalized to its corresponding API hostname.

## Implementation and dependencies
- Windows x64 native worker acquires a session through the authenticated local Java Salesforce backend before Salesforce Web Service/Bulk operations.
- Java obtains an access token from /services/oauth2/token with grant_type=client_credentials and uses the returned instance URL for SOAP API61 and Bulk sessions.
- No refresh token is used. Invalid sessions are discarded; subsequent reads obtain a new OAuth session. Native operations obtain a session when initialized.
- Force WSC61.0.0 and Partner API61.0.0 replace the old22/24/29 bindings. Jackson core/annotations/databind2.18.12 provide the Bulk dependency closure. Eclipse classpath and webapp assembly script include these libraries.
- Migration C:/ETL/Server/src/database/postgresql/migrations/042-salesforce-oauth.sql adds the four OAuth columns without removing existing data.
- Secrets use the inherited reversible Studio/server credential codec. Treat backups and machine access as sensitive. This work does not introduce a separate key vault or stronger replacement for that inherited codec.
- Diagnostic credential strings are redacted; OAuth WSC tracing disabled; bearer session variables excluded from operation-data persistence. Salesforce SOAP request spool files are redacted after sending, preserving their size for native bookkeeping. Temporary request contents still exist while a request is in progress.

## Verified tests
- Java Studio and server compile; x64 native deployment and worker builds.
- Live token exchange, SOAP getUserInfo, read-only User query and Account metadata.
- Studio/server Axis credential transport and connector deployment.
- Protected connector XML save/reopen; password-free backup preparation omits the client secret and restores the working connector afterward.
- Rejection of wrong secret, redacted errors, three concurrent token exchanges, invalid-session discard and subsequent reacquisition.
- Native-to-Java OAuth acquisition with runtime URL/client ID/encrypted-secret variables using rolled-back fixture rows.
- Queued native Salesforce SOAP query passed on the final installed build. A controlled fault test confirmed retained request-spool session tokens are redacted.
- Account update/upsert with unchanged Name passed on a DUET-configured Account. The user-supplied Account and synthetic insert were rejected by DUET validation: There is no Matching Configuration for the Logical Id. No existing Account deleted; no synthetic Account was created.
- Bulk API query, job completion and CSV result retrieval passed, including a query for the configured Account.

All seven development services are running after restart. PostgreSQL listens on6543; PgBouncer listens on5432. Native localhost47100 login, project listing and process-engine/scheduler status checks passed.

## Scope limits
The original Golden Copy JPK was inspected, not deployed or run. Full business-project migration, full JPK import/export UI roundtrip, live insufficient-permission-user testing and Salesforce insert/delete remain unverified. Current compound address/location metadata parents are omitted from the flat mapper; their scalar component fields remain available.

Official references:
https://developer.salesforce.com/blogs/2023/03/using-the-client-credentials-flow-for-easier-api-authentication
https://developer.salesforce.com/docs/platform/api/guide/api-eol-soap.html
https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.18