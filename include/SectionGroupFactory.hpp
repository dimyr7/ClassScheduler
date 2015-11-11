#ifndef SECTION_GROUP_FACTORY_H
#define SECTION_GROUP_FACTORY_H

#include "Course.hpp"
#include "SectionGroup.hpp"
#include "SGLecLBD.hpp"

#include <string>

class SectionGroupFactory{
	public:
		static Course::SectionGroup* createSectionGroup(std::bitset<Course::NUM_OF_SECTION_TYPES>	types, std::string sectionLetter);

	private:
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _LecLBD;
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _Onl;
};
#endif
