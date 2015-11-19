#ifndef WEEK_H
#define WEEK_H
#include "Time.hpp"
#include <string>
/**
 * A Week object represents the start and endtimes of a section for each day
 */
class Week{
	public:
		/**
		 * Constructor
		 * Defaults to no meeting times on all days
		 */
		Week();

		/**
		 * Destrutor
		 */
		~Week();

		/** 
		 * Copy constructor
		 * @param copy is the Week object to copy
		 */
		Week(const Week& copy);

		/**
		 * Copy Assignemnt Operator
		 * @param copy is the Week object to copy
		 * @return this object with the new values set
		 */
		Week& operator=(const Week& copy); 
		
		/**
		 * An enumeration of all days of the week
		 */
		enum Day{
			monday,
			tuesday,
			wednesday,
			thursday,
			friday,
			saturday,
			sunday
		};

		/**
		 * Number of days in a week
		 */
		static const int DAYSINWEEK = 7;

		/**
		 * Number of times each day holds
		 */
		static const int TIMESINDAY = 2;

		/**
		 * Returns the start time of a certain day
		 * @param day is the day to return the start time for
		 * @return is the time object of the start time
		 */
		Time* getStartTime(Day day) const;
	
		/**
		 * Returns the end time of certain day
		 * @param day is the day to return the end time for
		 * @return is the time object of the end time
		 */
		Time* getEndTime(Day day) const;

		/**
		 * Sets day to be active with the given start and end times
		 * If start is not before end, then object remains unchanged
		 * @param day is the day to set the start/end times for
		 * @param start is the start time of the day
		 * @param end is the end time of the day
		 * @return true if the operation succeeded, false otherwise
		 */
		bool setDay(Day day, Time* const start, Time* const end);

	private:
		
		
		/** 
		 * Used to print a time object
		 * @param os is the stream to write to
		 * @param week is the week object to write
		 * @return is the stream os with info already written to it
		 */
		friend std::ostream& operator<<(std::ostream& os, const Week& week);
	
		
		/**
		 * An array holding the start and end times for each day
		 */
		Time* _times[ DAYSINWEEK ][ TIMESINDAY ];

		/**
		 * An array holding the conversion from Day enum to string
		 */
		static const std::string daysStr[DAYSINWEEK];
		
};
#endif
