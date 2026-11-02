# Project Differences

Installed 2026-10-29. Administration → Project Differences allows selecting two saved local Studio .jip projects, including GitHub working copies. Server-deployed project selection is not implemented; deployed projects can first be downloaded to local projects for comparison.

The comparison reads temporary project snapshots through Studio's existing project loader. Original projects are not edited, deployed or migrated. Unsaved editor changes are not included; save first when they should participate.

Objects match by type and folder/name path. Different project/object UUIDs are normalized to their logical references. A renamed or moved object appears as left-only/right-only. Definition fields are displayed in a differences grid; select an object to see changed fields and a field to inspect full left/right values below.

Excluded: GraphLayout.xml resources, graph-layout nodes, deployment flags, transient state and creation/modification timestamps. Pipeline execution ordering and mapping logic remain significant; only visual positioning is ignored. Same values with different object identities, creation dates or deployment flags compare equally. Business text and script whitespace remain significant except CRLF/LF normalization in persisted text.

Passwords, passphrases, tokens, secrets and private-key properties are masked in the UI. A password project variable is masked. Protected values are compared using SHA-256 digests held in the comparison snapshot, not displayed in the UI. This is a change detector, not an exported audit log or a general scanner for credentials embedded in script literals.

Entity resources and project filestore assets are compared by streaming SHA-256. Binary/external resource differences show hashes rather than a textual edit diff. Symbolic links/reparse entries in compared resource trees are rejected. Folder structure affects logical object paths; visual diagram positions do not.

Java implementation exception: the Swing UI and the comparison consume Studio's existing Java project/entity persistence model. Reimplementing the binary project loader and object serializers in C++ would duplicate that model. No Tomcat call is involved.

Implementation: org.etlforge.integration.client.project.compare.ProjectComparison and ui.admin.compare.

Verification: ProjectComparisonTest (12 checks) and ProjectComparisonStudioTest (11 checks, including real Compare button, asynchronous snapshot loading, selection details, resource changes and ignored layout). The packaged Studio JARs were used for the final UI test. Screenshot: C:/ETL/Reports/pgp-compare/project-differences-loaded.png.
