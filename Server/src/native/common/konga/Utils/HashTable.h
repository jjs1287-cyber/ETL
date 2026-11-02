/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_HASHTABLE_H
#define KONGA_UTILS_HASHTABLE_H

#include "StringUtils.h"             

#define HASHTABLE_SIZE 35

namespace konga {

template<class key_type, class data_type>
class HashTable {
	unsigned int (*hashFunction) (key_type);
	bool (*equalFunction) (key_type , key_type);

	template<class kt, class dt>
	struct HashBucket {
	  kt key;
	  dt data;
	  HashBucket *next;
	  HashBucket(HashBucket *next, kt key, dt data){
		  this->next=next; this->key=key; this->data=data;
	  }
	};
	HashBucket<key_type,data_type> **data;

public:
	int size;
	HashTable(int size);
	HashTable(int size, unsigned int (*hashFunction)(key_type),
			   bool (*equalFunction)(key_type, key_type));
	~HashTable();
	void put(key_type key, data_type data);
	data_type get(key_type key);
	void put(key_type key, data_type data, unsigned int hash_value);                                      
	data_type get(key_type key, unsigned int hash_value);                                      
	void emptyTable();
	std::string toString();
};

extern unsigned int hashFunction_(const char* s1);
extern bool equalFunction_(const char* s1, const char* s2);

                                                 
template<class kt, class dt>
HashTable<kt,dt>::HashTable(int size){
	this->hashFunction = hashFunction_;
	this->equalFunction = equalFunction_;
	this->size=size;
	this->data=new HashBucket<kt,dt>*[size];
	for(int i=0;i<size;++i)data[i]=0;
}

template<class kt, class dt>
HashTable<kt,dt>::HashTable(int size, unsigned int (*hashFunction_special)(kt),
			 bool (*equalFunction_special)(kt, kt))
{
	this->hashFunction = hashFunction_special;
	this->equalFunction = equalFunction_special;
	this->size=size;
	this->data=new HashBucket<kt,dt>*[size];
	for(int i=0;i<size;++i)data[i]=0;
}

template<class kt, class dt>
HashTable<kt,dt>::~HashTable(){
	emptyTable();
	delete[] this->data;
}

template<class kt, class dt>
void HashTable<kt,dt>::put(kt key, dt data){
	unsigned int location = hashFunction(key) % size;
	HashBucket<kt,dt>* newBucket = new HashBucket<kt,dt>(this->data[location], key, data);
	this->data[location] = newBucket;
}

template<class kt, class dt>
void HashTable<kt,dt>::put(kt key, dt data, unsigned int hash_value){
	unsigned int location = hash_value % size;
	HashBucket<kt,dt>* newBucket = new HashBucket<kt,dt>(this->data[location], key, data);
	this->data[location] = newBucket;
}


template<class kt, class dt>
dt HashTable<kt,dt>::get(kt key){
	unsigned int location = hashFunction(key) % size;
	HashBucket<kt,dt> *lookAt = this->data[location];
	while(lookAt!=0){
		if(equalFunction(key, lookAt->key)){
			return lookAt->data;
		}
		lookAt = lookAt->next;
	}
	return 0;
}

template<class kt, class dt>
dt HashTable<kt,dt>::get(kt key, unsigned int hash_value){                                       
	unsigned int location = hash_value % size;
	HashBucket<kt,dt> *lookAt = this->data[location];
	while(lookAt!=0){
		if(equalFunction(key, lookAt->key)){
			return lookAt->data;
		}
		lookAt = lookAt->next;
	}
	return 0;
}

template<class kt, class dt>
void HashTable<kt,dt>::emptyTable(){
	HashBucket<kt,dt> *lookAt, *saved;
	for(int i=0;i<size;i++){
		lookAt=data[i];
		while(lookAt!=0){
			saved=lookAt->next;
			delete lookAt;
			lookAt = saved;
		}
		data[i]=0;
	}
}

template<class kt, class dt>
std::string HashTable<kt,dt>::toString()
{
	std::string tmp="{";
	HashBucket<kt,dt> *lookAt;
	for(int i=0;i<size;i++){
		lookAt=data[i];
		while(lookAt!=0){
			tmp+="["+std::string((const char*)lookAt->key)+"="+std::string((const char*)lookAt->data)+"]";
			lookAt=lookAt->next;
		}
	}
	tmp+="}";
	return tmp;
}

}                   


#endif                           

