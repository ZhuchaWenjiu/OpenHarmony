/*
 *
 *   Copyright (c) International Business Machines  Corp., 2001
 *
 *   This program is free software;  you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY;  without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 *   the GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program;  if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

/*
 * NAME
 *	setitimer01.c
 *
 * DESCRIPTION
 *	setitimer01 - check that a resonable setitimer() call succeeds.
 *
 * ALGORITHM
 *	loop if that option was specified
 *	allocate needed space and set up needed values
 *	issue the system call
 *	check the errno value
 *	  issue a PASS message if we get zero
 *	otherwise, the tests fails
 *	  issue a FAIL message
 *	  break any remaining tests
 *	  call cleanup
 *
 * USAGE:  <for command-line>
 *  setitimer01 [-c n] [-f] [-i n] [-I x] [-P x] [-t]
 *     where,  -c n : Run n copies concurrently.
 *             -f   : Turn off functionality Testing.
 *	       -i n : Execute test n times.
 *	       -I x : Execute test for x seconds.
 *	       -P x : Pause for x seconds between iterations.
 *	       -t   : Turn on syscall timing.
 *
 * HISTORY
 *	03/2001 - Written by Wayne Boyer
 *
 * RESTRICTIONS
 *	none
 */

#include "test.h"

#include <errno.h>
#include <sys/time.h>

void cleanup(void);
void setup(void);

char *TCID = "setitimer01";
int TST_TOTAL = 1;

#define SEC0	0
#define SEC1	20
#define SEC2	40

int main(int ac, char **av)
{
	int lc;
	struct itimerval *value, *ovalue;

	tst_parse_opts(ac, av, NULL, NULL);

	setup();		/* global setup */

	/* The following loop checks looping state if -i option given */

	for (lc = 0; TEST_LOOPING(lc); lc++) {
		/* reset tst_count in case we are looping */
		tst_count = 0;

		/* allocate some space for the timer structures */

		if ((value = malloc((size_t)sizeof(struct itimerval))) ==
		    NULL) {
			tst_brkm(TBROK, cleanup, "value malloc failed");
		}

		if ((ovalue = malloc((size_t)sizeof(struct itimerval))) ==
		    NULL) {
			tst_brkm(TBROK, cleanup, "ovalue malloc failed");
		}

		/* set up some reasonable values */

		value->it_value.tv_sec = SEC1;
		value->it_value.tv_usec = SEC0;
		value->it_interval.tv_sec = 0;
		value->it_interval.tv_usec = 0;
		/*
		 * issue the system call with the TEST() macro
		 * ITIMER_REAL = 0, ITIMER_VIRTUAL = 1 and ITIMER_PROF = 2
		 */

		TEST(setitimer(ITIMER_REAL, value, ovalue));

		if (TEST_RETURN != 0) {
			tst_resm(TFAIL, "call failed - errno = %d - %s",
				 TEST_ERRNO, strerror(TEST_ERRNO));
			continue;
		}

		/*
		 * call setitimer again with new values.
		 * the old values should be stored in ovalue
		 */
		value->it_value.tv_sec = SEC2;
		value->it_value.tv_usec = SEC0;

		if ((setitimer(ITIMER_REAL, value, ovalue)) == -1) {
			tst_brkm(TBROK, cleanup, "second setitimer "
				 "call failed");
		}

		if (ovalue->it_value.tv_sec <= SEC1) {
			tst_resm(TPASS, "functionality is correct");
		} else {
			tst_brkm(TFAIL, cleanup, "old timer value is "
				 "not equal to expected value");
		}
	}

	cleanup();
	tst_exit();
}

/*
 * setup() - performs all the ONE TIME setup for this test.
 */
void setup(void)
{

	tst_sig(NOFORK, DEF_HANDLER, cleanup);

	TEST_PAUSE;
}

/*
 * cleanup() - performs all the ONE TIME cleanup for this test at completion
 * 	       or premature exit.
 */
void cleanup(void)
{

}
