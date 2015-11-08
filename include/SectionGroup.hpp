#ifndef SECTION_GROUP_H
#define SECTION_GROUP_H
#include "Course.hpp"
#include <vector>
class Course::SectionGroup{	
	public:
		virtual bool 	addSection(Section* section) = 0;
		virtual std::vector<SectionCombo*> getCombos() const = 0;
		bool	isValid() const;

	private:
		bool 	_isValid;
};

#endif
