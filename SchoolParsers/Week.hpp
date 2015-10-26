#ifndef WEEK_H
#define WEEK_H
#include <array>
#include <iostream>
#include "Time.hpp"
using std::array;
namespace CourseInfo{
	class Week{
		friend class Section;
		public:
			enum Day{
				monday,
				tuesday,
				wednesday,
				thursday,
				friday,
				saturday,
				sunday
			};

		private:
			/*
			 * Creates a new Week object with no active days
			 */
			Week();

			/*
			 * destructor for Week object
			 */
			~Week();
	
			/*
			 * Copy constructor
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

			/*
			 * returns the start time if start=true and the end time otherwise of the specified day
			 */
			const Time* getTimes(Day day, bool start) const;

			//TODO look into updating with C++14
			array<array<Time*, 2>, 7> _times;

	};
}
#endif
