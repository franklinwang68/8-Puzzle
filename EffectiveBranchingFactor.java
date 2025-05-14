package HW1;

public class EffectiveBranchingFactor {
    
    public static double effectiveBranchingFactor(int N, int d) {
        if (d <= 0) {
            throw new IllegalArgumentException("Depth must be a positive integer.");
        }
        
        double low = 1.0, high = N, mid = 0.0;
        double epsilon = 1e-6;
        
        while (high - low > epsilon) {
            mid = (low + high) / 2.0;
            double sum = 0.0;
            double power = 1.0;
            
            for (int i = 0; i <= d; i++) {
                sum += power;
                power *= mid;
            }
            
            if (sum < N + 1) {
                low = mid;
            } else {
                high = mid;
            }
        }
        
        return (low + high) / 2.0;
    }
    
    public static void main(String[] args) {
        int N = 463234; // Number of nodes
        int D = 28;   // Depth
        double ebf = effectiveBranchingFactor(N, D);
        System.out.println("Effective Branching Factor: " + ebf);
    }
}