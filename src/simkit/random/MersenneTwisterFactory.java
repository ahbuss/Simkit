
package simkit.random;

import java.util.logging.*;
/**
* Used to dynamically create instances of MersenneTwisterDC.
*
* @version $Id$
* @author John Ruck (Rolands and Associates Corporation 7/1/05)
**/
public class MersenneTwisterFactory {

    public static final String _VERSON_ = "$Id$";

    public static Logger log = Logger.getLogger("simkit.random");

/**
* The maximum value of a 32 bit unsigned int. For any operation on a unsigned 32 bit integer
* from the C code the result will be anded with this to simulate the rollover since
* we need to use longs.
**/
    private static final long MAX_VALUE = 4294967295L;

/**
* Holds a default instance of the factory.
**/
    protected static MersenneTwisterFactory instance = new MersenneTwisterFactory(0);

/**
* Gets the default instance of the factory
**/
    public static MersenneTwisterFactory getInstance() {return instance;}

/**
* Create a new instance with the given seed. Note the seed is for the generator not
* the MT's that are created.
**/ 
    public MersenneTwisterFactory(int seed) {
        if (seed < 0) {
            String msg = "The seed cannot be less than zero.";
            log.severe(msg);
            throw new IllegalArgumentException(msg);
        }
        init_dc(seed);
    }

/**
* Create a MersenneTwister with the given period exponent and seed.
**/
    public MersenneTwisterDC createMT(int p, int seed) {
        MersenneTwisterDC mt = new MersenneTwisterDC();
        MTS mts = get_mt_parameter(31, p);
        mt.setSeeds(mts.toArray());
        mt.sgenrand(seed);
        return mt;
    }

/**
* Create a MersenneTwister with the given period exponent, stream id and seed.
**/
    public MersenneTwisterDC createMT(int p, int seed, int id) {
        MersenneTwisterDC mt = new MersenneTwisterDC();
        MTS mts = get_mt_parameter_id(31, p, id);
        mt.setSeeds(mts.toArray());
        mt.sgenrand(seed);
        return mt;
    }

/**
* Create an array of MersenneTwisters.
**/
    public MersenneTwisterDC[] createMTArray(int p, int[] seeds) {
        int number = seeds.length;
        MTS[] mts = get_mt_parameters(31, p, number - 1);
        MersenneTwisterDC[] mt = new MersenneTwisterDC[number];
        for (int i = 0; i < number; i++) {
            mt[i] = new MersenneTwisterDC();
            mt[i].setSeeds(mts[i].toArray());
            mt[i].sgenrand(seeds[i]);
        }
        return mt;
    }

/* What follows are ports of the various original C files */

/* from dc.h */
    public class MTS {
        public static final int NUMBER_PARAMS = 15;
        public long aaa;
        public int mm;
        public int nn;
        public int rr;
        public int ww;
        public long wmask;
        public long umask;
        public long lmask;
        public int shift0;
        public int shift1;
        public int shiftB;
        public int shiftC;
        public long maskB;
        public long maskC;
        public int i;
        public long[] state;

/**
* This puts the structure into a single array starting at 1.
**/
        public long[] toArray() {
            long[] data = new long[NUMBER_PARAMS + state.length + 1];
            data[1] = aaa;
            data[2] = mm;
            data[3] = nn;
            data[4] = rr;
            data[5] = ww;
            data[6] = wmask;
            data[7] = umask;
            data[8] = lmask;
            data[9] = shift0;
            data[10] = shift1;
            data[11] = shiftB;
            data[12] = shiftC;
            data[13] = maskB;
            data[14] = maskC;
            data[15] = i;
            for (int i = 0; i < state.length; ++i) {
                data[NUMBER_PARAMS + 1 + i] = state[i];
            }
            return data;
        }
    }//end MTS

/* check32.c */

/* Copyright (C) 2001 Makoto Matsumoto and Takuji Nishimura.       */
/* This library is free software; you can redistribute it and/or   */
/* modify it under the terms of the GNU Library General Public     */
/* License as published by the Free Software Foundation; either    */
/* version 2 of the License, or (at your option) any later         */
/* version.                                                        */
/* This library is distributed in the hope that it will be useful, */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of  */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            */
/* See the GNU Library General Public License for more details.    */
/* You should have received a copy of the GNU Library General      */
/* Public License along with this library; if not, write to the    */
/* Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA   */ 
/* 02111-1307  USA                                                 */


    public static final int CHECK32_REDU = 0;//There is another REDU in prescr.c which is 1
                                            // REDU in seive.c is also 0.
    public static final int IRRED = 1;

    public static final int LSB = 0x1;
    public static final int WORDLEN = 32;

    protected long upper_mask;
    protected long lower_mask;
    protected long word_mask;

    protected void _InitCheck32_dc(int r, int w) {
        int i;

        /* word_mask (least significant w bits) */
        word_mask = (~(0x0)) & MAX_VALUE;
        word_mask <<= WORDLEN - w;
        word_mask &= MAX_VALUE;
        word_mask >>= WORDLEN - w;
        word_mask &= MAX_VALUE;
        /* lower_mask (least significant r bits) */
        for (lower_mask=0,i=0; i<r; ++i) {
            lower_mask <<= 1;
            lower_mask &= MAX_VALUE;
            lower_mask |= LSB;
        }
        /* upper_mask (most significant (w-r) bits */
        upper_mask = ((~lower_mask) & MAX_VALUE) & word_mask;
    }


    protected int _CheckPeriod_dc(long a, int m, int n, int r, int w) {
        int i, j, p, pp;
        long y;
        long [] x;
        long[] init;
        long[] mat = new long[2];

     
        p = n*w-r;
        x = new long[2*p];

        init = new long[n];

        /* set initial values */
        for (i=0; i<n; ++i) {
            x[i] = init[i] = (word_mask & _genrand_dc());
        }
        /* it is better that LSBs of x[2] and x[3] are different */
        if ( (x[2]&LSB) == (x[3]&LSB) ) {
            x[3] ^= 1;
            x[3] &= MAX_VALUE;
            init[3] ^= 1;
            init[3] &= MAX_VALUE;
        }

        pp = 2*p-n;
        mat[0] = 0x0; mat[1] = a;
        for (j=0; j<p; ++j) {
                
            /* generate */
            for (i=0; i<pp; ++i){
                y = (x[i]&upper_mask) | (x[i+1]&lower_mask);
                x[i+n] = x[i+m] ^ ( ((y>>1) ^ mat[(int)y&LSB]) & MAX_VALUE );
                x[i+n] &= MAX_VALUE;
            }
                
            /* pick up odd subscritpt elements */
            for (i=2; i<=p; ++i)
                x[i] = x[(i<<1)-1];
                
            /* reverse generate */
            for (i=p-n; i>=0; --i) {
                y = x[i+n] ^ ((x[i+m] ^ mat[ (int)x[i+1]&LSB ]) & MAX_VALUE);
                y &= MAX_VALUE;
                y <<=1; 
                y &=MAX_VALUE;
                y |= x[i+1]&LSB;

                x[i+1] = (x[i+1]&upper_mask) | (y&lower_mask);
                x[i] = (y&upper_mask) | (x[i]&lower_mask);
            }
            
        }

        if ((x[0]&upper_mask)==(init[0]&upper_mask)) {
            for (i=1; i<n; ++i) {
                if (x[i] != init[i])
                break;
            }
            if (i==n) {
                return IRRED;
            }
        }


        return CHECK32_REDU;
    }

/* eqdeg.c */

/* Copyright (C) 2001 Makoto Matsumoto and Takuji Nishimura.       */
/* This library is free software; you can redistribute it and/or   */
/* modify it under the terms of the GNU Library General Public     */
/* License as published by the Free Software Foundation; either    */
/* version 2 of the License, or (at your option) any later         */
/* version.                                                        */
/* This library is distributed in the hope that it will be useful, */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of  */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            */
/* See the GNU Library General Public License for more details.    */
/* You should have received a copy of the GNU Library General      */
/* Public License along with this library; if not, write to the    */
/* Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA   */ 
/* 02111-1307  USA                                                 */



/**************************************/
    public static final int SSS = 7;
    public static final int TTT = 15;
    public static final int S00 = 12;
    public static final int S01 = 18;
/**************************************/

/** for get_tempering_parameter_hard **/
    public static final int LIMIT_V_BEST_OPT = 15; 
/**************************************/

    public static final int WORD_LEN = 32;
    public static final int MIN_INFINITE = (-2147483647-1);

//#define TRNSTMP  tmp ^= ( (tmp>>shift_0) & greal_mask )
//#define MASKTMP  tmp ^= ( (tmp<<shift_s) & mask_b);tmp ^= ( (tmp<<shift_t) & mask_c)
//#define LSB(x) ( ((x) >> ggap) & 0x1 )
    protected long LSB(long x) {
        return ( ((x) >> ggap) & 0x1 );
    }

    public static class Vector {
        public long z;    /* integer part */
        public long[] cf;  /* fraction part */
        public int start;     /* beginning of fraction part */
        public int degree;    /* maximum degree */
        public long bp;   /* bitpatterm (shifted&bitmasked) at the maximum degree */
    }

    public static class MaskNode {
        public long b,c;
        public int v,leng;
        public MaskNode next;
    }

/***********************************/
/*******  global variables  ********/
/***********************************/
    protected long[] bitmask = new long[WORD_LEN];
    protected long[] cur_bitmask = new long[WORD_LEN];
    protected long mask_b, mask_c;
    protected long upper_v_bits;
    protected int shift_0, shift_1, shift_s, shift_t;
    protected int mmm, nnn, rrr, www;
    protected long[] aaa = new long[2];
/** most significant  (WWW - RRR) bits **/
    protected long gupper_mask;
/** least significant RRR bits **/
    protected long glower_mask;
/** upper WWW bitmask **/
    protected long greal_mask;
/** difference between machine wordsize and dest wordsize **/
    protected int ggap;
/** for optimize_v_hard **/
    protected int[] gcur_maxlengs = new int[WORD_LEN];
    protected long gmax_b, gmax_c;
/*************************************/


    protected void _get_tempering_parameter_dc(MTS mts) {
        init_tempering(mts);
        optimize_v(0x0, 0x0, 0);
        mts.shift0 = shift_0;
        mts.shift1 = shift_1;
        mts.shiftB = shift_s;
        mts.shiftC = shift_t;
        mts.maskB = mask_b >> ggap;
        mts.maskC = mask_c >> ggap;
    }

    protected void _get_tempering_parameter_hard_dc(MTS mts) {
        int i;
        MaskNode mn0 = new MaskNode();
        MaskNode cur;
        MaskNode next;

        init_tempering(mts);

        for (i=0; i<www; i++) 
            gcur_maxlengs[i] = -1;

        mn0.b = mn0.c = mn0.leng = 0;
        mn0.next = null;
        
        cur = mn0;
        for (i=0; i<LIMIT_V_BEST_OPT; i++) {
            log.info("i= " + i + "The MaskNode contains " + countMaskNodeLevels(cur));
            next = optimize_v_hard(i, cur);
            if (i > 0) 
                delete_MaskNodes(cur);
            cur = next;
        }
        delete_MaskNodes(cur);

        optimize_v(gmax_b, gmax_c,i);
        mts.shift0 = shift_0;
        mts.shift1 = shift_1;
        mts.shiftB = shift_s;
        mts.shiftC = shift_t;
        mts.maskB = mask_b >> ggap;
        mts.maskC = mask_c >> ggap;

        /* show_distrib(mts); */
    }

/**
* For debugging.
**/
    private int countMaskNodeLevels(MaskNode mn) {
        int i = 0;
        MaskNode node = mn;
        while (node != null) {
            i++;
            node = node.next;
        }
        return i;
    }

    protected void init_tempering(MTS mts) {
        int i;

        mmm = mts.mm;
        nnn = mts.nn;
        rrr = mts.rr; 
        www = mts.ww;
        shift_0 = S00;
        shift_1 = S01;
        shift_s = SSS;
        shift_t = TTT; 
        ggap = WORD_LEN - www;
        /* bits are filled in mts.aaa from MSB */
        aaa[0] = 0x0; aaa[1] = ((mts.aaa) << ggap) & MAX_VALUE;


        for( i=0; i<WORD_LEN; i++) {
            bitmask[i] = (0x80000000 >>> i) & MAX_VALUE;
            cur_bitmask[i] = (0x80000000 >>> i) & MAX_VALUE;
            log.info("bitmask["+i+"]=" + bitmask[i]);
        }

        for( i=0, glower_mask=0; i<rrr; i++)
            glower_mask = ((glower_mask<<1) & MAX_VALUE)| 0x1;
        gupper_mask = ~glower_mask;
        gupper_mask &= MAX_VALUE;
        gupper_mask <<= ggap;
        gupper_mask &= MAX_VALUE;
        glower_mask <<= ggap;
        glower_mask &= MAX_VALUE;

        greal_mask = (gupper_mask | glower_mask);

    }

/** (v-1) bitmasks of b,c */
    protected MaskNode optimize_v_hard(int v, MaskNode prev_masks) {
        int i, ll, t;
        long[] bbb = new long[8];
        long[] ccc = new long[8];
        MaskNode cur_masks = null;


        while (prev_masks != null) {

            ll = push_stack(prev_masks.b,prev_masks.c,v,bbb,ccc);

            for (i=0; i<ll; ++i) {
                mask_b = bbb[i];
                mask_c = ccc[i];
                t = lenstra(v+1);
                if (t >= gcur_maxlengs[v]) {
                    gcur_maxlengs[v] = t;
                    gmax_b = mask_b;
                    gmax_c = mask_c;
                    cur_masks = cons_MaskNode(cur_masks, mask_b, mask_c, t);
                }
            }
        prev_masks = prev_masks.next;
        }

        cur_masks = delete_lower_MaskNodes(cur_masks, gcur_maxlengs[v]);

        return cur_masks;
}


/** (v-1) bitmasks of b,c */
    protected void optimize_v(long b, long c, int v) {
        int i, max_len, max_i, ll, t;
        long[] bbb = new long[8];
        long[] ccc = new long[8];

        ll = push_stack(b,c,v,bbb,ccc);

        max_len = max_i = 0;
        if (ll > 1) { 
        for (i=0; i<ll; ++i) {
            mask_b = bbb[i];
            mask_c = ccc[i];
            t = lenstra(v+1);
            if (t > max_len) {
                max_len = t;
                max_i = i;
            }
        }
        }

        if ( v >= www-1 ) {
            mask_b = bbb[max_i];
            mask_c = ccc[max_i];
            return;
        }

        optimize_v(bbb[max_i], ccc[max_i], v+1);
    }

    protected int push_stack(long b, long c, int v, long[] bbb, long[] ccc) {
        int i, ll, ncv;
        long[] cv_buf = new long[2];

        ll = 0;

        if( (v+shift_t) < www ){
            ncv = 2; cv_buf[0] = c | bitmask[v]; cv_buf[1] = c;
        }
        else {
            ncv = 1; cv_buf[0] = c;
        }

        for( i=0; i<ncv; ++i)
            ll += push_mask( ll, v, b, cv_buf[i], bbb, ccc);

        return ll;
    }

    protected int push_mask(int l, int v, long b, long c, long[] bbb, long[] ccc) {
        int i, j, k, nbv, nbvt;
        long bmask;
        long[] bv_buf = new long[2] ;
        long[] bvt_buf = new long[2];

        k = l;
        if( (shift_s+v) >= www ){
            nbv = 1; bv_buf[0] = 0;
        }
        else if( (v>=shift_t) && ((c&bitmask[v-shift_t]) != 0 ) ){
            nbv = 1; bv_buf[0] = b&bitmask[v];
        }
        else {
            nbv = 2; bv_buf[0] = bitmask[v]; bv_buf[1] = 0;
        }

        if( ((v+shift_t+shift_s) < www) && ((c&bitmask[v]) != 0) ){
            nbvt = 2; bvt_buf[0] = bitmask[v+shift_t]; bvt_buf[1] = 0;
        }
        else {
            nbvt = 1; bvt_buf[0] = 0;
        }

        bmask = bitmask[v];
        if( (v+shift_t) < www )
            bmask |= bitmask[v+shift_t];
        bmask = ~bmask;
        bmask &= MAX_VALUE;
        for( i=0; i<nbvt; ++i){
            for( j=0; j<nbv; ++j){
                bbb[k] = (b&bmask) | bv_buf[j] | bvt_buf[i];
                ccc[k] = c;
                ++k;
            }
        }

        return k-l;
    }


/**********************************/
/****  subroutines for lattice ****/
/**********************************/
    protected int lenstra(int v) {
        Vector[] lattice;
        Vector ltmp;
        int i, j, deg, max_row;

        upper_v_bits = 0;
        for( i=0; i<v; i++) {
            cur_bitmask[i] = bitmask[i];
            upper_v_bits |= bitmask[i];
        }

        lattice = make_lattice( v );

        i = -1; max_row=v;
        while( i<max_row ){ /* normalized until i-th row */

            pull_min_row( i+1, max_row, lattice );
            hakidasi( i+1, lattice);

            if( (lattice[i+1].bp & upper_v_bits) != 0 ) {
                pull_max( i+1, v, lattice[i+1] );
                ++i;
            }
            else {
                deg = degree_of_vector( lattice[i+1]); 

                if(deg==MIN_INFINITE){
                /* if deg==MIN_INFINITE, */ 
                /* exchange (i+1)-th row and v-th row */
                    ltmp = lattice[i+1]; lattice[i+1] = lattice[v];
                    lattice[v] = ltmp;
                    --max_row; 
                }
                else {
                    for( j=i; j>=0; j--){
                        if( lattice[j].degree <= deg ) break;
                    }
                    i = j;
                }
            }

        }

        i = lattice[max_row].degree;
        free_lattice( lattice, v );
        return -i;
    }



/********************************/
/** allocate momory for Vector **/
/********************************/
    protected Vector new_Vector() {
        Vector v = new Vector();

        v.cf = new long[nnn];// (uint32 *)calloc( nnn, sizeof( uint32 ) );

        v.start = 0;

        return v;
    }


/************************************************/
/* frees *v which was allocated by new_Vector() */
/************************************************/
/**
* Sets the given Vector to null
**/
    protected void free_Vector( Vector v ) {
        v = null;
    }

    protected void free_lattice( Vector[] lattice, int v) {
        int i;

        for( i=0; i<=v; i++)
            lattice[i] = null;
        lattice = null;
    }

/** Vector v is multiplied by t^k */
    protected Vector mult( Vector v, int k)  {
        int i, j, jmmm; /* jmmm = j + mmm */
        long tmp;
        Vector u;

        u = new_Vector();
        for( i=0; i<nnn; i++) u.cf[i] = v.cf[i]; /* copy v to u */
        u.start = v.start;
        u.degree = v.degree;
        u.bp = v.bp;
        u.z = v.z;

        if( k == 0 ) return u; /* if k==0, only v is copied to u  */

        j=u.start; 
        jmmm = j+mmm; /* note : jmmm < nnn+mmm < 2nnn */
        for(i=0; i<k; i++){

            if (j>=nnn) j -= nnn; /* same as j%=nnn */
            if (jmmm>=nnn) jmmm -= nnn; /* same as jmmm %= nnn */

            u.z = u.cf[j];
            tmp =  (u.cf[j]&gupper_mask) | (u.cf[(j+1)%nnn]&glower_mask) ;
            tmp = u.cf[jmmm] ^ ( (((tmp>>1) & MAX_VALUE) ^ aaa[(int)LSB(tmp)]) & MAX_VALUE );
            tmp &= MAX_VALUE;
            tmp &= greal_mask;
            u.cf[j] =  tmp;
            
            ++j;
            ++jmmm;
        }

        u.start += k; u.start %= nnn;

        /* integer part is shifted and bitmasked */
        tmp = u.z;
        tmp ^= ( (tmp>>shift_0) & greal_mask );//TRNSTMP;
        tmp &= MAX_VALUE;
        tmp ^= ( (tmp<<shift_s) & MAX_VALUE & mask_b);
        tmp &= MAX_VALUE;
        tmp ^= ( (tmp<<shift_t) & MAX_VALUE & mask_c);//MASKTMP;
        tmp &= MAX_VALUE;
        u.z = tmp;
        u.degree += k;

        return u;
    }

/** adds v to u (then u will change) */
    protected void add( Vector u, Vector v) {
        int i, stu, stv;

        stu = u.start; stv = v.start;
        for( i=0; i<nnn; i++){

        /*  0 <= stu,stv < 2*nnn always holds          **/
        /* so, modulo nnn can be calculated as follows **/
        if (stu>=nnn) stu -= nnn; /* same as stu %= nnn  */
        if (stv>=nnn) stv -= nnn; /* same as stv %= nnn  */

        u.cf[stu] ^= v.cf[stv];
        u.cf[stu] &= MAX_VALUE;
        stu++; stv++;
        }

        u.z ^=  v.z;
        u.z &= MAX_VALUE;
    }

/** returns the max degree of v */
    protected int degree_of_vector( Vector v) {
        int i,j,k;
        int immm; /* immm = i + mmm */
        long tmp;
        Vector u;

        if( (v.z & upper_v_bits) !=0 ){ /* if integer part is not 0 */
            v.bp = v.z;
            v.degree = 0;
            return 0;
        }

        for(k=v.start, j=0; j<nnn; j++,k++){

        if (k>=nnn) k -= nnn; /* same as k %= nnn (note 0<= k < 2*nnn) */

            tmp = v.cf[k];
        if (tmp != 0) {
            tmp ^= ( (tmp>>shift_0) & greal_mask );//TRNSTMP;
            tmp &= MAX_VALUE;
            tmp ^= ( (tmp<<shift_s) & MAX_VALUE & mask_b);
            tmp &= MAX_VALUE;
            tmp ^= ( (tmp<<shift_t) & mask_c);//MASKTMP;
            tmp &= MAX_VALUE;
            if( (tmp & upper_v_bits) != 0 ) {
                v.bp = tmp;
                v.degree = -(j+1);
                return -(j+1);
            }
        }
        }

        u = new_Vector(); /* copy v to u */
        for( j=0; j<nnn; j++) u.cf[j] = v.cf[j];
        u.z = v.z;
        u.start = v.start;


        k = nnn * (www-1) - rrr;
        i=u.start; 
        immm = i + mmm; /* note : immm < nnn+mmm < 2nnn */
        for(j=0; j<k; j++){ 

          /* i = (u.start + j) % nnn */
          if (i>=nnn) i -= nnn; /* same as i%=nnn: note always 0<=i<2*nnn */
          if (immm>=nnn) immm -= nnn; /* same as immm %= nnn */

          tmp = (u.cf[i]&gupper_mask) | (u.cf[(i+1)%nnn]&glower_mask);
          tmp = u.cf[immm] ^ ( (((tmp>>1) & MAX_VALUE) ^ aaa[(int)LSB(tmp)]) & MAX_VALUE );
          tmp &= MAX_VALUE;
          tmp  &=  greal_mask;
          u.cf[i] = tmp;

          if (tmp != 0) {
              tmp ^= ( (tmp>>shift_0) & greal_mask );//TRNSTMP;
              tmp &= MAX_VALUE;
              tmp ^= ( (tmp<<shift_s) & MAX_VALUE & mask_b);
              tmp &= MAX_VALUE;
              tmp ^= ( (tmp<<shift_t) & MAX_VALUE & mask_c);//MASKTMP;
              tmp &= MAX_VALUE;
              if( (tmp & upper_v_bits) != 0 ) {
                  v.bp = tmp;
                  v.degree = -(j+nnn+1);
                  free_Vector(u);
                  return -(j+nnn+1);
              }
          }
          
          ++i;
          ++immm;
        }

        free_Vector(u);
        v.bp = 0;
        v.degree = MIN_INFINITE;
        return MIN_INFINITE; /* if 0 inspite of  (nw-r) times of generation */
    }


/** add t^k*(i-th row) to j-th row */
    protected void add_i_to_j( Vector[] lattice, int i, int j, int k) {
        Vector ith;

        ith = mult( lattice[i], k);
        add( lattice[j], ith);
        free_Vector( ith );
    }

/** exchange columns so that i-th element of variable vec 
* gives the norm of vec */
    protected void pull_max( int i, int v, Vector vec) {
        int j;
        long tmp;

        if( (vec.bp & cur_bitmask[i]) != 0 ) return;

        for( j=i+1; j<v; j++){
            if( (vec.bp & cur_bitmask[j]) != 0 ){
                tmp = cur_bitmask[i];
                cur_bitmask[i] = cur_bitmask[j];
                cur_bitmask[j] = tmp;
                break;
            }
        }

    }

/** puts i-th row be the minimum one in i-th row ... v-th row */
    protected void pull_min_row( int i, int v, Vector[] lattice) {
        int j, min_deg, min_j;
        Vector vtmp;

        min_deg = lattice[i].degree;
        min_j = i;
        for( j=i+1; j<=v; j++){
            if( min_deg > lattice[j].degree ){
                min_deg = lattice[j].degree;
                min_j = j;
            }
        }

        vtmp = lattice[min_j]; lattice[min_j] = lattice[i];
        lattice[i] = vtmp;
    }

/** sweeps out k-th row with 0th ... (k-1)th rows */
    protected void hakidasi( int k, Vector[] lattice) {
        int i;

        for( i=0; i<k; i++){
            if( (lattice[k].bp & cur_bitmask[i]) != 0 ){
                  add_i_to_j( lattice, i, k, lattice[k].degree - lattice[i].degree);
                  lattice[k].bp ^= lattice[i].bp;
            }
        }
    }

/** makes a initial lattice */
    protected Vector[] make_lattice(int v) {
        int i;
        long tmp;
        Vector[] lattice;
        Vector top;

        lattice = new Vector[v+1];//(Vector **)malloc( (v+1) * sizeof( Vector *) );

        for( i=1; i<=v; i++){ /* from 1st row to v-th row */
            lattice[i] = new_Vector();
            lattice[i].z = bitmask[i-1];
            lattice[i].start = 0;
            lattice[i].bp = bitmask[i-1];
            lattice[i].degree = 0;
        }


        top = new_Vector(); /* 0th row */
        for(i=0; i<nnn; i++) {
          top.cf[i] = _genrand_dc();
          top.cf[i] &= greal_mask;
        }
        tmp = ( top.cf[0] & gupper_mask ) | ( top.cf[1] & glower_mask );
        top.cf[0] = top.cf[mmm] ^ ( (((tmp>>1) & MAX_VALUE) ^ aaa[(int)LSB(tmp)]) & MAX_VALUE );
        top.cf[0] &= MAX_VALUE;
        top.cf[0] &= greal_mask;
        top.z = 0; top.start = 1; 
        degree_of_vector( top );
        lattice[0] = top;

        return lattice;
    }

/***********/
    protected MaskNode cons_MaskNode(MaskNode head, long b, long c, int leng) {
        MaskNode t = new MaskNode();

        t.b = b;
        t.c = c;
        t.leng = leng;
        t.next = head;

        return t;
    }

    protected void delete_MaskNodes(MaskNode head) {
        MaskNode t;

        while(head != null) {
            t = head.next;
            head = null;
            head = t;
        }
    }

    protected MaskNode delete_lower_MaskNodes(MaskNode head, int l) {
        MaskNode s, t, tail;

        s = head;
        while(true) { /* heading */
            if (s == null)
                return null;
            if (s.leng >= l)
                break;
            t = s.next;
            s = null;
            s = t;
        }

        head = tail = s;
        
        while (head != null) {
            t = head.next;
            if (head.leng < l) {
                head = null;
            }
            else {
                tail.next = head;
                tail = head;
            }
            head = t;
        }
        
        tail.next = null;
        return s;
    }


    protected void show_distrib(MTS mts) {
        int i, lim, diff, t;
        double per;

        init_tempering(mts);
        
        mask_b = ((mts.maskB) << ggap) & MAX_VALUE;
        mask_c = ((mts.maskC) << ggap) & MAX_VALUE;
        for (i=0; i<www; i++) {
            t = lenstra(i+1);
            lim = (nnn*www-rrr)/(i+1);
            diff = lim  - t;
            per = (double)t / (double)lim;
            //printf ("%d %d %d %d %4.2f\n", i+1, t,  lim, diff, per);
        }
    }
/* init.c */

/* Copyright (C) 2001 Makoto Matsumoto and Takuji Nishimura.       */
/* This library is free software; you can redistribute it and/or   */
/* modify it under the terms of the GNU Library General Public     */
/* License as published by the Free Software Foundation; either    */
/* version 2 of the License, or (at your option) any later         */
/* version.                                                        */
/* This library is distributed in the hope that it will be useful, */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of  */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            */
/* See the GNU Library General Public License for more details.    */
/* You should have received a copy of the GNU Library General      */
/* Public License along with this library; if not, write to the    */
/* Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA   */ 
/* 02111-1307  USA                                                 */


    protected void init_dc(int seed) {
        _sgenrand_dc(seed);
    }
/* mt19937.c */

/* A C-program for MT19937: Integer version (1999/10/28)          */
/*  genrand() generates one pseudorandom unsigned integer (32bit) */
/* which is uniformly distributed among 0 to 2^32-1  for each     */
/* call. sgenrand(seed) sets initial values to the working area   */
/* of 624 words. Before genrand(), sgenrand(seed) must be         */
/* called once. (seed is any 32-bit integer.)                     */
/*   Coded by Takuji Nishimura, considering the suggestions by    */
/* Topher Cooper and Marc Rieffel in July-Aug. 1997.              */

/* This library is free software; you can redistribute it and/or   */
/* modify it under the terms of the GNU Library General Public     */
/* License as published by the Free Software Foundation; either    */
/* version 2 of the License, or (at your option) any later         */
/* version.                                                        */
/* This library is distributed in the hope that it will be useful, */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of  */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            */
/* See the GNU Library General Public License for more details.    */
/* You should have received a copy of the GNU Library General      */
/* Public License along with this library; if not, write to the    */
/* Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA   */ 
/* 02111-1307  USA                                                 */

/* Copyright (C) 1997, 1999 Makoto Matsumoto and Takuji Nishimura. */
/* When you use this, send an email to: matumoto@math.keio.ac.jp   */
/* with an appropriate reference to your work.                     */

/* REFERENCE                                                       */
/* M. Matsumoto and T. Nishimura,                                  */
/* "Mersenne Twister: A 623-Dimensionally Equidistributed Uniform  */
/* Pseudo-Random Number Generator",                                */
/* ACM Transactions on Modeling and Computer Simulation,           */
/* Vol. 8, No. 1, January 1998, pp 3--30.                          */


/* Period parameters */  
    public static final int N = 624;
    public static final int M = 397;
    public static final int MATRIX_A = 0x9908b0df;   /* constant vector a */
    public static final int UPPER_MASK = 0x80000000; /* most significant w-r bits */
    public static final int LOWER_MASK = 0x7fffffff; /* least significant r bits */

/* Tempering parameters */   
    public static final int TEMPERING_MASK_B  = 0x9d2c5680;
    public static final int TEMPERING_MASK_C = 0xefc60000;
    protected long TEMPERING_SHIFT_U(long y) {return  (y >> 11);}
    protected long TEMPERING_SHIFT_S(long y) {return ((y << 7) & MAX_VALUE);}
    protected long TEMPERING_SHIFT_T(long y) {return ((y << 15) & MAX_VALUE);}
    protected long TEMPERING_SHIFT_L(long y) {return (y >> 18);}

    protected long[] mt = new long[N]; /* the array for the state vector  */
    protected int mti=N+1; /* mti==N+1 means mt[N] is not initialized */

/** Initializing the array with a seed */
    protected void _sgenrand_dc(long seed) {
        int i;

        for (i=0;i<N;i++) {
             mt[i] = seed & 0xffff0000;
             seed = 69069 * seed + 1;
             seed = seed & MAX_VALUE;
             mt[i] |= (seed & 0xffff0000) >> 16;
             seed = 69069 * seed + 1;
             seed = seed & MAX_VALUE;
        }
        mti = N;
    }


    protected long _genrand_dc() {
        long y;
        long[] mag01 = new long[] {0x0, MATRIX_A};
        /* mag01[x] = x * MATRIX_A  for x=0,1 */

        if (mti >= N) { /* generate N words at one time */
            int kk;

            if (mti == N+1)   /* if sgenrand() has not been called, */
                _sgenrand_dc(4357); /* a default initial seed is used   */

            for (kk=0;kk<N-M;kk++) {
                y = (mt[kk]&UPPER_MASK)|(mt[kk+1]&LOWER_MASK);
                mt[kk] = (((mt[kk+M] ^ (y >> 1)) & MAX_VALUE) ^ mag01[(int)y & 0x1]) & MAX_VALUE;
            }
            for (;kk<N-1;kk++) {
                y = (mt[kk]&UPPER_MASK)|(mt[kk+1]&LOWER_MASK);
                mt[kk] = (((mt[kk+(M-N)] ^ (y >> 1)) & MAX_VALUE) ^ mag01[(int)y & 0x1]) & MAX_VALUE;
            }
            y = (mt[N-1]&UPPER_MASK)|(mt[0]&LOWER_MASK);
            mt[N-1] = (((mt[M-1] ^ (y >> 1)) & MAX_VALUE) ^ mag01[(int)y & 0x1]) & MAX_VALUE;

            mti = 0;
        }
      
        y = mt[mti++];
        y ^= TEMPERING_SHIFT_U(y);
        y &= MAX_VALUE;
        y ^= TEMPERING_SHIFT_S(y) & TEMPERING_MASK_B;
        y &= MAX_VALUE;
        y ^= TEMPERING_SHIFT_T(y) & TEMPERING_MASK_C;
        y &= MAX_VALUE;
        y ^= TEMPERING_SHIFT_L(y);
        y &= MAX_VALUE;

        return y; 
    }

/* prescr.c */

/* Copyright (C) 2001 Makoto Matsumoto and Takuji Nishimura.       */
/* This library is free software; you can redistribute it and/or   */
/* modify it under the terms of the GNU Library General Public     */
/* License as published by the Free Software Foundation; either    */
/* version 2 of the License, or (at your option) any later         */
/* version.                                                        */
/* This library is distributed in the hope that it will be useful, */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of  */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            */
/* See the GNU Library General Public License for more details.    */
/* You should have received a copy of the GNU Library General      */
/* Public License along with this library; if not, write to the    */
/* Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA   */ 
/* 02111-1307  USA                                                 */

/* example 

   ------------------------
   _InitPrescreening_dc(m,n,r,w)

   for(...)
      _prescrrening_dc(aaa)

   _EndPrescreening_dc()
   ------------------------
   _InitPrescreening_dc(),_EndPrescreening_dc() shoud be called once.
   Parameters (m,n,r,w) should not be changed.
*/


    public static final int NOT_REJECTED = 1;
    public static final int REJECTED = 0;


    public static final int LIMIT_IRRED_DEG = 31;
    public static final int NIRREDPOLY = 127;
    public static final int MAX_IRRED_DEG = 9;

    public static final int PRESCR_REDU = 1;//There is another REDU in check32 and seive which 
                                            // are both 0.
    public static final int NONREDU = 0;

/* list of irreducible polynomials whose degrees are less than 10 */
    protected int[][] irredpolylist = new int[][]/*[NIRREDPOLY][MAX_IRRED_DEG+1] =*/ {
    {0,1,0,0,0,0,0,0,0,0,},{1,1,0,0,0,0,0,0,0,0,},{1,1,1,0,0,0,0,0,0,0,},
    {1,1,0,1,0,0,0,0,0,0,},{1,0,1,1,0,0,0,0,0,0,},{1,1,0,0,1,0,0,0,0,0,},
    {1,0,0,1,1,0,0,0,0,0,},{1,1,1,1,1,0,0,0,0,0,},{1,0,1,0,0,1,0,0,0,0,},
    {1,0,0,1,0,1,0,0,0,0,},{1,1,1,1,0,1,0,0,0,0,},{1,1,1,0,1,1,0,0,0,0,},
    {1,1,0,1,1,1,0,0,0,0,},{1,0,1,1,1,1,0,0,0,0,},{1,1,0,0,0,0,1,0,0,0,},
    {1,0,0,1,0,0,1,0,0,0,},{1,1,1,0,1,0,1,0,0,0,},{1,1,0,1,1,0,1,0,0,0,},
    {1,0,0,0,0,1,1,0,0,0,},{1,1,1,0,0,1,1,0,0,0,},{1,0,1,1,0,1,1,0,0,0,},
    {1,1,0,0,1,1,1,0,0,0,},{1,0,1,0,1,1,1,0,0,0,},{1,1,0,0,0,0,0,1,0,0,},
    {1,0,0,1,0,0,0,1,0,0,},{1,1,1,1,0,0,0,1,0,0,},{1,0,0,0,1,0,0,1,0,0,},
    {1,0,1,1,1,0,0,1,0,0,},{1,1,1,0,0,1,0,1,0,0,},{1,1,0,1,0,1,0,1,0,0,},
    {1,0,0,1,1,1,0,1,0,0,},{1,1,1,1,1,1,0,1,0,0,},{1,0,0,0,0,0,1,1,0,0,},
    {1,1,0,1,0,0,1,1,0,0,},{1,1,0,0,1,0,1,1,0,0,},{1,0,1,0,1,0,1,1,0,0,},
    {1,0,1,0,0,1,1,1,0,0,},{1,1,1,1,0,1,1,1,0,0,},{1,0,0,0,1,1,1,1,0,0,},
    {1,1,1,0,1,1,1,1,0,0,},{1,0,1,1,1,1,1,1,0,0,},{1,1,0,1,1,0,0,0,1,0,},
    {1,0,1,1,1,0,0,0,1,0,},{1,1,0,1,0,1,0,0,1,0,},{1,0,1,1,0,1,0,0,1,0,},
    {1,0,0,1,1,1,0,0,1,0,},{1,1,1,1,1,1,0,0,1,0,},{1,0,1,1,0,0,1,0,1,0,},
    {1,1,1,1,1,0,1,0,1,0,},{1,1,0,0,0,1,1,0,1,0,},{1,0,1,0,0,1,1,0,1,0,},
    {1,0,0,1,0,1,1,0,1,0,},{1,0,0,0,1,1,1,0,1,0,},{1,1,1,0,1,1,1,0,1,0,},
    {1,1,0,1,1,1,1,0,1,0,},{1,1,1,0,0,0,0,1,1,0,},{1,1,0,1,0,0,0,1,1,0,},
    {1,0,1,1,0,0,0,1,1,0,},{1,1,1,1,1,0,0,1,1,0,},{1,1,0,0,0,1,0,1,1,0,},
    {1,0,0,1,0,1,0,1,1,0,},{1,0,0,0,1,1,0,1,1,0,},{1,0,1,1,1,1,0,1,1,0,},
    {1,1,0,0,0,0,1,1,1,0,},{1,1,1,1,0,0,1,1,1,0,},{1,1,1,0,1,0,1,1,1,0,},
    {1,0,1,1,1,0,1,1,1,0,},{1,1,1,0,0,1,1,1,1,0,},{1,1,0,0,1,1,1,1,1,0,},
    {1,0,1,0,1,1,1,1,1,0,},{1,0,0,1,1,1,1,1,1,0,},{1,1,0,0,0,0,0,0,0,1,},
    {1,0,0,0,1,0,0,0,0,1,},{1,1,1,0,1,0,0,0,0,1,},{1,1,0,1,1,0,0,0,0,1,},
    {1,0,0,0,0,1,0,0,0,1,},{1,0,1,1,0,1,0,0,0,1,},{1,1,0,0,1,1,0,0,0,1,},
    {1,1,0,1,0,0,1,0,0,1,},{1,0,0,1,1,0,1,0,0,1,},{1,1,1,1,1,0,1,0,0,1,},
    {1,0,1,0,0,1,1,0,0,1,},{1,0,0,1,0,1,1,0,0,1,},{1,1,1,1,0,1,1,0,0,1,},
    {1,1,1,0,1,1,1,0,0,1,},{1,0,1,1,1,1,1,0,0,1,},{1,1,1,0,0,0,0,1,0,1,},
    {1,0,1,0,1,0,0,1,0,1,},{1,0,0,1,1,0,0,1,0,1,},{1,1,0,0,0,1,0,1,0,1,},
    {1,0,1,0,0,1,0,1,0,1,},{1,1,1,1,0,1,0,1,0,1,},{1,1,1,0,1,1,0,1,0,1,},
    {1,0,1,1,1,1,0,1,0,1,},{1,1,1,1,0,0,1,1,0,1,},{1,0,0,0,1,0,1,1,0,1,},
    {1,1,0,1,1,0,1,1,0,1,},{1,0,1,0,1,1,1,1,0,1,},{1,0,0,1,1,1,1,1,0,1,},
    {1,0,0,0,0,0,0,0,1,1,},{1,1,0,0,1,0,0,0,1,1,},{1,0,1,0,1,0,0,0,1,1,},
    {1,1,1,1,1,0,0,0,1,1,},{1,1,0,0,0,1,0,0,1,1,},{1,0,0,0,1,1,0,0,1,1,},
    {1,1,0,1,1,1,0,0,1,1,},{1,0,0,1,0,0,1,0,1,1,},{1,1,1,1,0,0,1,0,1,1,},
    {1,1,0,1,1,0,1,0,1,1,},{1,0,0,0,0,1,1,0,1,1,},{1,1,0,1,0,1,1,0,1,1,},
    {1,0,1,1,0,1,1,0,1,1,},{1,1,0,0,1,1,1,0,1,1,},{1,1,1,1,1,1,1,0,1,1,},
    {1,0,1,0,0,0,0,1,1,1,},{1,1,1,1,0,0,0,1,1,1,},{1,0,0,0,0,1,0,1,1,1,},
    {1,0,1,0,1,1,0,1,1,1,},{1,0,0,1,1,1,0,1,1,1,},{1,1,1,0,0,0,1,1,1,1,},
    {1,1,0,1,0,0,1,1,1,1,},{1,0,1,1,0,0,1,1,1,1,},{1,0,1,0,1,0,1,1,1,1,},
    {1,0,0,1,1,0,1,1,1,1,},{1,1,0,0,0,1,1,1,1,1,},{1,0,0,1,0,1,1,1,1,1,},
    {1,1,0,1,1,1,1,1,1,1,},
};

    public static class Polynomial {
        int[] x; 
        int deg;
    } 

    protected int sizeofA; /* parameter size */
    protected long[][] modlist; //was static uint32 **modlist;
    protected Polynomial[] preModPolys;

    protected int _prescreening_dc(long aaa) {
    
        int i;

        for (i=0; i<NIRREDPOLY; i++) {
        if (IsReducible(aaa,modlist[i])==PRESCR_REDU) 
            return REJECTED;
        }
        return NOT_REJECTED;
    }

    protected void _InitPrescreening_dc(int m, int n, int r, int w) {
        int i;
        Polynomial pl;

        sizeofA = w;
        
        preModPolys = new Polynomial[sizeofA+1];
            //(Polynomial **)malloc((sizeofA+1)*(sizeof(Polynomial*)));
        MakepreModPolys(m,n,r,w);

        modlist = new long[NIRREDPOLY][];//(uint32**)malloc(NIRREDPOLY * sizeof(uint32*));
        for (i=0; i<NIRREDPOLY; i++) {
            modlist[i] = new long[sizeofA + 1];
                        //(uint32*)malloc( (sizeofA + 1) * (sizeof(uint32)) );
        }


        for (i=0; i<NIRREDPOLY; i++) {
            pl = NewPoly(MAX_IRRED_DEG);
            NextIrredPoly(pl,i); 
            makemodlist(pl, i);
            FreePoly(pl);
        }

        for (i=sizeofA; i>=0; i--) FreePoly(preModPolys[i]);
        preModPolys = null;

    }

    protected void _EndPrescreening_dc() {
        modlist = null;

    }

/*************************************************/
/******          static functions           ******/
/*************************************************/

    protected void NextIrredPoly(Polynomial pl, int nth) {
        int i, max_deg;
        
        for (max_deg=0,i=0; i<=MAX_IRRED_DEG; i++) {
        if ( irredpolylist[nth][i] != 0 ) 
            max_deg = i;
        pl.x[i] = irredpolylist[nth][i];
        }

        pl.deg = max_deg;

    }

    protected void makemodlist(Polynomial pl, int nPoly) {
        Polynomial tmpPl;
        int i;
        
        for (i=0; i<=sizeofA; i++) {
            tmpPl = PolynomialDup(preModPolys[i]);
            PolynomialMod(tmpPl,pl);
            modlist[nPoly][i] = word2bit(tmpPl);
            FreePoly(tmpPl);
        }
    }
   
/** Pack Polynomial into a word */
    protected long word2bit(Polynomial pl) {
        int i;
        long bx;

        bx = 0;
        for (i=pl.deg; i>0; i--) {
            if (pl.x[i] != 0) bx |= 1;
            bx <<= 1;
            bx &= MAX_VALUE;
        }
        if (pl.x[0] != 0) bx |= 1;
          
        return bx;
    }

/** REDU -- reducible 
 aaa = (a_{w-1}a_{w-2}...a_1a_0 */   
    protected int IsReducible(long aaa, long[] polylist) {
        int i;
        long x;

        x = polylist[sizeofA];
        for (i=sizeofA-1; i>=0; i--) {
            if ((aaa&1) != 0) {
                x ^= polylist[i];
                x &= MAX_VALUE;
            }
            aaa >>= 1;
        }

        if ( x == 0 ) return PRESCR_REDU;
        else return NONREDU;
    }
	  

/***********************************/
/**   functions for polynomial    **/
/***********************************/
    protected Polynomial NewPoly(int degree) {
        Polynomial p = new Polynomial();
        
        p.deg = degree;

        if (degree < 0) {
            p.x = null;
            return p;
        }
        
        p.x = new int[degree + 1];//(int *)calloc( degree + 1, sizeof(int));

        return p;
    }

    protected void FreePoly( Polynomial p) {
        p = null;
    }


/** multiplication **/
    protected Polynomial PolynomialMult(Polynomial p0,Polynomial p1) {
        int i, j;
        Polynomial p;

        /* if either p0 or p1 is 0, return 0 */
        if ( (p0.deg < 0) || (p1.deg < 0) ) {
            p = NewPoly(-1);
            return p;
        }

        p = NewPoly(p0.deg + p1.deg);
        for( i=0; i<=p1.deg; i++){
            if( p1.x[i] != 0 ){
                for( j=0; j<=p0.deg; j++){
                p.x[i+j] ^= p0.x[j];
                }
            }
        }

        return p;
    }

/** wara mod waru 
* the result is stored in wara ********/
    protected void PolynomialMod( Polynomial wara, final Polynomial waru) {
        int i;
        int deg_diff;

        while( wara.deg >= waru.deg  ){
            deg_diff = wara.deg - waru.deg;
            for( i=0; i<=waru.deg; i++){
                wara.x[ i+deg_diff ] ^= waru.x[i];
            }
            
            for( i=wara.deg; i>=0; i--){
                if( wara.x[i] != 0 ) break;
            }
            wara.deg=i;	
        
        }
    }

    protected Polynomial PolynomialDup(Polynomial pl) {
        Polynomial pt;
        int i;

        pt = NewPoly(pl.deg);
        for (i=pl.deg; i>=0; i--)
        pt.x[i] = pl.x[i];

        return pt;
    }

/** make the polynomial  "t**n + t**m"  **/
    protected Polynomial make_tntm( int n, int m) {
        Polynomial p;

        p = NewPoly(n);
        p.x[n] = p.x[m] = 1;

        return p;
    }

    protected void MakepreModPolys(int mm, int nn, int rr, int ww) {
        Polynomial t, t0, t1, s, s0, s1;
        int i,j;

        j = 0;
        t = NewPoly(0);
        t.deg = 0;
        t.x[0] = 1;
        preModPolys[j++] = t;

        t = make_tntm (nn, mm);
        t0 = make_tntm (nn, mm);
        s = make_tntm (nn-1, mm-1);

        for( i=1; i<(ww - rr); i++){
        preModPolys[j++] = PolynomialDup(t0);
        t1 = t0; 
        t0 = PolynomialMult(t0, t); 
        FreePoly(t1);
        }

        preModPolys[j++] = PolynomialDup(t0);

        s0 =PolynomialMult( t0, s);
        FreePoly(t0);	FreePoly(t);
        for( i=(rr-2); i>=0; i--){
        preModPolys[j++] = PolynomialDup(s0);
        s1 = s0; 
        s0 = PolynomialMult( s0, s); 
        FreePoly(s1);
        }
        
        preModPolys[j++] = PolynomialDup(s0);

        FreePoly(s0); FreePoly(s); 
    }

    /********************************/

    /* following functions are used for debuging */
/*
static void printPoly(Polynomial *p)
{
    int i;
    for (i=0; i<=p.deg; i++) {
	if (p.x[i] == 1) printf ("1");
	else if (p.x[i] == 0) printf ("0");
	else printf ("*");
    }
    printf("\n");
}

static void printPoly2(Polynomial *p)
{
    int i;
    for (i=0; i<=p.deg; i++) {
	if (p.x[i] == 1) printf ("%d ", i);
    }
    printf("\n");
}

static void printPoly3(Polynomial *p)
{
    int i,cnt;
    int startf;
    
    startf = 0;
    cnt = 0;
    for (i=0; i<=p.deg; i++) {
	if (p.x[i] == 1) {
	    if (startf) {
		if (i==1)
		    printf ("x");
		else
		    printf ("+x^%d", i);
	    }
	    else {
		if (i==0) printf ("1");
		else if (i==1) printf ("x");
		else printf ("x^%d", i);
		startf = 1;
	    }
	    cnt++;
	    if (cnt==10) {printf("\n");cnt=0;}
	}
    }
    printf("\n");
}

static void printuint32(uint32 x)
{
    int i;
    
    for (i=0; i<32; i++) {
	if (x&0x80000000U) printf ("1");
	else printf ("0");
	x <<= 1;
    }
    printf ("\n");
}

static void show_modlist(void)
{
    int i,j;

    for (i=0; i<NIRREDPOLY; i++)  {
	for (j=0; j<=sizeofA; j++)
	    printuint32(modlist[i][j]);
	getchar();
    }
}

/** addition **/
/*
static Polynomial *PolynomialSum( Polynomial *p0, Polynomial *p1)
{
    Polynomial *p, *pmin, *pmax;
    int i, maxdeg, mindeg;
    
    if ( p0.deg > p1.deg ) {
	pmax = p0;
	pmin = p1;
    }
    else {
	pmax = p1;
	pmin = p0;
    }
    maxdeg = pmax.deg;
    mindeg = pmin.deg;

    p = NewPoly(maxdeg);
    for (i=0; i<=maxdeg; i++)
	p.x[i] = pmax.x[i];
    for( i=0; i<=mindeg; i++)
	p.x[i] ^= pmin.x[i];
    
    for( i=p.deg; i>=0; i--){
	if( p.x[i] ) break;
    }
    p.deg=i;
    
    return p;
}

static Polynomial *chPoly(uint32 a)
{
    Polynomial *pl, *tmpP;
    int i;

    pl = PolynomialDup(preModPolys[sizeofA]);
    for (i=sizeofA-1; i>=0; i--) {
	if (a&1U) {
	    tmpP = PolynomialSum(pl, preModPolys[i]);
	    FreePoly(pl);
	    pl = tmpP;
	}
	a >>= 1;
    }
    
    return pl;
}


int main(void)
{
    int i,j,cnt;
    uint32 aaa;

    for (j=0; j<1000;j++) {
	InitPrescreening(11, 17, 23, 32);
		
	for (cnt=0,i=0; i<1000; i++) {
	    aaa = random();
	    aaa |= 0x80000000U;
	    if (NOT_REJECTED == prescreening(aaa)) {
		cnt++;
	    }
	}
	printf ("%d\n",cnt);
	
	EndPrescreening();
    }

}
*/

/* seive.c */

/* Copyright (C) 2001 Makoto Matsumoto and Takuji Nishimura.       */
/* This library is free software; you can redistribute it and/or   */
/* modify it under the terms of the GNU Library General Public     */
/* License as published by the Free Software Foundation; either    */
/* version 2 of the License, or (at your option) any later         */
/* version.                                                        */
/* This library is distributed in the hope that it will be useful, */
/* but WITHOUT ANY WARRANTY; without even the implied warranty of  */
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            */
/* See the GNU Library General Public License for more details.    */
/* You should have received a copy of the GNU Library General      */
/* Public License along with this library; if not, write to the    */
/* Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA   */ 
/* 02111-1307  USA                                                 */


    //public static final int WORDLEN = 32; defined in check32.c
    //public static final int LSB = 0x1; defined in check32.c
    public static final int MAX_SEARCH = 10000;


/********* prescreening function (prescr.c) *********/
    //public static final int NOT_REJECTED = 1;
    //public static final int REJECTED = 0;
/*******************************************************/

/************ deterministic seive (check32.c) ************/
//#define REDU 0
//#define IRRED 1
/************************************************************/



/* When idw==0, id is not embedded into "a" */
    public static final int FOUND = 1;
    public static final int NOT_FOUND = 0;
    protected int get_irred_param(MTS mts, int id, int idw) {
        int i;
        long a;

        for (i=0; i<MAX_SEARCH; i++) {
            if (idw == 0)
                a = nextA(mts.ww); 
            else
                a = nextA_id(mts.ww, id, idw); 
            if (NOT_REJECTED == _prescreening_dc(a) ) {
                if (IRRED == _CheckPeriod_dc(a,mts.mm,mts.nn,mts.rr,mts.ww)) {
                mts.aaa = a;
                break;
                }
            }
        }

        if (MAX_SEARCH == i) {
            log.severe("i= " + i + " which is equal to MAX_SEARCH (" + MAX_SEARCH + ")");
            return NOT_FOUND;
        }
        return FOUND;
    }


    protected long nextA(int w) {
        long x, word_mask;

        word_mask = ~(0x0);
        word_mask &= MAX_VALUE;
        word_mask <<= WORDLEN - w;
        word_mask &= MAX_VALUE;
        word_mask >>= WORDLEN - w;
        word_mask &= MAX_VALUE;
      
        x = _genrand_dc(); 
        x &= word_mask;
        x |= (LSB << (w-1)) & MAX_VALUE;

        return x;
    }

    protected long nextA_id(int w, int id, int idw) {
        long x, word_mask;

        word_mask = ~0;
        word_mask &= MAX_VALUE;
        word_mask <<= WORDLEN - w;
        word_mask &= MAX_VALUE;
        word_mask >>= WORDLEN - w;
        word_mask >>= idw;
        word_mask <<= idw;
        word_mask &= MAX_VALUE;

        x = _genrand_dc();
        x &= word_mask;
        x |= (LSB << (w-1)) & MAX_VALUE;
        x |= id; /* embedding id */

        return x;
    }

    protected void make_masks(int r, int w, MTS mts) {
        int i;
        long ut, wm, um, lm;

        wm = ~0;
        wm &= MAX_VALUE;
        wm >>= (WORDLEN - w);

        ut = 0;
        for (i=0; i<r; i++) {
            ut <<= 1;
            ut &= MAX_VALUE;
            ut |= LSB;
        }

        lm = ut;
        um = ((~ut) & MAX_VALUE) & wm;

        mts.wmask = wm;
        mts.umask = um;
        mts.lmask = lm;
    }

    protected MTS init_mt_search(int w, int p) {
        int n, m, r;
        MTS mts;
        
        if ( (w>32) || (w<31) ) {
            log.severe("Sorry, currently only w = 32 or 31 is allowded.\n");
            return null;
        }

        if ( proper_mersenne_exponent(p) == 0 ) {
        if (p<521) {
            log.severe("\"p\" is too small.\n");
            return null;
        }
        else if (p>44497){
            log.severe("\"p\" is too large.\n");
            return null;
        }
        else {
            log.severe("\"p\" is not a Mersenne exponent.\n");
            return null;
        }
        }

        n = p/w + 1; /* since p is Mersenne Exponent, w never divids p */
        mts = alloc_mt_struct(n);
        if (null == mts) return null;

        m = n/2;
        if (m < 2) m = n-1;
        r = n * w - p;

        make_masks(r, w, mts);
        _InitPrescreening_dc(m, n, r, w);
        _InitCheck32_dc(r, w);

        mts.mm = m;
        mts.nn = n;
        mts.rr = r;
        mts.ww = w;

        return mts;
    }

    protected void end_mt_search() {
        _EndPrescreening_dc();
    }

/* 
   w -- word size
   p -- Mersenne Exponent
*/
    protected MTS get_mt_parameter(int w, int p) {
        MTS mts;

        mts = init_mt_search(w, p);	
        if (mts == null) {
            log.severe("init_mt_search returned null");
            return null;
        }

        if ( NOT_FOUND == get_irred_param(mts,0,0) ) {
            free_mt_struct(mts);
            log.severe("get_irred_param returned NOT_FOUND");
            return null;
        }
        _get_tempering_parameter_hard_dc(mts);
        end_mt_search();

        return mts;
    }

/* 
   w -- word size
   p -- Mersenne Exponent
*/
    public static final int DEFAULT_ID_SIZE = 16;
/* id <= 0xffff */
    protected MTS get_mt_parameter_id(int w, int p, int id) {
        MTS mts;

        if (id > 0xffff) {
            log.severe("\"id\" must be less than 65536\n");
            return null;
        }
        if (id < 0) {
            log.severe("\"id\" must be positive\n");
            return null;
        }
        
        mts = init_mt_search(w, p);	
        if (mts == null) return null;
        
        if ( NOT_FOUND == get_irred_param(mts, id, DEFAULT_ID_SIZE) ) {
            free_mt_struct(mts);
            return null;
        }
        _get_tempering_parameter_hard_dc(mts);
        end_mt_search();
        
        return mts;
    }

    public MTS[] get_mt_parameters(int w, int p, int max_id) {
        MTS[] mtss;
        MTS template_mts;
        int bit_w, i, t;

        mtss = new MTS[max_id+1];//(mt_struct**)malloc(sizeof(mt_struct*)*(max_id+1));

        for (bit_w=0,t=max_id; (t != 0); bit_w++)
            t >>= 1;

        template_mts = init_mt_search(w, p);
        if (template_mts == null) {
            mtss = null;
            return null;
        }

        for (i=0; i<=max_id; i++) {
            mtss[i] = alloc_mt_struct(template_mts.nn);
            if (null == mtss[i]) {
                delete_mt_array(i,mtss);
                free_mt_struct(template_mts);
                end_mt_search();
                return null;
            }

            copy_params_of_mt_struct(template_mts, mtss[i]);

            if ( NOT_FOUND == get_irred_param(mtss[i],i,bit_w) ) {
                delete_mt_array(i+1, mtss);
                free_mt_struct(template_mts);
                end_mt_search();
                return null;
            }
            _get_tempering_parameter_hard_dc(mtss[i]);
        }

        free_mt_struct(template_mts);
        end_mt_search();
        return mtss;
    }

/* n : sizeof state vector */
    public MTS alloc_mt_struct(int n) {
        MTS mts;

        mts = new MTS();// (mt_struct*)malloc(sizeof(mt_struct));
        mts.state = new long[n];//(uint32*)malloc(n*sizeof(uint32));
        return mts;
    }

    public void free_mt_struct(MTS mts) {
        mts.state = null;
        mts = null;
    }

    public void delete_mt_array(int i, MTS[] mtss) {
        int j;

        for (j=0; j<i; j++) 
            mtss[i] = null;
        mtss = null;
    }

    public void copy_params_of_mt_struct(MTS src, MTS dst) {
        dst.nn = src.nn;
        dst.mm = src.mm;
        dst.rr = src.rr;
        dst.ww = src.ww;
        dst.wmask = src.wmask;
        dst.umask = src.umask;
        dst.lmask = src.lmask;
    }

    public int proper_mersenne_exponent(int p) {
        switch(p) {
            case 521:
            case 607:
            case 1279: 
            case 2203:
            case 2281:
            case 3217:
            case 4253:
            case 4423:
            case 9689:
            case 9941:
            case 11213:
            case 19937:
            case 21701:
            case 23209:
            case 44497:
            return 1;
            default:
            return 0;
        }
    }
}
