package findway;
import java.util.Arrays;
import java.util.Random;

public class TimDuongDi {
	int soDong, soCot;
	
	int data[][];
								       
								       
	Random rand = new Random();
	
	// Parameter
	int D;                    // n gen
	//range_of_gen = (0, D-1)           # range of gen
	int max_iter = 100;                   // Maximum Iterator
	int N = 300;                          // Population size
	int limit = 30;                       // scout Phase
	int trial[] = new int[N];           // initialize to 0
	float population[][];
	
	public TimDuongDi(int data[][], int D) {
		this.D = D;
		this.soCot = D;
		this.soDong = D;
		this.data = new int[D][D];
		this.population = new float[N][D];
		for(int i=0;i<D;i++)
		{
			for(int j=0;j<D;j++)
			{
				this.data[i][j] = data[i][j];
			}
		}
	}
	
	int round(float v) {
		
		int v_int = (int)v;
		if ((v - v_int) > 0.5)
			return v_int + 1;
					
		return v_int;
	}
	
	float fitness_ind(float ind[]) {
		
		int ind_c[] = new int[D];
		for (int i = 0; i < D; i++) {
			ind_c[i] = round(ind[i]);
		}
		float score = 0;
		
		for (int j = 0; j < D - 1; j++)
			score += data[ind_c[j]][ind_c[j+1]];
		
		score += data[ind_c[D-1]][ind_c[0]];
		
		
		int temp[] = ind_c.clone();
		
		Arrays.sort(temp);
		for (int j = 1; j < D; j++) {
			if (temp[j] == temp[j - 1])
				score += 9999;
		}
		
		return (float) (1/(score));
	}	
	
	float[] fitness_population(float population[][]) {
		
		float[] scores = new float[N];
		
		for (int i = 0; i < N; i++) {
			scores[i] = fitness_ind(population[i]);
		}
		
		return scores;
	}
	
	public int[] TimDuong()
	{
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < D; j++) {
				population[i][j] = rand.nextFloat() * (D-1);
			}
		}
		
//		for (int i = 0; i < N; i++) {
//			for (int j = 0; j < D; j++) {
//				System.out.print(Math.round(population[i][j]) + " ");
//			}
//			System.out.println();
//		}
		
		float fx[] = fitness_population(population);
		
		float losses[] = new float[max_iter];
		
		for (int it = 0; it < max_iter; it++) {
			

			
			// Employed bee Phase
			for (int i = 0; i < N; i++) {

				float x_new[] = population[i].clone();
				int var = rand.nextInt(D);
				int p = rand.nextInt(N);
				
				x_new[var] += (rand.nextFloat() * 2 - 1) * (x_new[var] - population[p][var]);
				
				if (x_new[var] < 0)
					x_new[var] = 0;
				if (x_new[var] > D - 1)
					x_new[var] = D - 1;

				
				float f_new = fitness_ind(x_new);

				if (f_new > fx[i]) {
		            population[i] = x_new;
		            trial[i] = 0;
		            fx[i] = f_new;
				}
		        else
		            trial[i] += 1;
			}
			
			
			// Onlooker phase
			float s = 0;
			for (int i = 0; i < N; i++)
				s += fx[i];

			
			float prob[] = new float[N];
			for (int i = 0; i < N; i++) 
				prob[i] = fx[i]/s;
			
			
			for (int i = 0; i < N; i++) {
		        if (prob[i] > rand.nextFloat()) {
		            
		        	float x_new[] = population[i].clone();
					int var = rand.nextInt(D);
					int p = rand.nextInt(N);
					
					x_new[var] += (rand.nextFloat() * 2 - 1) * (x_new[var] - population[p][var]);
					
					if (x_new[var] < 0)
						x_new[var] = 0;
					if (x_new[var] > D - 1)
						x_new[var] = D - 1;
					
					float f_new = fitness_ind(x_new);
					
					if (f_new > fx[i]) {
			            population[i] = x_new;
			            trial[i] = 0;
			            fx[i] = f_new;
					}
			        else
			            trial[i] += 1;
		    
		        }
			}
			
			//Scout Phase
			for (int i = 0; i < N; i++) {
				if (trial[i] > limit){
					
					float temp[] = new float[D];
					for (int j = 0; j < D; j++) {
						temp[j] = rand.nextFloat() * (D-1);
					}
					population[i] = temp;
					fx[i] = fitness_ind(temp);
					trial[i] = 0;
				}
			} 
		}
		
		
		fx = fitness_population(population);
		
		int i_max = 0;
		for (int i = 1; i < N; i++) {
			if (fx[i] > fx[i_max])
				i_max = i;
//			System.out.println(1/fx[i]);
		}
		
		int value[] = new int[D];
		for (int i = 0; i < D; i++) {
			value[i] = round(population[i_max][i]);
		}
		return value;
	}
}
