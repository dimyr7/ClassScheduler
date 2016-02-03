#ifndef SCHEDULE_H
#define SCHEDULE_H

#include "SectionCombo.hpp"
class Schedule{
	public:	
		/**
		 * Constructor
		 */
		Schedule();

		/**
		 * Destructor
		 * Does not delete the section combos
		 */
		~Schedule();

		/**
		 * Copy constrcutor
		 * @param copy is the schedule to copu
		 */
		Schedule(const Schedule& copy);

		/**
		 * Assignment operator
		 * @param copy is the schedule to copy
		 * @return this with the new values;
		 */
		Schedule& operator=(const Schedule& copy);

		/**
		 * Adds a section combination to this schedule
		 * @param combo is a new SectionCombo to add to this schedule
		 */
		void addCombo(SectionCombo* combo);
	private: 

		/**
		 * A list of section combinations
		 */
		std::vector<SectionCombo*> _combos;

};
#endif
