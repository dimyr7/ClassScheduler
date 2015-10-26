#ifndef TIME_H
#define TIME_H
#include <iostream>
#include <string>
using std::string;
using std::ostream;
using std::to_string;
namespace CourseInfo{
	/*
	 * Represents a time on 24 hour clock
	 */
	class Time{
		public:
			/*
			 * Sets the hour and minute to the specified parameters
			 * If either value is invalid (e.g. hour >23, hour <0, minute >59, minute <0) the time will be 00:00
			 */
			Time(int hour=0  , int minute=0);
			/*
			 * Destructor for the Time object
			 */
			~Time();
			/*
			 * Copy constructor
			 */
			Time(const Time& copy);
			/*
			 * Copy Assignment Operator
			 */
			Time& operator=(const Time& copy);

			/*
			 * returns the hour of time [0, 23]
			 */
			int 	getHour() 	const;
			/*
			 * returns the minute of the time [0, 59]
			 */
			int		getMinute() const;
			/*
			 * These functions will return true on success
			 * Returns false otherwise (i.e. value is invalid) and does not change the time
			 */
			bool 	setHour(int hour);
			bool 	setMinute(int minute);

			/*
			 * Returns true if the parameter is valid
			 */
			static bool validHour(int hour);
			static bool validMinute(int minute);

			/*
			 * Returns true if Time before is before Time after
			 */
			static bool before(const Time* before, const Time* after);
			/*
			 * returns a new Time object by adding the hour and minute of the two specified times
			 * If that causes minutes to overflow, then hour will increment additionally
			 * IF that causes hours to overflow, then they will be modded by 24
			 */
			Time operator+(const Time& second) const;
			
			/*
			 * friend function that is used for pring Time object to stream in the form 08:23
			 */
			friend ostream& operator<<(ostream& os, const Time& time);
		private:
			int _hour;
			int _minute;
	};
}
#endif
