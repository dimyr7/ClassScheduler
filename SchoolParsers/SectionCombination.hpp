#ifndef COMBOBASE_H 
#define COMBOBASE_H 
#include <vector>
/*
 *
 * Abstract class that represents a collection of combination of sections that have to be take concurrently
 * Something line A1(Lecture), L2A(Lab), and D4B(Discussion) for physics
 */
using std:vector;
class ComboBase{
	public:
		/*
		 * returns: if the given combination is valid
		 * AL1, AD4 => true
		 * AL1, BD6 => false, if discussion must match lecture
		 * AD3		=> false, if the lecture is required
		 */
		virtual bool isValid() = 0;	
		/*
		 * returns a vector of sections that this combo rempresents
		 */
		vector<SectionBase> getSections();
		void addSection(Section section);
	protected:
		/*
		 * returns the configuration object
		 */
		
		ComobBase();
	private:
		vector<Section> _sections;	
};

#endif
