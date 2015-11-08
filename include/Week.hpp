#ifndef WEEK_H
#define WEEK_H
#include "Time.hpp"
#include <string>
class Week{
	//friend class Section::SectionBuilder;
	public:
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
		static const int DAYSINWEEK = 7;
		static const int TIMESINDAY = 2;

		Time* getTimes(Day day, bool start) const;
		std::string getDayStr(int i) const;
		/*
		 * Sets day to be active with the given start and end times
		 * If start is not before end, then object remains unchanged
		 */
		bool 	setDay(Day day, Time* const start, Time* const end);

	private:
		
		
		/*
		 * Unsets the provided day even it is already unset
		 */
		void  	unsetDay(Day day);
		
		/*
		 * Used to print a time object
		 */
		friend std::ostream& operator<<(std::ostream& os, const Week& week);
	
		
				
		//TODO look into updating with C++14
		Time* _times[DAYSINWEEK][TIMESINDAY];
		static const std::string daysStr[DAYSINWEEK];
		
};
#endif
