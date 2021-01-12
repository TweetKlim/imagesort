package sorting.visualizer.sorting;

public class MasterSorter {
	
	public static boolean sort(Pixel[] pixels, SortingMethod method, int iterations) {
		int n = pixels.length;
		int g;
		for (int inc = n / 2; inc > 0; inc /= 2)
		{
			g = n % inc;
			for (int numb = 0; numb < inc; ++numb)
			{
				Pixel[] p;
				if(g > numb)
				{
					p = new Pixel[n/inc+1];
				}
				else 
				{
					p = new Pixel[n/inc];
				}

				int t = 0;
				for (int i = numb; i < n; i += inc)
				{
					p[t] = pixels[i];
					t++;
				}
				for(int i = 0; i < p.length; i++) {
					for (int j = i;j > 0; j --)
					{
						Pixel p1 = pixels[i-j];
						Pixel p2 = pixels[j];
						if(p1.id > p2.id) {
							p[i-j] = p2;
							p[j] = p1;
						}
					}
				}
				t =0;
				for (int i = numb; i < n; i += inc)
				{
					pixels[i]=p[t];
					t++;
				}
			}
		}
		
		return isSorted(pixels);
	}
	
	public static boolean isSorted(Pixel[] pixels) {
		for(int i = 0; i < pixels.length; i++) {
			Pixel pixel = pixels[i];
			if(i != pixel.id) return false;
		}
		return true;
	}

}
