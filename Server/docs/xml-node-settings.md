# Transformation XML node settings

Installed 2026-10-30 for ETLForge 42 Windows x64.

Right-click a source or target XML transformation node and choose **XML node settings...**.

- E: exactly one, required (minOccurs=1, maxOccurs=1).
- E?: zero or one, optional (0,1).
- E+: one or more (1,unbounded).
- E*: zero or more (0,unbounded).
- Custom: nonnegative minimum and maximum >= minimum, or unbounded.
- A: scalar attribute; required or optional, never repeating.
- V: parent text content. A scalar element must be its parent's sole child element; existing attributes are preserved. This removes the selected element wrapper.
- EV is Studio's compact display for an element containing a scalar text value. It is not an independent XML schema kind.

The document root remains an element occurring exactly once. Complex elements cannot become attributes/text by discarding their children. Existing V nodes are read-only here; select their containing element to change its occurrences or kind.

## Persistence

Each applied change creates a unique UTF-8 XSD bundle in the project file store and changes only the selected transformation side's schema reference. The original XSD and other users of it remain untouched. Local includes/imports are copied with unique filenames and rewritten dependencies. Named complex types along the selected path are inlined to isolate the branch. The copy is schema-validated, uploaded through the existing native XSD API, and a new native mapping tree is generated before the transformation reference changes. An upload/generation failure keeps the old transformation reference.

Save the transformation/project normally and deploy it to use the change at runtime. Refresh uses the new XSD reference. This does not overwrite the external schema from which the original was imported.

## Limits

Occurrence changes work with existing mappings and preserve their paths. Changing element/attribute/text kind changes mapping paths, so this version requires an unmapped transformation for those conversions. Automatic rewriting of mapping expressions for kind conversions is not implemented.

XSD only; DTD, JSON, database, SOAP/WSDL and Salesforce structures are not changed by this feature. Inherited complex content, referenced groups, redefine, cross-namespace type editing, restricted-type-to-parent-text conversion and ambiguous declarations are rejected with an explanation rather than guessed. XSD includes/imports must have relative schemaLocation files available under the main local schema folder. A missing project XSD should first be downloaded using Edit Structure. Bundles are limited to 64 schemas, each at most 8 MiB. XXE/DOCTYPE and remote dependencies are rejected.

## Implementation exception

This is Java Studio editor/model/persistence functionality: it consumes the existing CROM tree, FileStore, Swing UI and upload client. Duplicating these models through a new C++ bridge would add complexity without improving operation processing. Schema-to-mapping generation continues through the existing C++ API; no new Tomcat operation-row calls or runtime dependencies were introduced.

## Verification

54 checks passed using installed JARs:

- XmlNodeSettingsTest: 37. Four common occurrence ranges plus custom, required attribute, element/attribute conversion, parent text conversion, Unicode, original bytes, shared-type isolation, includes, references, persistence and invalid/security cases.
- XmlNodeSettingsStudioTest: 17. Real Studio login/editor, source/target apply, cancellation, native XSD upload and mapping generation, original/other-side isolation, existing mapping preservation, explicit server refresh, project save/reload, and no new Studio errors.

Evidence:
- C:/ETL/Reports/xml-node-settings-tests.txt
- C:/ETL/Reports/xml-node-settings/studio-results.txt
- C:/ETL/Reports/xml-node-settings/node-settings.png
- C:/ETL/Reports/xml-node-settings-package.txt

An original TreeComponent.java copy is at C:/ETL/Archives/schema-node-editor-original/TreeComponent.java. Test projects are isolated under C:/ETL/Reports/xml-node-settings; existing user projects were not edited.