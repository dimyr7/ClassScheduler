#ifndef COMBOBASE_H 
#define COMBOBASE_H 
#include <vector>
#include "Section.hpp"
/*
 * Represents a valid combinations of sections required to register for a class
 * Something line A1(Lecture), L2A(Lab), and D4B(Discussion) for physics
 */
using std::vector;
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
		vector<Section*> getSections();
		void addSection(Section* section);
		SectionCombo();
	private:
		vector<Section*> _sections;	
};

#endif
