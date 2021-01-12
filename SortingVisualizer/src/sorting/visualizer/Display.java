package sorting.visualizer;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import sorting.visualizer.graphics.Picture;
import sorting.visualizer.sorting.MasterSorter;
import sorting.visualizer.sorting.Pixel;
import sorting.visualizer.sorting.SortingMethod;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private Thread thread;
	private static String title = "Sorting Visualizer";
	private int width = 2000;
	private int height = 1000;
	private boolean running = false, sorted = false;
	private Picture picture;
	private int iter =0;
	private BufferedImage image;
	private int[] pixels;
	private Pixel[] myPixels;
	private Pixel[][] myPixels1;

	public Display() {
		frame = new JFrame();
		
		picture = new Picture("/images/doby.jpg");
		
		width = picture.getWidth();
		height = picture.getHeight();
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		myPixels = new Pixel[width * height];
		myPixels1 = new Pixel[height][width];
		Dimension size = new Dimension(400, (int) (400.0/width * height));
		this.setPreferredSize(size);

		initPixels();
		initPixels1();
	}
	
	private void initPixels() {
		for(int i = 0; i < pixels.length; i++) {
			myPixels[i] = new Pixel(picture.getPixels()[i], i);
		}
		randomizePixels();
	}
	private void initPixels1() {
		for(int z = 0; z < myPixels1.length; z++) {
			for(int i = 0; i < myPixels1[z].length; i++) {
				myPixels1[z][i] = new Pixel(picture.getPixels()[z*width+i], i);

			}
		}
		randomizePixels1();
	}
	
	private void randomizePixels() {
		ArrayList<Pixel> pixelList = new ArrayList<Pixel>();
		
		for(int i = 0; i < myPixels.length; i++) {
			pixelList.add(myPixels[i]);
		}
		
		Collections.shuffle(pixelList);
		
		for(int i = 0; i < myPixels.length; i++) {
			myPixels[i] = pixelList.get(i);
		}
	}
	
	private void randomizePixels1() {
		for(int j = 0;j< myPixels1.length;j++)
		{
			ArrayList<Pixel> pixelList = new ArrayList<Pixel>();
			
			for(int i = 0; i < myPixels1[j].length; i++) {
				pixelList.add(myPixels1[j][i]);
			}
			
			Collections.shuffle(pixelList);
			
			for(int i = 0; i < myPixels1[j].length; i++) {
				myPixels1[j][i] = pixelList.get(i);
			}
		}
	}
	public static void main(String[] args) {
		Display display = new Display();
		display.frame.setTitle(title);
		display.frame.add(display);
		display.frame.pack();
		display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.frame.setLocationRelativeTo(null);
		display.frame.setVisible(true);
		
		display.start();
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static boolean isSorted(Pixel[] pix) {
		for(int i = 0; i < pix.length; i++) {
			Pixel p = pix[i];
			if(i != p.id) return false;
		}
		return true;
	}
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 400;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		int shot =0;
		render(1);
		while(!isSorted(myPixels)) {
			int n = myPixels.length;
			int g;
			for (int inc = n / 2; inc > 0; inc /= 2)
			{
				g = n % inc;
				for (int numb = 0; numb < inc; ++numb)
				{
					long now = System.nanoTime();
					delta += (now - lastTime) / ns;
					lastTime = now;
							for (int i = numb + inc; i < n; i += inc)
							{
								for (int j = i;j-inc >= 0 && myPixels[j - inc].id > myPixels[j].id && j > numb; j -= inc)
								{
									Pixel a = myPixels[j - inc];
									myPixels[j - inc] = myPixels[j];
									myPixels[j] = a;
									shot++;
									if(shot%6000000 == 0)render(shot/6000000);
								}
							}

					
					frames++;
					/*try {
					      Thread.sleep(10);
					} catch (InterruptedException e) {}*/
					if(System.currentTimeMillis() - timer >= 1000) {
						timer += 1000;
						frame.setTitle(title + " | " + frames + " fps");
						frames = 0;
						updates = 0;
					}
					
				}
			}
			}	
		
		render(shot/6000000+1);
	}

	public void run1() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 400;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		int shot = 0;
		int phot = 0;
		boolean flag = true;
		while(!isSorted(myPixels1[0])) {
			int n = myPixels1[0].length;
			int g;
			for (int inc = n / 2; inc > 0; inc /= 2)
			{
				g = n % inc;
				for (int numb = 0; numb < inc; ++numb)
				{
					long now = System.nanoTime();
					delta += (now - lastTime) / ns;
					lastTime = now;
							for (int i = numb + inc; i < n; i += inc)
							{
								for (int j = i;j-inc >= 0  && j > numb; j -= inc)
								{
									int z = 0;
									for(;z < myPixels1.length;z++)
									{
										if(myPixels1[z][j - inc].id > myPixels1[z][j].id)
									
										{
											Pixel a = myPixels1[z][j - inc];
											myPixels1[z][j - inc] = myPixels1[z][j];
											myPixels1[z][j] = a;
											
										}
										 
									}
									shot++;
									if(shot%(10*(n/inc)) == 0)
										{
											render1(phot);
											phot++;
										}

									System.out.println(shot);
								
								}

							}
					
					frames++;
					/*try {
					      Thread.sleep(10);
					} catch (InterruptedException e) {}*/
					if(System.currentTimeMillis() - timer >= 1000) {
						timer += 1000;
						frame.setTitle(title + " | " + frames + " fps");
						frames = 0;
						updates = 0;
					}
					
				}
			}
			}	
		
		
	}
	private void render(int shot) {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		for(int i = 0; i < myPixels.length; i++) {
				pixels[i] = myPixels[i].color;
		}
		try {
		    File outputfile = new File("C://Users/klim/IdeaProjects/SortingVisualizer2/res/images/shot","saved"+Integer.toString(shot)+".jpg");
		    ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	private void render1(int shot) {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		for(int i = 0; i < myPixels1[0].length; i++) {
			for(int z = 0; z < myPixels1.length; z++) {
				pixels[z * width + i] = myPixels1[z][i].color;
			}
		}
		try {
		    File outputfile = new File("C://Users/klim/IdeaProjects/SortingVisualizer2/res/images/shot1","saved"+Integer.toString(shot)+".png");
		    ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	private void update() {
		if(!sorted) {
			sorted = MasterSorter.sort(myPixels, SortingMethod.BubbleSort, iter);
		}
	}

}
