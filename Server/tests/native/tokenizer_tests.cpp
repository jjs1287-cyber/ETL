#include <konga/Utils/StringTokenizer.h>
#include <cstdlib>
#include <iostream>
#include <string>
#include <vector>
static void require(bool ok, const char* message) {
  if (!ok) { std::cerr << message << '\n'; std::exit(1); }
}
int main() {
  konga::StringTokenizer plain("alpha,beta,,gamma", ",");
  std::vector<std::string> tokens;
  while (plain.NextToken()) tokens.push_back(plain.token());
  require(tokens == std::vector<std::string>({"alpha", ",", "beta", ",", ",", "gamma"}), "delimiter tokenization");
  plain.Reset();
  require(std::string(plain.peek_next_token()) == "alpha", "peek after reset");
  require(plain.NextToken() && std::string(plain.token()) == "alpha", "reset position");
  konga::StringTokenizer quoted("\"hello,\"\"world\"\"\",tail", ",\"");
  require(quoted.NextToken() && std::string(quoted.token()) == "\"", "opening CSV quote");
  quoted.spanTo_CSV_String();
  require(std::string(quoted.token()) == "hello,\"world\"", "escaped CSV quote");
  require(quoted.NextToken() && std::string(quoted.token()) == "hello,\"world\"", "CSV token replay");
  require(quoted.NextToken() && std::string(quoted.token()) == ",", "CSV delimiter");
  require(quoted.NextToken() && std::string(quoted.token()) == "tail", "CSV trailing field");
  const std::string large(4096, 'x');
  konga::StringTokenizer growth(large.c_str(), ",");
  require(growth.NextToken() && growth.token() == large, "token buffer growth");
  konga::StringTokenizer bounded("abcdef,tail", 3, ",");
  require(bounded.NextToken() && std::string(bounded.token()) == "abc", "bounded input");
  require(!bounded.NextToken(), "bounded input end");
  konga::StringTokenizer empty("", ",");
  require(!empty.NextToken(), "empty input");
  std::cout << "PASS: delimiters, CSV quoting, lookahead, reset, growth, bounded input, empty input\n";
}
