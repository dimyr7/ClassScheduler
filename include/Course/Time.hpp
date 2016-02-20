#ifndef TIME_H
#define TIME_H
#include <iostream>
/**
 * Represents a time on 24 hour clock
 */
class Time{
	public:
		/**
		 * Constructor
		 * If either value is invalid, it defaults to 0
		 * @param hour a 24-hour value [0, 23] to represent the hour, defaults to 0
		 * @param minute a minute value [0, 59] to represent the minute, defaults to 0
		 */
		Time(int hour=0  , int minute=0);

		/**
		 * Destructor
		 */
		~Time();

		/*
		 * Copy constructor
		 * @param copy is the Time object to copy
		 */
		Time(const Time& copy);

		/**
		 * Copy Assignment Operator
		 * @param copy is the Time object to copy
		 * @return this object with the values changed
		 */
		Time& operator=(const Time& copy);

		/**
		 * @return the hour of time [0, 23]
		 */
		int 	getHour() 	const;

		/**
		 * @return the minute of the time [0, 59]
		 */
		int		getMinute() const;


		/**
		 * @param before is the Time object assumed to be earlier
		 * @param after is the Time object assumed to be later
		 * @return true if before is ealrier than after
		 */
		static bool before(const Time* before, const Time* after);

	private:
		
		/**
		 * Checks if the hour is valid
		 * @param hour is an integer representaiton of a potential hour
		 * @reutrn true if hour is valid, false otherwise
		 */
		static bool validHour(int hour);

		/**
		 * Checks if the minutes is valid
		 * @param minute is an integer representaiton of a potential minute
		 * @return true if the minute is valid, false otherwise
		 */
		static bool validMinute(int minute);

		/**
		 * Used to print a Time object to steram
		 * @param os is the stream to write to
		 * @param time is the Time obejct to write
		 * @return a stream os that has been written to
		 */
		friend std::ostream& operator<<(std::ostream& os, const Time& time);

		/**
		 * Hour of the time
		 */
		int _hour;

		/**
		 * Minute representation of the time
		 */
		int _minute;
};
#endif
