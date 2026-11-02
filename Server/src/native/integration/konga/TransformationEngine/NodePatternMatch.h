/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef NodePatternMatch_H
#define NodePatternMatch_H

#include <string>
#include <vector>

#include <konga/Xml/DOMNodePtr.h>

class CROM;

class NodePatternMatch
{
private:
	bool g__err, g__mark, g__retrack, g__bIsGroup;
	std::string name;
	int g__i;
	int nmax;

	CROM** dtd_list;
	CROM** dtd0_list;
	CROM** dtd_saved;
	CROM* dtd0;

	bool* retrack_saved;
	bool* mark_list;
	std::vector<CROM*>* dtd_entries;
	FILE*LOG;

	std::map<std::string,CROM*>&elementTypes;
	std::map<std::string,CROM*>&elements;

private:
	void initArrays(int n);
	void reinitialize(int n);

public:
	NodePatternMatch(std::map<std::string,CROM*>&elementTypes, std::map<std::string,CROM*>&elements, FILE*LOG=0);
	~NodePatternMatch();

	void findMatchingCrom(const std::vector<std::string>& name_list, CROM*pDataNode);
	bool loopThruChild(CROM*&p, const std::string&header);
	CROM* find_next_dtd(CROM*parent,CROM*current,const std::string&header);
	CROM* get_dtd(int i){return dtd_list[i];}
	                                        
	CROM* get_starting_dtd(int i){return dtd0_list[i];}
};

#endif                      

