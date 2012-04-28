package scopelite;

/**
 *
 * @author http://blog.datasingularity.com/?p=53
 */
public class FFT {
    public double[] four1(double data[], int nn, int isign) {

        int i, j, n, mmax, m, istep;
        double wtemp, wr, wpr, wpi, wi, theta, tempr, tempi;

        n = nn << 1;
        j = 1;
        for(i = 1; i < n; i += 2) {
            if(j > i) {
            double temp;
            temp = data[j];
            data[j] = data[i];
            data[i] = temp;
            temp = data[j+1];
            data[j+1] = data[i+1];
            data[i+1] = temp;
            }
            m = n >> 1;
            while(m >= 2 && j > m) {
            j -= m;
            m >>= 1;
            }
            j += m;
        }
        mmax = 2;
        while(n > mmax) {
            istep = (mmax << 1);
            theta = (isign*(6.28318530717959/mmax));
            wtemp = java.lang.Math.sin(0.5*theta);
            wpr = -2.0*wtemp*wtemp;
            wpi = java.lang.Math.sin(theta);
            wr = 1.0;
            wi = 0.0;
            for(m = 1; m < mmax; m += 2) {
            for(i = m; i <= n; i += istep) {
                j = i+mmax;
                tempr = wr*data[j]-wi*data[j+1];
                tempi = wr*data[j+1]+wi*data[j];
                data[j] = data[i] - tempr;
                data[j+1] = data[i+1] - tempi;
                data[i] += tempr;
                data[i+1] += tempi;
            }
            wr = (wtemp=wr)*wpr-wi*wpi+wr;
            wi = wi*wpr+wtemp*wpi+wi;
            }
            mmax = istep;
        }
        return data;
    }
}
