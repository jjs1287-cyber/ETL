/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                        
  
                                                                      

#ifndef NODE_H
#define NODE_H

#include <string>
#include <vector>

#include <boost/noncopyable.hpp>

                       

namespace konga {

	   
                                           
   
	class node : boost::noncopyable {
	public:
		                                                                                 
		                                                         
		enum bool_op { op_undefined=-1,
					   op_not=100,
					   op_multiply=90, op_divide=91,
					   op_plus=80, op_minus=81, 
					   op_lt=70, op_lteq=71, op_gt=72, op_gteq=73,
					   op_eq=60, op_noteq=61,
					   op_xor=40, op_or=41, op_and = 42,
					   op_assign=20, op_plusassign=21, op_minusassign=22,
					   op_multiplyassing=23, op_divideassign=24,
					   op_bool_fcn=1, op_exception=2};

		static bool_op get_bool_op(const std::string& stStringRep);
		static bool is_unary(bool_op op);
		static const char* op_str(bool_op op);

		explicit node(bool_op bop=op_undefined) : m_op(bop) {}
		explicit node(const char* d) : m_op(op_undefined), m_data(d) {}
		explicit node(const std::string& d) : m_op(op_undefined), m_data(d) {}

		~node(void) {
			for(int i=m_children.size()-1; i>=0 ; --i) {
				delete m_children[i];
			}
		}

		void add_node(node* n) { m_children.push_back(n); }

		node* get_node(size_t i) { 
			if( i>m_children.size()-1 ) return 0;
			return m_children[i];
		}

		int fanout() const { return static_cast<int>(m_children.size()); }
		bool_op op() const { return m_op; }

		                                                   
		const char* op_str() const;

		                    
		std::string data() const { return m_data; }

		bool is_complete() const;

		                      
		std::string toString(const std::string& indention = "") const;

	private:
		std::vector<node*> m_children;
		bool_op m_op;
		std::string m_data;
	};
}                   

#endif          

