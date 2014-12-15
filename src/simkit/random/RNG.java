package simkit.random;

/*
 **************************************************************************
 **************************************************************************
 **************************************************************************
 *
 *  RNG - a Random Number Generator class for Discrete Event Simulation
 *  Copyright (C) 1999  Paul J. Sanchez
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  General Public License for more details. 
 *
 *  A copy of the GNU General Public License can be found at
 *    http://www.fsf.org/copyleft/gpl.html
 *  Alternatively, you can get a copy by writing to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 *  02111-1307, USA.  
 *
 **************************************************************************
 **************************************************************************
 **************************************************************************
 *
 *  The following code generates Uniform(0,1) random numbers by
 *  pooling the outcome of a Linear Congruential Generator (LCG) and a
 *  Tausworthe generator.  The periods of these two generators are
 *  relatively prime, so the period of the combined generator is the
 *  product of the two, approximately 2^62.  This should give ~2^32
 *  usable numbers before being caught by Heath & Sanchez's birthday
 *  test, as opposed to ~2^17 usable numbers if a 31 bit LCG or
 *  Tausworthe is used by itself.
 *
 *  This generator has passed every empirical test I've tried on it:
 *  the Birthday test, Chi-Square, runs (about the mean), spectral,
 *  and Kolmogorov-Smirnov.  This doesn't mean diddly - since we know
 *  the output is algorithmic and hence non-random, somebody somewhere 
 *  will eventually construct a test clever enough to catch this guy.
 *  Use it entirely at your own risk.
 *
 *  Last modified - 8/13/99 - pjs
 *  
 */

/**
 *  Generates Uniform(0,1) random numbers by
 *  pooling the outcome of a Linear Congruential Generator (LCG) and a
 *  Tausworthe generator.  The periods of these two generators are
 *  relatively prime, so the period of the combined generator is the
 *  product of the two, approximately 2^62.  This should give ~2^32
 *  usable numbers before being caught by Heath &amp; Sanchez's birthday
 *  test, as opposed to ~2^17 usable numbers if a 31 bit LCG or
 *  Tausworthe is used by itself.
 *  <p>
 *  RNG - a Random Number Generator class for Discrete Event Simulation
 *  Copyright (C) 1999  Paul J. Sanchez
 *  <p>
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  <p>
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  General Public License for more details. 
 *  <p>
 *  A copy of the GNU General Public License can be found at
 *    http://www.fsf.org/copyleft/gpl.html
 *  Alternatively, you can get a copy by writing to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 *  02111-1307, USA.  
 *
 * @version $Id$
**/
public class RNG
{
    long	seed1;
    long	seed2;

/**
* Constructs a new RNG with both seeds set to the default (42).
**/
    public RNG()
    {
	this.randomizeSeeds();
    }

/**
* Constructs a new RNG with the given seeds.
**/
    public RNG(int s1, int s2)
    {
	seed1 = s1;
	seed2 = s2;
    }

/**
* Sets the seeds to the given values.
**/
    public void setSeeds(int s1, int s2)
    {
	seed1 = s1;
	seed2 = s2;
    }

/**
* Sets both seeds to 42.
**/
    public void randomizeSeeds()
    {
	/*
	struct timeval tp;
	struct timezone tzp;

	gettimeofday(&tp, &tzp);
	srandom(tp.tv_usec);
	seed1 = random();
	seed2 = random();
	*/

	seed1 = seed2 = 42;
    }

/**
* Generates the next U(0,1) value.
**/
    public float getUniform()
    {
	/*
	 * PMMLCG with modulus 2^31 - 1 and multiplier 630360016
	 * based on Marse & Roberts "Implementing a Portable FORTRAN
	 * Uniform(0,1) Generator", Simulation, 41: 135-139 (1983)
	 */
	seed1 *= 630360016;
	seed1 %= 0x7FFFFFFF;

	/*
	 * Tausworthe generator with shift quantities as described on
	 * p. 202 of Bratley, Fox and Schrage, "A Guide to Simulation, 
	 * 2nd Ed.".
	 */
	seed2 ^= seed2 >> 13;
	seed2 ^= seed2 << 18;
	seed2 &= 0x7FFFFFFF;

	return (seed1 ^ seed2) * 4.656612875e-10f;
    }

    /* The following was for debugging.  Leave out of production code...

    public void getSeeds()
    {
	System.out.println(seed1 + ", " + seed2);
    }

    */

}

