/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __TransacParser_H_
#define __TransacParser_H_

#include "crom.h"

namespace konga {
class JbNode;
class JbParser;
}

class TransacParser {
private:
	CROM* transac;
	std::map<CROM*,int> dtd_index_map;
	konga::JbNode** footers;
	bool beforeTransac;
	std::map<CROM*,konga::JbNode*>dtd_JbNode_map;
	konga::JbNode* node_combining;
	std::string fileName;
	konga::JbNode* jbRoot;
	konga::JbParser* parser;
	bool isXml;

public:
	TransacParser(const std::string& fileName, int typeId, CROM*pDtdRoot, CROM*pTransacCrom, std::vector<konga::JbNode*>&transac_list);
	~TransacParser();
	void write_transac_node(FILE*out, konga::JbNode*transac_node, std::stack<konga::JbNode*>&current_nodes, bool isXml);
	void write_transac_trailer(FILE*out, konga::JbNode*transac_node, std::stack<konga::JbNode*>&current_nodes, bool isXml);
	static void writeEndTagFromHeader(FILE*out, const std::string& header);
	konga::JbNode*getJbRoot(){return jbRoot;}
	konga::JbNode*getXmlHeader();
	konga::JbNode*getDocType();

private:
	enum CompareCromResult {r_is_transac, r_is_transac_child, r_is_transac_parent, r_is_diff_branch, r_unknown};
	CompareCromResult compare2Croms(CROM*dtd);
	void combineNode(size_t pos2, konga::JbNode* node_combining);
	void xml_parse_all(CROM*pDtdRoot, std::vector<konga::JbNode*>&transac_list);
	void text_parse_all(CROM*pDtdRoot, std::vector<konga::JbNode*>&transac_list);
	void process_line(CROM*dtd, std::string&id, size_t i1, size_t i2, std::vector<konga::JbNode*>& transac_list);
	konga::JbNode* findJbParent(CROM*dtd);
	void saveJbNodeToMap(CROM*dtd, konga::JbNode*node);
	void resetFooters(CROM*dtd);
	konga::JbNode*getFooter(CROM*dtd);
	void saveFooter(CROM*dtd, konga::JbNode*node);
};

#endif                      
