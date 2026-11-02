# ETLForge Studio screen-opening runbook

Use this after a Studio build, an endpoint/UI change, or before releasing an installer. Opening a screen and successfully executing its feature are separate checks. Record both separately.

## Preparation and evidence

1. Save and close the normal Studio session. Back up the local Test Operations project before editing it.
2. Start the installed ETLForge shortcut, open **Test Operations**, and leave the application disconnected for the first pass.
3. Open **Help > Application Logs**. Record the initial error count and the build version. Start a results sheet with: screen, connected/disconnected, action, PASS/FAIL, error text, screenshot, build, time.
4. A screen passes only if it opens without a new exception, its controls and labels are readable, every tab opens, resizing/minimizing/restoring works, and Cancel/Close returns to the main window. An empty data table is acceptable when there is no matching data; an exception or missing editor is not.
5. Use disposable objects prefixed `UI CHECK`. Cancel dialogs after testing. Do not delete retained logs, keys, users, projects, or operation history during a screen-opening pass.
6. Connect to **localhost:47100** using the existing test account for the connected pass. The Studio gateway uses this one HTTP port.

## Startup, project selection, and File

| Check | Clicks and expected behavior |
|---|---|
| Startup and recent projects | Launch; inspect Recent Projects, project names, locations and buttons. Open Test Operations. |
| New project | File > New Project; enter a disposable name, browse a folder, then Cancel. |
| Open/Browse/Search | File > Open; open each selection/browse/search dialog and Cancel. Reopen the existing project. |
| Download project | Open project > Download Project; inspect the server selection/project tree and Cancel. **Known server limitation:** the legacy full server-project download method is currently unimplemented; this is not a passing end-to-end download test. |
| Source-control project | Open project > source-control option; inspect repository URL, branch and local folder fields. Cancel before cloning. |
| Import Backup | File > Import Backup; browse a `.jpk`, inspect the import choices, Cancel. |
| Export Backup | File > Export Backup; inspect file selection and options, Cancel. |
| Migrate | File > Migrate; select another disposable local project, inspect the preview, Cancel. A separate execution test must verify preservation of existing sources, targets and Project Variables. |
| Save and close | Open a disposable object, save it, close the editor, reopen it, then close/reopen the project. No load/serialization exception. |

## Design editors

Open a new object and an existing object for every row. Select every tab/subpage, open its property dialogs, then close without saving changes to the real fixtures.

| Object | Required subchecks |
|---|---|
| Operation | New selector has no obsolete SELECT TYPE/instruction/Do not show again controls. Open Transformation, Script, Archive, Web Service Call, Hosted Web Service and XSLT operation types. Open Operation Options for each. Open its source, target, mapping and script from the graph. |
| Script | New default includes trans/javascript examples and the speed note. Open Functions, Project Items, Data Elements, Plugins and Test tabs. Expand every function category, filter, clear filter, and open a function's details. Confirm Project Items loads without the old null-pointer error. |
| Transformation | Open source/target trees; mapping editor, function tabs, source-data load and Test panels. Check SOURCE > TARGET text is readable. Right-click scalar, repeated and attribute nodes: inspect cardinality/type choices; Cancel. Execute a node/XSD refresh test only on a copied transformation. |
| XSLT Transform | Open an existing stylesheet and a new one; inspect editor and validation. Open an operation using it. |
| Project Variable | Name, one-line Value, Is password, four-line Description. On a disposable variable check masking and that unchecking clears the value. |
| File Format | Open text/delimited and fixed-width definitions; field grid, qualifiers, delimiters and validation dialogs. |
| Schedule | Open all recurrence choices, start/end/date/time controls, exclusions and enabled state. Cancel; do not attach an enabled schedule to the full regression runner. |
| Email Message | Open addressing, subject, body, attachments and available options. No real email is sent during this pass. |
| Web Service Method | Open WSDL browse/import, method selection, request and response structures, authentication and HTTP options. Cancel network imports unless using the local Echo fixture. |
| Hosted HTTP Endpoint | Open URL, verbs, request/response settings and triggered-operation choices. |
| Salesforce | Open Org OAuth editor, Query, Insert/Create, Update, Upsert, Delete and Bulk operation editors that are present in the tree. Expand their object/mapping pages. Screen loading never authorizes a Salesforce write. |

## Every Source and Target type

For **both** New Source and New Target, open each type below, switch to a different type and back, close, and reopen a saved disposable object. Also check the type chooser contains every entry.

| Type | Subscreens to open |
|---|---|
| Database (ODBC/JDBC) | Driver chooser/cache, connection fields, password dialog, SQL/table selection and advanced properties. JDBC parsing must not report a null stream. |
| File Share (Windows SMB) | UNC path, credentials, file filter and options. |
| FTP/SFTP/FTPS | Select FTP, SFTP and available TLS choices; inspect host/port, password and SSH key selection/fingerprint controls. SFTP is port 2022 on this test machine. |
| HTTP/HTTPS | URL, verb, credentials, Options and **Advanced Properties**. All checkbox labels, the authentication dropdown, OK and Cancel must remain readable in the dark theme. |
| LDAP | Connection, credentials, search scope/filter and available advanced pages. |
| Local File on Server | Folder/file selection, patterns, naming and after-success handling. |
| Temporary Storage | Open its full editor, including compression. It must not throw the former EndpointCompressionPanel exception. |
| Global Variable | Variable name, validation and Test Connection. |
| Amazon S3 | Bucket, region, authentication selector, access key, masked secret, temporary-session token, prefix, recursion/overwrite, and file options. |

For every applicable file endpoint, expand **Compression** and **PGP**. Toggle enable/disable, inspect password masking, format and combined/individual choices, then Cancel. Source format auto-detection and target format choices must not be confused. Test ZIP, GZIP, TAR.GZ, 7z and PGP execution using regression operations, not merely by opening these controls.

### Disconnected behavior

Click Test Connection while disconnected, including on Amazon S3. Expect only **Not connected to a server.** There must be no stack trace, Details button or audit-history wording. Reconnect and repeat the load checks. Real connection errors while connected should identify the relevant endpoint.

## Administration

Open each console one at a time, select every tab and then close it. Refresh only read-only data during this pass.

| Console | Checks |
|---|---|
| System Console | All status tabs and refresh. No stop/restart action. |
| System Queue | Refresh, filter, tree/flat modes; no cancellation of unrelated work. |
| System Operation Log | Refresh/filter, select success, warning and failed instances; messages/details populate. Open a message dialog. |
| System Operation History | Refresh/filter, select instances and source/target file rows. No invalid UTF-8 sequence exception. Open a retained test file if available; no deletion. |
| Scheduler Console | Schedule Engine, Deployed Schedules and Scheduled To Run; Status and refresh. No functionality-unavailable fault. Leave start/stop buttons untouched. |
| User Console | User list, new/edit dialog and permissions pages; Cancel. |
| Project Console | Projects, permissions and available details; Cancel changes. |
| Server Proxy Settings | Load existing values and inspect all fields. No SOAP-method-not-allowed fault. Do not Save during this pass. |
| Server Logs | List files, change log category, open/read a log and return. Check UTF-8 rendering. |
| Audit Log | Defaults detail 30/header 365; project/type/date filters, blank object name, paging, selected-entry previous/current values, splitter resizing. Blank name means all matching objects in the selected project. Do not change retention. |
| Cloud logging | Reconnect/reload; inspect SFTP, HTTP and SMB destination forms. Masked secrets remain masked. Save/Reload states agree with connection/permission state. Do not change the live destination. |
| Multi-server configuration | Open node list/configuration and refresh; verify this laptop appears. Do not join nodes or change scheduler leadership during this pass. This does not certify multi-host failover. |
| SSH/PGP Keys | List, type selection, new/import/edit dialogs and key metadata. Cancel; never capture private key material in screenshots. |
| Project Differences | Icon present; select two local test projects, compare, select changed objects and inspect both values. Cancel/close. A separate comparison test must prove layout-only changes are ignored. |

## Menus, dialogs, preferences, appearance

1. Open each top-level menu: File, Edit, View, Actions, Source Control, Transformation, Windows and Help. Open each submenu. Every retained command must be readable and every dialog-producing command should open and cancel cleanly. Commands unavailable in the current selection should be disabled appropriately.
2. Under Source Control, open status/diff, commit-comment and repository dialogs that are available. Cancel any commit, push, pull or branch mutation during screen testing. A separate Git test uses a disposable repository and verifies commit-and-push before deployment.
3. Open Search/Replace, entity/folder rename, copy, move, new-folder, dependency/usage, validation, deploy preview and operation-run dialogs from the applicable tree/graph context menu. Cancel mutations and deployments during this pass.
4. In Preferences open every installed page. Baseline pages: General; General > Error Log; General > UI; Administration; Deploy; Operations; Operations > Graph; Project; Project > Backup Policy; Project > Import & Restore; Project > UI; Salesforce Wizards; Schedules; Scripts; Sources & Targets; Transformations; Web Service Methods; Wizards. If a new page appears, add it to this runbook and test it. Cancel without applying preference changes.
5. Help should retain Application Logs only. Confirm the obsolete About page is absent.
6. Check black/dark-gray surfaces, white text, light-gray borders/scrollbars, white minimize/maximize/close glyphs, readable disabled controls, selection colors, tooltips, password dialogs and modal shading. Resize each editor and verify scrollbars reach all controls; test a smaller display/window size too.
7. Close every test editor, save only deliberate disposable changes, exit normally, reopen Studio and the project, and inspect Application Logs again.

## Automated companion

`C:\ETL\Server\tests\integration\StudioScreenLoadingTest.java` opens the registered entity editors, source/target types, operation types/options, admin consoles, preferences and selected modal dialogs in an isolated workspace. It records screenshots and checks the application error counter.

Evidence directory: `C:\ETL\Reports\studio-screen-loading` (`results.txt`, `summary.txt`, PNGs). It does not click every control or prove every external integration; use the manual checklist above as well. The test process must close only its own windows. Keep the regular Studio closed when running this companion.

## Failure and release decision

Stop the affected screen's test on any new exception. Record the exact click path, object ID/type, connected state, error/stack trace and screenshot. Close the dialog normally; do not repeatedly press a failing destructive command. After a fix, repeat that screen and nearby shared panels, then reopen the project once more. A release is not certified by a green operation suite alone: the screen runbook, connector setup and explicitly manual/service checks must also pass.
