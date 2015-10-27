#ifndef WEEK_H
#define WEEK_H

#include <array>
#include <iostream>
#include "Section.hpp"
class Time;
class Section::Week{
	friend class Section;
	public:
		class Time;
		enum Day{
			monday,
			tuesday,
			wednesday,
			thursday,
			friday,
			saturday,
			sunday
		};
		/*
		 * returns the start time if start=true and the end time otherwise of the specified day
		 */
		Time* getTimes(Day day, bool start) const;

	private:
		/*
		 * Creates a new Week object with no active days
		 */
		Week();

		/*
		 * destructor for Week object
		 */
		~Week();

		/* * Copy constructor
		*/
		Week(const Week& copy);

		/*
		 * Copy Assignemnt Operator
		 */
		Week& operator=(const Week& copy); 

		/*
		 * Sets day to be active with the given start and end times
		 * If start is not before end, then object remains unchanged
		 */
		bool 	setDay(Day day, Time* const start, Time* const end);

		/*
		 * Unsets the provided day even it is already unset
		 */
		void  	unsetDay(Day day);


		//TODO look into updating with C++14
		std::array<std::array<Time*, 2>, 7> _times;

};
#endif
