/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __JBNODE_H_
#define __JBNODE_H_

#include <string>
#include <vector>
#include <map>
#include <stack>

#include <konga/Utils/StringBuilder.h>

class TransacParser;

namespace konga {

class JbParser;
class XmlReader;
class XPathSearch;

class JbNode {
	friend class JbParser;
	friend class XmlReader;
	friend class XPathSearch;
	friend class XmlEditor;
	friend class TransacParser;

#ifdef _DEBUG
public:
	std::string m_name;
	std::string m_contents;
#endif

protected:
	unsigned short m_name_index;
	size_t m_pos;
	long m_length;
	JbNode*m_parent;
	std::vector<JbNode*> m_children;
	JbParser*parser;

private:
	std::string extractXmlNameFromContents(const std::string& xmlContents);
	std::string getNamespaceFromPrefix(const std::string& prefix) const;
	std::string getNS(const std::string& xmlns_prefix) const;
	void modifyContents();

public:
	enum JbNodeBase {string_base=-1, file_base=-2};
	JbNode(JbParser* parser, JbNode* parent, const std::string& name, size_t pos1, size_t pos2);

	                                                                           
	                                                               
	JbNode(JbParser*parser, JbNode* parent, JbNode* sibling, const::std::string& xmlContents, JbNodeBase lcode=string_base);
	~JbNode();

	unsigned short getNameIndex() const {return m_name_index;}

	std::vector<JbNode*>& getChildrenRef() {
		return m_children;
	}

	bool changeElementName(konga::StringBuilder& sb, const std::map<unsigned short,unsigned short>&oldToNewNameIndex_map, const std::string& header);
	bool changeElementName(FILE*out,
		const std::map<unsigned short,unsigned short>& oldToNewNameIndex_map,
		const std::map<unsigned short, std::string>& nameIndexValue_map,
		const std::string& header);

protected:
	size_t getEndPos() const {return m_pos+m_length;}
	void setEndPos(size_t pos2);

	void addAttributeNode(JbNode*attr);
	bool isChildOf(JbNode*parent);

public:
	void set(const std::string& nodeString, int lcode);
	JbNode* getParent() const {return m_parent;}
	void setParent(JbNode* parent) { m_parent = parent; }
	void moveToParent(JbNode* parent);
	void clearChildren(){m_children.clear();}
	std::vector<JbNode*>::iterator findIterator();
	std::vector<JbNode*>& getChildren() {return m_children;}
	JbNode*getChildByName(const std::string&name) const;
	const std::string& getName() const;
	std::string getLocalName() const;
	std::string getNamespaceURI() const;
	std::string getContents();
	std::string getXmlString();
	std::string getAttributeValue(const std::string& namespaceURI, const std::string& localName) const;
	bool IsValueNode() const;
	std::string getText() const;
	void writeDebugTree(FILE*debug_log, const std::string& header);
	size_t getPos() const {return m_pos;}
	long getLength() const {return m_length;}
	JbNode* getValueNode() const;
	std::string getValue();
	std::string getAttributeValue(const std::string& name, bool& found) const;

	                              
	bool isComplete(const std::string&name, const std::string& contents) const;
	bool isComplete();

	void addToChildren(const std::string& name, std::vector<JbNode*>&children);
	void collectNodes(const std::string& path, std::vector<JbNode*>&list);

	std::string getPrefix() const;
	std::string getPrefixedName() const;
	void getAttributeMap(std::map<std::string,std::string>& attributeMap);
};

}                   
#endif               

