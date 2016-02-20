#ifndef	SECTION_COMBO_H 
#define SECTION_COMBO_H

#include "Section.hpp"
#include <vector>

/*
 * Represents a valid combinations of sections required to register for a class
 * Something line A1(Lecture), L2A(Lab), and D4B(Discussion) for physics
 */
class SectionCombo{
	public:
		/*
		 * returns true: if the given combination is valid
		 * AL1, AD4 => true
		 * AL1, BD6 => false, if discussion must match lecture
		 * AD3		=> false, if the lecture is required
		 */
		bool isValid();
		/*
		 * returns a vector of sections that this combo rempresents
		 */
		std::vector<Section*> getSections();
		void addSection(Section* section);
		SectionCombo();
		SectionCombo(std::vector<Section*> sections);
		~SectionCombo();
	private:
		std::vector<Section*> 	_sections;	
		bool 				_isValid;
};

#endif
