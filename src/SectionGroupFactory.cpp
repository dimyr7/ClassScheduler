#include "SectionGroupFactory.hpp"

#include "SGLecLBD.hpp"
#include <cassert>
#include <bitset>
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_LecLBD("00000110000");
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_Onl   ("00010000000");
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_LecLab("00010100000");
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_LecDis("01000100000");
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_LCD   ("00000010000");
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_Lec   ("00000100000");
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_LCDDis("01000010000");
const std::bitset<Course::NUM_OF_SECTION_TYPES> SectionGroupFactory::_Ind   ("00100000000");

Course::SectionGroup* SectionGroupFactory::createSectionGroup(std::bitset<Course::NUM_OF_SECTION_TYPES> types, std::string sectionLetter){
	if(types == SectionGroupFactory::_LecLBD){
		std::cout << "Created a new Lec-LBD" << std::endl;
		return new SGLecLBD(sectionLetter);
	}
	std::cout << "THIS SHOULD NEVER HAPPEN" << std::endl;
	std::cout << types << sectionLetter << std::endl;
	assert(false);
	return NULL;
}
