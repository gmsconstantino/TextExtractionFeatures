import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans {

  private static final int REPLICATION_FACTOR = 1;
  private static final int CHAR_FACTOR = 50;
  
  private static final int[] NPRIMES = new int[] {2,3,5,7,11,13,17,19,23,29,31,37,41,43,
		  47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,
		  157,163,167,173,179,181,191,193,197,199,211,223,227,229,233,239,241,251,257,
		  263,269,271,277,281,283,293,307,311,313,317,331,337,347,349,353,359,367,373,
		  379,383,389,397,401,409,419,421,431,433,439,443,449,457,461,463,467,479,487,
		  491,499,503,509,521,523,541,547,557,563,569,571,577,587,593,599,601,607,613,
		  617,619,631,641,643,647,653,659};

  public static class Point50D {
      
      private char[] cood;
      
      public Point50D() {
    	  cood = new char[CHAR_FACTOR];
      }
      
      public Point50D(Point50D point) {
          this.cood = Arrays.copyOf(point.cood, point.cood.length);
      }
      
      public Point50D(char[] point) {
          this.cood = point;
      }
      
      private double getDistance(Point50D other) {
    	  double diff = 0;
    	  for(int i=0; i< CHAR_FACTOR; i++) {
    		  diff += Math.pow(  this.cood[i] -  other.cood[i] , 2);
//    		  diff += Math.pow(  Math.pow(2, i) * this.cood[i] -  Math.pow(2, i) * other.cood[i] , 2);
//    		  diff += Math.pow(  NPRIMES[i] * this.cood[i] -  NPRIMES[i] * other.cood[i] , 2);
    	  }
    	  
          return Math.sqrt(diff);
      }
      
      public int getNearestPointIndex(List<Point50D> points) {
          int index = -1;
          double minDist = Double.MAX_VALUE;
          for (int i = 0; i < points.size(); i++) {
              double dist = this.getDistance(points.get(i));
              if (dist < minDist) {
                  minDist = dist;
                  index = i;
              }
          }
          return index;
      }
      
      public static Point50D getMean(List<Point50D> points) {
          if (points.size() == 0) return new Point50D();
          
          Point50D r = new Point50D();
          
    	  for(int i=0; i< CHAR_FACTOR; i++) {
    		  double sum = 0;
    		  for (Point50D point : points) {
        		  sum += point.cood[i];
        	  }
    		  r.cood[i] = (char) (sum / points.size());
          }
          return r;
      }
      
      

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(cood);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point50D other = (Point50D) obj;
		if (!Arrays.equals(cood, other.cood))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + new String(cood) + "]";
	}  
      
  }
  
  static List<List<Point50D>> clusters;

  public static List<Point50D> getDataset(String inputFile) throws Exception {
      List<Point50D> dataset = new ArrayList<>();
      BufferedReader br = new BufferedReader(new FileReader(inputFile));
      String line;
      while ((line = br.readLine()) != null) {
          char[] ss = line.toCharArray();
          char[] newArray = new char[CHAR_FACTOR];
          System.arraycopy(ss, 0, newArray, 0, ss.length);
          
          
          Point50D point = new Point50D(newArray);
          
          for (int i = 0; i < REPLICATION_FACTOR; i++)
              dataset.add(point);
      }
      br.close();
      return dataset;
  }
  
  public static List<Point50D> initializeRandomCenters(int n, int lowerBound, int upperBound) {
      List<Point50D> centers = new ArrayList<>(n);
      for (int i = 0; i < n; i++) {
    	  
    	  Point50D point = new Point50D();
    	  for(int j = 0; j< CHAR_FACTOR; j++)
    		  point.cood[j] = (char)(Math.random() * (upperBound - lowerBound) + lowerBound);

          
          centers.add(point);
      }
      return centers;
  }
  
  public static List<Point50D> initializeSampleCenters(int n, List<Point50D> data) {
      List<Point50D> centers = new ArrayList<>(n);
//      Random generator = new Random(234);
      
      for (int i = 0; i < n; i++) {	  
    	  Point50D point = new Point50D( data.get((int)(Math.random() * data.size())) );
//    	  Point50D point = new Point50D( data.get( generator.nextInt(data.size()) ) );
          centers.add(point);
      }
      return centers;
  }

  public static List<Point50D> getNewCenters(List<Point50D> dataset, List<Point50D> centers) {
      List<List<Point50D>> clusters = new ArrayList<>(centers.size());
      for (int i = 0; i < centers.size(); i++) {
          clusters.add(new ArrayList<Point50D>());
      }
      for (Point50D data : dataset) {
          int index = data.getNearestPointIndex(centers);
          clusters.get(index).add(data);
      }
      List<Point50D> newCenters = new ArrayList<>(centers.size());
      for (List<Point50D> cluster : clusters) {
          newCenters.add(Point50D.getMean(cluster));
      }
      KMeans.clusters = clusters;
      
      return newCenters;
  }
  
  public static double getDistance(List<Point50D> oldCenters, List<Point50D> newCenters) {
      double accumDist = 0;
      for (int i = 0; i < oldCenters.size(); i++) {
          double dist = oldCenters.get(i).getDistance(newCenters.get(i));
          accumDist += dist;
      }
      return accumDist;
  }
  
  public static List<Point50D> kmeans(List<Point50D> centers, List<Point50D> dataset, int k) {
      boolean converged;
      do {
          List<Point50D> newCenters = getNewCenters(dataset, centers);
          double dist = getDistance(centers, newCenters);
          centers = newCenters;
          converged = dist == 0;
      } while (!converged);
      return centers;
  }
  
  public static double[] getDistanctFromCenter(Point50D point, List<Point50D> centers) {
	  double[] distances = new double[centers.size()];
	  
	  for(int i = 0; i < centers.size(); i++) {
		  distances[i] = point.getDistance(centers.get(i));
	  }
	  
	  return distances;	  
  }
  
  public static String MytoString(double[] v)
  {
    if (v == null)
      return "null";
    StringBuilder b = new StringBuilder("[");
    for (int i = 0; i < v.length; ++i)
      {
    if (i > 0)
      b.append(" | ");
    b.append(i+"="+ String.format("%,10.1f", v[i]) );
      }
    b.append("]");
    return b.toString();
  }

  public static void main(String[] args) {

      String inputFile = "data/Description_unique.txt";
//      String inputFile = "data/Description.txt";
//      String inputFile = "Teste.txt";
      int k = 10;
      List<Point50D> dataset = null;
      try {
          dataset = getDataset(inputFile);
      } catch (Exception e) {
          System.err.println("ERROR: Could not read file " + inputFile);
          System.exit(-1);
      }
//      List<Point50D> centers = initializeRandomCenters(k, 0, 254);
      List<Point50D> centers = initializeSampleCenters(k, dataset);
      for(int i = 0; i < centers.size(); i++) {
    	  System.out.println(i+"="+centers.get(i));
      }
      
      List<Point50D> Ncenters = centers;
      
      int n = 1;
      for (int m = 0; m < n; m++) {
    	  System.out.println("=========== Run "+m+"==============");
	      long start = System.currentTimeMillis();
	      Ncenters = kmeans(Ncenters, dataset, k);
	      System.out.println("Time elapsed: " + (System.currentTimeMillis() - start) + "ms");
	      
	      List<List<Point50D>> clusters = KMeans.clusters;
	      
	      
	      System.out.println();
	      for (int i=0; i<k; i++) {
	    	  System.out.println("Cluster "+i);
	    	  
	    	  List<Point50D> cluster = clusters.get(i);
	    	  for(Point50D p: cluster)
	    		  System.out.println("\t"+p.toString() + " " + MytoString(getDistanctFromCenter(p, Ncenters)));
	    	  
	      }
	      
	      System.out.println("Centers");
	      for(int i = 0; i < Ncenters.size(); i++) {
	    	  System.out.println((i<10?" ":"")+i+"="+Ncenters.get(i));
	      }
	      System.out.println();
	      System.out.println("=======================================");
	      System.out.println();
      
      }
      
  }

}