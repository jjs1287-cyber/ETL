/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef DEFINES_H
#define DEFINES_H

#include <vector>
#include <string>
#include <map>

#include <konga/Utils/KString.h>

struct ltCString
{
  bool operator()(KString s1, KString s2) const
  {
    return s1.Compare(s2) < 0;
  }
};

typedef std::vector<std::string>	StringVector;
typedef StringVector::iterator		StringVectorIt;

typedef std::vector<std::string> vString;
typedef std::vector<std::string>::iterator itvString;
typedef std::vector<std::string>::const_iterator citvString;

typedef std::multimap<KString, KString, ltCString> MapStr2Str;
typedef std::multimap<KString, KString, ltCString>::iterator MapStr2StrIt;
#endif                     
