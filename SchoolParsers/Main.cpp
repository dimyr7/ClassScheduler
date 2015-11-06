#include <fstream>
#include <iostream>
#include <string>
#include "Course.hpp"
#include "SectionBuilder.hpp"
#include "Parser.hpp"

using std::iostream;
using std::ifstream;
using namespace std;
int main(){
    Parser::Parser parse; // example.json default
    // or Parser::Parser parse(filename);
    
    /* Get sections individually 
     *
    while(parse.hasNext()) { 
        Section::Section* current = parse.getNext(); 
        cout << *current << endl; 
    } 
    return 0;
    */

    // Get vector of all sections
    std::vector<Section::Section*> sections = parse.getAll();
    for (int i = 0; i < sections.size(); i++) {
        cout << *sections[i] << endl;
    }
    return 0;
}
