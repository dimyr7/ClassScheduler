#include "Course/Course.hpp"
#include "Course/Section.hpp"
#include "Course/Parser.hpp"
#include "Course/CourseStore.hpp"
#include "Communication/CourseFiller.hpp"

#include <sys/resource.h>
#include <sys/types.h>
#include <sys/time.h>


void printSection(Section* sec){
	std::cout<< sec->getSectionName() << std::endl;
	std::cout<< sec->getWeek() << std::endl;
}

int main(){
	CourseStore* store = new CourseStore();
	CourseFiller::fill(store);
	
	Course* cs125 = store->get("CS125");

	std::cout << cs125->getSections().size() << std::endl;

	std::vector<SectionCombo*> combos = cs125->getCombos();	

	std::cout << combos.size() << " Combos Generated" << std::endl;
	for(std::vector<SectionCombo*>::const_iterator it = combos.begin(); it != combos.end(); it++){
		std::cout << "=== New Combo ===" << std::endl;
		std::vector<Section*> sections = (*it)->getSections();
		for(std::vector<Section*>::const_iterator is = sections.begin(); is != sections.end(); is++){
			printSection(*is);
		}
	}

    return 0;
}
