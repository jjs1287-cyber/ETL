#pragma once
#include <string>
#include <vector>
#include <set>
namespace etlforge { namespace javascript {
struct Block {
    enum Kind { Trans, JavaScript } kind;
    size_t start, body, close, end;
};
std::vector<Block> parseBlocks(const std::string& source);
std::set<std::string> globalIdentifiers(const std::string& source);
std::vector<std::string> sourcePathLiterals(const std::string& source);
std::vector<std::pair<std::string,std::string>> hostReferenceLiterals(const std::string& source);
} }
