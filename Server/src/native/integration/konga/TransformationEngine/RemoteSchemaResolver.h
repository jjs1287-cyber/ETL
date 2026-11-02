#ifndef REMOTE_SCHEMA_RESOLVER_H
#define REMOTE_SCHEMA_RESOLVER_H

#include <string>
#include <vector>

#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMElementPtr.h>


class RemoteSchemaResolver
{
public:
	RemoteSchemaResolver(const std::string& stBaseDir);
	~RemoteSchemaResolver(void) {};

	std::string Resolve(const std::string& stNamespace, const std::string& stLocation);

private:
	std::string	m_stBaseDirectory;
	
	void ResolveRemoteImportsInDownloadedSchema(const std::string& stSchemaFile);
	bool ProcessImportNodes(const konga::xml::DOMElementPtr& schemaNode);
	bool ProcessImportNode(konga::xml::DOMElementPtr& importNode);
	void SaveSchema(const konga::xml::DOMDocumentPtr& schemaDoc, const std::string& stSchemaFile);
};



#endif
