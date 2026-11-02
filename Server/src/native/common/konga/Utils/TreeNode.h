/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_TREE_NODE_H
#define KONGA_TREE_NODE_H

#include <string>
#include <vector>

namespace konga {

	class TreeNode {
	public:
		typedef std::vector<TreeNode*>::size_type size_type;
		typedef std::vector<TreeNode*>::iterator iterator;
		typedef std::vector<TreeNode*>::const_iterator const_iterator;
	public:
		explicit TreeNode(const std::string& sName = "", const std::string& sData = "", 
			bool isDataBase64Encoded = false) :
			m_sName(sName), m_sData(sData), m_isDataBase64Encoded(isDataBase64Encoded) {}
		virtual ~TreeNode() {
			std::vector<TreeNode*>::iterator itNode = m_vChildren.begin();
			for(; itNode != m_vChildren.end(); ++itNode)
				delete *itNode;
		}

		void AddNode(TreeNode* pNode) { m_vChildren.push_back(pNode); }

		void SetName(const std::string& sName) { m_sName = sName; }
		void SetData(const std::string& sData) { m_sData = sData; }

		std::string GetName() const { return m_sName; }
		std::string GetData() const { return m_sData; }
		bool IsDataBase64Encoded() const { return m_isDataBase64Encoded; }

		iterator begin() { return m_vChildren.begin(); }
		iterator end() { return m_vChildren.end(); }

		bool empty() const { return m_vChildren.empty(); }

		const_iterator begin() const { return m_vChildren.begin(); }
		const_iterator end() const { return m_vChildren.end(); }

		size_type size() const { return m_vChildren.size(); }

	private:
		std::vector<TreeNode*> m_vChildren;
		std::string m_sName;
		std::string m_sData;
		bool m_isDataBase64Encoded;
	};

}                   

#endif                     
