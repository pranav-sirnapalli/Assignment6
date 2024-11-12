package utils.compression;

import java.util.Arrays;

/**
 * Helper class for compression.
 */
public class HaarTransform {

  /**
   * method for transformation of double values.
   * @param sequence input is a double seq.
   * @return returns a result.
   */
  public static double[] haarWaveletTransform(double[] sequence) {
    int reqlen = sequence.length;
    double[] netRes = Arrays.copyOf(sequence, reqlen);

    while (reqlen > 1) {
      double[] avgDiff = new double[reqlen];
      for (int i = 0; i < reqlen; i += 2) {
        avgDiff[i / 2] = (netRes[i] + netRes[i + 1]) / Math.sqrt(2);
        avgDiff[reqlen / 2 + i / 2] = (netRes[i] - netRes[i + 1]) / Math.sqrt(2);
      }
      System.arraycopy(avgDiff, 0, netRes, 0, reqlen);
      reqlen /= 2;
    }
    return netRes;
  }

  /**
   * method for inverse trnasformation of double values.
   * @param transformedSequence input is a double seq.
   * @return reutrns a result.
   */
  public static double[] invHaarWaveTransf(double[] transformedSequence) {
    int length = transformedSequence.length;
    double[] result = Arrays.copyOf(transformedSequence, length);
    int m = 2;

    while (m <= length) {
      double[] originalSeq = new double[m];
      for (int i = 0; i < m / 2; i++) {
        double avg = result[i];
        double diff = result[m / 2 + i];
        originalSeq[2 * i] = (avg + diff) / Math.sqrt(2);
        originalSeq[2 * i + 1] = (avg - diff) / Math.sqrt(2);
      }
      System.arraycopy(originalSeq, 0, result, 0, m);
      m *= 2;
    }
    return result;
  }

  /**
   * method for haar2D transformation.
   * @param resMatrix is the input for the matrix.
   * @return returns a matrix.
   */
  public static double[][] haarWaveTransf2D(double[][] resMatrix) {
    int size = resMatrix.length;

    for (int i = 0; i < size; i++) {
      resMatrix[i] = haarWaveletTransform(resMatrix[i]);
    }

    for (int j = 0; j < size; j++) {
      double[] column = new double[size];
      for (int i = 0; i < size; i++) {
        column[i] = resMatrix[i][j];
      }
      column = haarWaveletTransform(column);
      for (int i = 0; i < size; i++) {
        resMatrix[i][j] = column[i];
      }
    }

    return resMatrix;
  }

  /**
   * Method for invHaar2d transform.
   * @param resMatrix input which is a double matrix.
   * @return return a result.
   */
  public static double[][] invHaarWaveTransf2D(double[][] resMatrix) {
    int size = resMatrix.length;

    for (int j = 0; j < size; j++) {
      double[] column = new double[size];
      for (int i = 0; i < size; i++) {
        column[i] = resMatrix[i][j];
      }
      column = invHaarWaveTransf(column);
      for (int i = 0; i < size; i++) {
        resMatrix[i][j] = column[i];
      }
    }

    for (int i = 0; i < size; i++) {
      resMatrix[i] = invHaarWaveTransf(resMatrix[i]);
    }

    return resMatrix;
  }
}

