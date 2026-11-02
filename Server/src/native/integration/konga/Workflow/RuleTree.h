/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef RULE_TREE_H
#define RULE_TREE_H

#include <string>
#include <vector>

#include <boost/noncopyable.hpp>

#include "node.h"

namespace konga {

	class RuleTree : boost::noncopyable {
	public:
	                         
	                                 

		RuleTree(void);
		RuleTree(const std::string& stackExp);
		virtual ~RuleTree() { if(m_root) delete m_root; }

		                                            
		                                                   
		void parseExpression(const std::string& exp);

		                                              
		                                                          
		void parseStack(const std::string& stk);

		std::string expressionFormat();
		std::string stackFormat();

		                
		std::string toString() const;

		node* getRoot() const { return m_root; }

		typedef std::vector< std::string >::iterator tokenIt;
	private:
		                                                           
		tokenIt parseSubExpression(tokenIt begin, tokenIt end, node** pNewNode);
		node* m_root;                         
		int m_nOpenParanthesis, m_nCloseParanthesis;
		bool m_bReverse;
	};
}

#endif               

