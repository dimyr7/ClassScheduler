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
	/*
	struct rlimit r1;
	getrlimit(RLIMIT_AS, &r1);
	
	struct rlimit r2;
	r2.rlim_max = (rlim_t)(-1);		
	setrlimit(RLIMIT_AS, &r2);
	std::cout << "The current limit is: " << (long long int)r1.rlim_cur << std::endl;
	*/
    Parser parse("cs125.json");
	Course cs125("PHYS", "211");
	std::vector<Section*> sections = parse.getAll();
	
    for (int i = 0; i < (int)sections.size(); i++) {
        //cout << *sections[i] << endl;
		cs125.addSection(sections[i]);
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
