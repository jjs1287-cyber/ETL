#include <konga/Utils/VectorUtils.h>
#include <cstdlib>
#include <iostream>
#include <string>
#include <vector>
int main() {
  const std::vector<std::string> input{"a", "b", "a", "c"}, excluded{"b"};
  std::vector<std::string> output;
  konga::VectorUtils::GetUniqueStrings(input, excluded, output);
  if (output != std::vector<std::string>({"a", "c"})) return 1;
  output.clear();
  konga::VectorUtils::GetUniqueStrings(input, excluded, output, false);
  if (output != std::vector<std::string>({"a", "a", "c"})) return 2;
  output = {"existing"};
  konga::VectorUtils::GetUniqueStrings({}, excluded, output);
  if (output != std::vector<std::string>({"existing"})) return 3;
  std::cout << "PASS: exclusion, duplicate handling, ordering, empty input\n";
}
