/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <vector>
#include <string>

namespace konga {
	namespace xml {
		class DOMElementPtr;
		class XmlNamespaceDeclarations;
	}

	class SoapEnvelopeCleaner {
	public:
		SoapEnvelopeCleaner(void);
		~SoapEnvelopeCleaner(void);

		void CleanSoapEnvelope(xml::DOMElementPtr& pEnvelopeNode);

	private:
		void RemoveDuplicateNamespaces(xml::DOMElementPtr& node);
		void FindUsedNamespacesOneLevel(xml::DOMElementPtr& node);
		void AddUniqueNamespace(const std::string& prefix, const std::string& namespaceURI);
		void RemoveUnusedNamespaces(xml::DOMElementPtr& node);
		void ReplacePrefixesRecursive(const std::string& oldPrefix, const std::string& newPrefix, xml::DOMElementPtr& node);

		std::string GetNamespaceURI(const std::string& prefix) const;

		bool AlreadyDefined(const std::string& prefix, const std::string& namespaceURI, std::string& currentPrefix);

	private:
		typedef std::vector<xml::XmlNamespaceDeclarations*> NamespacesT;
		NamespacesT m_vNamespaceDeclarations;
		xml::XmlNamespaceDeclarations* m_pUsedNamespaces;
	};
}
