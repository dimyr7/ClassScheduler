#ifndef	SECTION_COMBO_H 
#define SECTION_COMBO_H

#include "Section.hpp"
#include <vector>

/**
 * Represents a combinations of sections required to register for a class
 * Something line A1(Lecture), L2A(Lab), and D4B(Discussion) for physics
 */
class SectionCombo{
	public:
		/**
		 * Constructor
		 */
		SectionCombo();
		
		/**
		 * Destructor
		 * Doesn't delet sections 
		 */ 
		~SectionCombo();
	
		/**
		 * Returns all sections in this section group
		 * @return a vectr sections
		 */
		std::vector<Section*> getSections();

		/**
		 * Adds a new section to this Combo
		 * @param section is the new section to add
		 */
		void addSection(Section* section);
	private:

		/**
		 * A vector of sections
		 */
		std::vector<Section*> 	_sections;	
};

#endif
