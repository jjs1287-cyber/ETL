/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef KONGA_LDAP_TREE_NODE_H
#define KONGA_LDAP_TREE_NODE_H

#include <string>
#include <vector>

namespace konga {

	class LdapTreeNode {
	public:
		typedef std::vector<LdapTreeNode*>::size_type size_type;
		typedef std::vector<LdapTreeNode*>::iterator iterator;
		typedef std::vector<LdapTreeNode*>::const_iterator const_iterator;
	public:
		explicit LdapTreeNode(const std::string& sName = "", const std::string& sData = "", 
			bool isDataBase64Encoded = false, const std::string& sDn = "") :
			m_sName(sName), m_sData(sData), m_isDataBase64Encoded(isDataBase64Encoded),
			m_sDn(sDn) {}
		virtual ~LdapTreeNode() {
			std::vector<LdapTreeNode*>::iterator itNode = m_vChildren.begin();
			for(; itNode != m_vChildren.end(); ++itNode)
				delete *itNode;
		}

		void AddNode(LdapTreeNode* pNode) { m_vChildren.push_back(pNode); }

		void SetName(const std::string& sName) { m_sName = sName; }
		void SetData(const std::string& sData) { m_sData = sData; }

		std::string GetName() const { return m_sName; }
		std::string GetData() const { return m_sData; }
		bool IsDataBase64Encoded() const { return m_isDataBase64Encoded; }
		std::string GetDistinguishedName() const { return m_sDn; }

		iterator begin() { return m_vChildren.begin(); }
		iterator end() { return m_vChildren.end(); }

		bool empty() const { return m_vChildren.empty(); }

		const_iterator begin() const { return m_vChildren.begin(); }
		const_iterator end() const { return m_vChildren.end(); }

		size_type size() const { return m_vChildren.size(); }

	private:
		std::vector<LdapTreeNode*> m_vChildren;
		std::string m_sName;
		std::string m_sData;
		bool m_isDataBase64Encoded;
		std::string m_sDn;
	};

}                   

#endif                          
