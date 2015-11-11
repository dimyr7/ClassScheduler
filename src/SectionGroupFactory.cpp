#include "SectionGroupFactory.hpp"

#include "SGLecLBD.hpp"
#include <cassert>

const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_LecLBD("00001100000");
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_Onl("00000001000");

Course::SectionGroup* SectionGroupFactory::createSectionGroup(std::bitset<Course::NUM_OF_SECTION_TYPES> types, std::string sectionLetter){
	if(types == SectionGroupFactory::_LecLBD){
		std::cout << "Created a new Lec-LBD" << std::endl;
		return new SGLecLBD(sectionLetter);
	}
	std::cout << "THIS SHOULD NEVER HAPPEN" << std::endl;
	assert(false);
	return NULL;
}
