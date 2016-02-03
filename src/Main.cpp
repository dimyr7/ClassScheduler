#include "Course.hpp"
#include "Section.hpp"
#include "Parser.hpp"

#include <sys/resource.h>
#include <sys/types.h>
#include <sys/time.h>
using std::iostream;
using std::ifstream;
using namespace std;
int main(){

    Parser parse("cs125.json");
	std::vector<Section*> cs125S = parse.getAll();
	Course cs125("CS", "125", "Intro to Computer Science");
    for (int i = 0; i < (int)cs125S.size(); i++) {
		cs125.addSection(cs125S[i]);
    }

	std::vector<SectionCombo*> combos = cs125.getCombos();	
	//std::cout << combos.size() << " Combos Generated" << std::endl;
	for(std::vector<SectionCombo*>::const_iterator it = combos.begin(); it != combos.end(); it++){
		//std::cout << "=== New Combo ===" << std::endl;
		std::vector<Section*> sections = (*it)->getSections();
		for(std::vector<Section*>::const_iterator is = sections.begin(); is != sections.end(); is++){
			//std::cout << (*is)->getSectionName() <<std::endl;
		}
	}


    return 0;
}
