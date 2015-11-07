#ifndef SECTION_GROUP_H
#define SECTION_GROUP_H
#include <vector>
#include "Course.hpp"
#include "Section.hpp"
#include "SectionCombo.hpp"
class Course::SectionGroup{
	friend class Course;	
	protected:
		virtual bool 	addSection(Section* section) = 0;
		virtual std::vector<SectionCombo*>* getCombos() const = 0;
		bool	isValid() const;

	private:
		bool 	_isValid;
};

#endif
