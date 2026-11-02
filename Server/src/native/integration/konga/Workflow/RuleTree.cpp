/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                                      
  
                                                                      
#include <stack>
#include <sstream>	               
#include <memory>	           
#include <map>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/StringUtils.h>	                                     

#include "node.h"
#include "RuleTree.h"

using namespace std;
using namespace konga;


                    
namespace {
	const char sep = ';';
	const char* csep = ";";
	const char tokens[] = "()&|!~+-=<>^%*/\"";

	string evalToExpression(node* pn, bool bReverse) {
		if( pn->op() == node::op_undefined )
			return pn->data();
		else if ( pn->op() == node::op_not ) {
			return pn->op_str() + pn->get_node(0)->data();
		}

		stringstream ss;
		ss << '(';
		if ( bReverse ) {
			for( int i=pn->fanout()-1; i>=0; --i ) {
				ss << evalToExpression(pn->get_node(i), bReverse);
				if(i>0) ss << pn->op_str();
			}
		}
		else {
			for( int i=0, n=pn->fanout(); i<n; ++i ) {
				ss << evalToExpression(pn->get_node(i), bReverse);
				if (i<n-1) ss << pn->op_str();
			}
		}
		ss << ')';
		return ss.str();
	}

	string evalToStack(node* pn, bool bReverse) {

		if( pn->op() == node::op_undefined ) return pn->data();

		stringstream ss;
		if ( bReverse ) {
			for(int i=pn->fanout()-1; i>=0; --i) {
				ss << evalToStack(pn->get_node(i), bReverse) << sep;
			}
		}
		else {
			for( int i=0, n=pn->fanout(); i<n; ++i ) {
				ss << evalToStack(pn->get_node(i), bReverse) << sep;
			}
		}

		ss << pn->op_str();
		int fanout = pn->fanout();
		if ( !node::is_unary(pn->op()) && fanout != 2 ) {
			ss << pn->fanout();
		}
		return ss.str();
	}

	                                                                 
	template<class T>
	class AutoDeleteStackPointers {
	public:
		AutoDeleteStackPointers(stack<T*>& stack) : m_pStack(&stack) {}
		~AutoDeleteStackPointers(void) {
			if ( m_pStack != 0 ) {
				while( !m_pStack->empty() ) {
					delete m_pStack->top();
					m_pStack->pop();
				}
				m_pStack = 0;
			}
		}

	private:
		stack<T*>* m_pStack;
	};

	                                                                                                               
	                                                                                  
	inline bool checkToken(const char* tok, StringTokenizer& tokenizer, const char* compareTok, vector<string>& vTokens) {
		string stTok(tok);	                                                                                                                          
		if ( !tokenizer.NextToken() )
			throw CrException("No more tokens after \"" + string(tok) + "\".");
		const char* nextTok = tokenizer.token();
		if ( strcmp(nextTok, compareTok) == 0 ) {
			vTokens.push_back(stTok+nextTok);
			return true;
		}
		else {
			vTokens.push_back(stTok);
			return false;
		}
	}
}

RuleTree::RuleTree(void)
	: m_root(0), m_nOpenParanthesis(0), m_nCloseParanthesis(0), m_bReverse(true) {
}
RuleTree::RuleTree(const std::string& stackExp)
	: m_root(0), m_nOpenParanthesis(0), m_nCloseParanthesis(0), m_bReverse(true) {
	parseStack(stackExp);
}

                    
namespace {
	void getTokens(const string& s, vector<string>& vTokens) {
		string expr(RemoveUnquotedWhitespace(s));
		if ( expr.empty() ) throw CrException("Expression is empty or contains only ignorable whitespace.");
		StringTokenizer rawTokens(expr.c_str(), tokens);

		                                                                                                                                     
		bool bIsQuoted = false;
		string stTok;
		while( rawTokens.NextToken() ) {
	begin:	                                                                                                   
			const char* tok = rawTokens.token();
			if ( strcmp(tok, "\"") == 0 ) {
				                            
				if ( bIsQuoted ) {
					                               
					stTok += tok;
					vTokens.push_back(stTok);
					stTok.erase();
				}
				else {
					                      
					stTok = tok;
				}
				bIsQuoted = !bIsQuoted;
			}
			else if ( bIsQuoted ) {
				                                                                                     
				stTok += tok;
			}
			else if ( strcmp(tok, "=") == 0 ) {
				                  
				if ( !rawTokens.NextToken() )
					throw CrException("No more tokens after \"=\"");
				tok = rawTokens.token();
				if ( strcmp(tok, "=") == 0 )
					vTokens.push_back("==");
				else {
					vTokens.push_back("=");
					goto begin;	                    
				}
			}
			else if ( strcmp(tok, "&") == 0 ) {
				                                                                                    
				vTokens.push_back("&&");
				if ( !rawTokens.NextToken() )
					throw CrException("No more tokens after \"&\"");
				tok = rawTokens.token();
				if ( strcmp(tok, "&") != 0 )
					goto begin;	                    
			}
			else if ( strcmp(tok, "|") == 0 ) {
				                                                                                   
				vTokens.push_back("||");
				if ( !rawTokens.NextToken() )
					throw CrException("No more tokens after \"|\"");
				tok = rawTokens.token();
				if ( strcmp(tok, "|") != 0 )
					goto begin;	                    
			}
			else if ( strcmp(tok, "!") == 0 ) {
				if ( !checkToken(tok, rawTokens, "=", vTokens) )
					goto begin;
			}
			else if ( strcmp(tok, ">") == 0 ) {
				if ( !checkToken(tok, rawTokens, "=", vTokens) )
					goto begin;
			}
			else if ( strcmp(tok, "<") == 0 ) {
				if ( !checkToken(tok, rawTokens, "=", vTokens) )
					goto begin;
			}
			else if ( strcmp(tok, "+") == 0 ) {
				if ( !checkToken(tok, rawTokens, "=", vTokens) )
					goto begin;
			}
			else if ( strcmp(tok, "-") == 0 ) {
				if ( !checkToken(tok, rawTokens, "=", vTokens) )
					goto begin;
			}
			else if ( strcmp(tok, "*") == 0 ) {
				if ( !checkToken(tok, rawTokens, "=", vTokens) )
					goto begin;
			}
			else if ( strcmp(tok, "/") == 0 ) {
				if ( !checkToken(tok, rawTokens, "=", vTokens) )
					goto begin;
			}
			else {
				vTokens.push_back(tok);
			}
		}
		if ( bIsQuoted )
			throw CrException("Open quote found in expression.");
	}
}

void RuleTree::parseExpression(const string& s) {
	vector<string> vTokens;
	getTokens(s, vTokens);

	m_nOpenParanthesis = 0;
	m_nCloseParanthesis = 0;
	if ( m_root != 0 ) {
		delete m_root;
		m_root = 0;
	}

                                                
                                            
	 parseSubExpression(vTokens.begin(), vTokens.end(), &m_root);

	if ( m_nOpenParanthesis > m_nCloseParanthesis )
		throw CrException("Expression contains unclosed parenthesis");
	else if ( m_nOpenParanthesis < m_nCloseParanthesis )
		throw CrException("Expression contains too many closing parenthesis");
	m_bReverse = false;
}

                     
namespace {
	inline bool tokcmp(RuleTree::tokenIt tok, const char* strTok) {
		return strcmp(tok->c_str(), strTok) == 0;
	}
}

RuleTree::tokenIt RuleTree::parseSubExpression(tokenIt begin, tokenIt end, node** pNewNode) {

	std::stack<node*> incompleteExprStack, wellFormedExprStack;
	AutoDeleteStackPointers<node> delete_incompleteExprStack(incompleteExprStack);
	AutoDeleteStackPointers<node> delete_wellFormedExprStack(wellFormedExprStack);

	                                                                                         
	tokenIt currentTok = --begin;
	while( ++currentTok != end ) {
		if ( tokcmp(currentTok, "(") ) {
			++m_nOpenParanthesis;
			node* pn = 0;
			currentTok = parseSubExpression(++currentTok, end, &pn);
			wellFormedExprStack.push(pn);
			while( !incompleteExprStack.empty() ) {
				pn = incompleteExprStack.top();
				if ( wellFormedExprStack.empty() ) {
					throw CrException("The operator \"" + string(node::op_str(pn->op())) + "\" is missing an argument.");
				}
				pn->add_node( wellFormedExprStack.top() );
				wellFormedExprStack.pop();
				incompleteExprStack.pop();
				wellFormedExprStack.push(pn);
			}
			continue;
		}
		else if ( tokcmp(currentTok, ")") ) {
			++m_nCloseParanthesis;
			break;
		}
		else {
                                 
			node::bool_op op = node::get_bool_op(*currentTok);
			if ( op != node::op_undefined ) {
				                                
				auto_ptr<node> np(new node(op));
				if ( op == node::op_not ) {
					incompleteExprStack.push(np.release());
					continue;
				}
				if ( wellFormedExprStack.empty() ) {
					                                                             
					throw CrException("Unexpected operator \"" + *currentTok + "\" encountered.");
				}
				if ( incompleteExprStack.empty() ) {
					np->add_node(wellFormedExprStack.top());
					wellFormedExprStack.pop();
				}
				else {
					node* prevNode = incompleteExprStack.top();	                        
					if ( np->op() > prevNode->op() ) {
						                                     
						np->add_node(wellFormedExprStack.top());
						wellFormedExprStack.pop();                 
					}
					else {
						prevNode->add_node(wellFormedExprStack.top());
						wellFormedExprStack.pop();
						if ( prevNode->is_complete() ) {
							incompleteExprStack.pop();
							wellFormedExprStack.push(prevNode);
						}
						np->add_node(wellFormedExprStack.top());
						wellFormedExprStack.pop();
					}
				}
				incompleteExprStack.push(np.release());
			}
			else {
				                               
				auto_ptr<node> np(new node(*currentTok));
				                                                                                               
				if ( !incompleteExprStack.empty() ) {
					node* prevNode = incompleteExprStack.top();	                                                 
					if ( prevNode->op() == node::op_not ) {
						prevNode->add_node( np.release() );
						np.reset(prevNode);
						incompleteExprStack.pop();
					}
				}
				wellFormedExprStack.push(np.release());
			}
		}
	}                        

	                                         
	while( !incompleteExprStack.empty() ) {
		node* np = incompleteExprStack.top();
		if ( wellFormedExprStack.empty() ) {
			throw CrException("The operator \"" + std::string(node::op_str(np->op())) + "\" is missing an argument.");
		}
		np->add_node(wellFormedExprStack.top());
		wellFormedExprStack.pop();
		incompleteExprStack.pop();
		wellFormedExprStack.push(np);
	}

	if ( wellFormedExprStack.size() != 1 )
		throw CrException("Illegal expression.");
	
	node* pn = wellFormedExprStack.top();
	wellFormedExprStack.pop();
	*pNewNode = pn;
	return currentTok;
}

void RuleTree::parseStack(const string& sin) {

	const string stStack(RemoveUnquotedWhitespace(sin));
	if ( stStack.empty() )
		throw CrException("The stack expression is empty or contains only ignorable whitespace.");

	stack<node*> st;
	AutoDeleteStackPointers<node> adopt_stack(st);	                                                                                                 
	StringTokenizer tokenizer(stStack.c_str(), csep);
	while( tokenizer.NextToken() ) {
		string stTok(tokenizer.token());
		if ( strcmp(stTok.c_str(), csep) == 0 )
			continue;

		int fanout = 0;
		int len = stTok.length();
		if ( len > 1 && len < 4 ) {
			                                           
			if ( isdigit(stTok.at(len-1)) ) {
				fanout = atoi(stTok.substr(len-1).c_str());
			}
		}

		node::bool_op op = node::get_bool_op(stTok.substr(0, fanout>0?len-1:len));
		if ( op != node::op_undefined ) {
			                                                                                   
			if ( fanout == 0 )
				fanout = node::is_unary(op) ? 1 : 2;

			if ( node::is_unary(op) && fanout != 1 )
				throw CrException("A fanout of " + konga::toString(fanout) + " was given for a unary operator \"" + string(node::op_str(op)) + "\" where only 1 is valid.");

			                       
			auto_ptr<node> pn(new node(op));
			for(int i=0; i<fanout ; ++i) {
				if( st.size() == 0 )
					throw CrException("Missing operand for operator \"" + string(node::op_str(op)) + "\"");
				pn->add_node(st.top());
				st.pop();
			}
			st.push(pn.release());
		}
		else {
			                     
			st.push(new node(stTok));
		}
	}

	if ( m_root != 0 ) delete m_root;
	m_root = st.top();
	st.pop();	                                     
	if ( !st.empty() )
		throw CrException("Some operands are missing operators or vice versa.");
	m_bReverse = true;	                                                             
}

string RuleTree::expressionFormat() {
	if ( m_root == 0 )
		return string();
	string exp(evalToExpression(m_root, m_bReverse));
	                          
	exp.erase(0, 1);
	return exp.erase(exp.size()-1,1);
}

string RuleTree::stackFormat() {
	if ( m_root == 0 )
		return string();
	return evalToStack(m_root, m_bReverse);
}

string RuleTree::toString() const {
	return m_root!=0 ? m_root->toString() : string();
}
