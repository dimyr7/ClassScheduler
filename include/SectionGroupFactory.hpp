#ifndef SECTION_GROUP_FACTORY_H
#define SECTION_GROUP_FACTORY_H

#include "Course.hpp"
#include "SectionGroup.hpp"
#include "SGLecLBD.hpp"

#include <string>

class SectionGroupFactory{
	public:
		static Course::SectionGroup* createSectionGroup(std::bitset<Course::NUM_OF_SECTION_TYPES>	types, std::string id);

	private:
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _LecLBD; // DONE
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _Onl;
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _LecLab; // DONE
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _LecDis;
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _LCD;
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _Lec;
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _LCDDis;
		static const std::bitset<Course::NUM_OF_SECTION_TYPES> _Ind;


};
#endif
