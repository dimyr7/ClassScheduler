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
		 * Copy Constructor
		 * @param copy is the SectionCombo to copy
		 */
		SectionCombo(const SectionCombo& copy);

		/**
		 * Assignment operator
		 * @param copy a SectionCombo to copy
		 * @return this with the new values
		 */
		SectionCombo& operator=(const SectionCombo& copy);

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
		SectionCombo(std::vector<Section*> sections);

		/**
		 * Checks if two section combos overlap
		 * @param one a SectionCombo
		 * @param two a SectionCombo
		 * @param returns true if one and two overlap, false otherwise
		 */
		static bool overlap(SectionCombo* one, SectionCombo* two);
	private:

		/**
		 * A vector of sections
		 */
		std::vector<Section*> 	_sections;	

		/**
		 * @param os the stream to write to
		 * @param combo SectionCombo to write to stream
		 * @return the stream os after information is written
		 */
		friend std::ostream& operator<<(std::ostream& os, SectionCombo& combo);
};

#endif