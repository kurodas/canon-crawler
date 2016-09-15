package main;

public class Camera{
	double price;
	Category category;
	String name;
	String imageURL;
	String productURL;
	
	public Camera(String name, Category category, double price, String imageURL, String productURL){
		this.price = price;
		this.category = category;
		this.name = name;
		this.imageURL = imageURL;
		this.productURL = productURL;
	}
	
	public void printDetails(){
		System.out.println();
		System.out.println("Name: " + name);
		System.out.println("Camera Category: " + category);
		if(price > 0)
			System.out.printf("Price: %.2f\n", price);
		else
			System.out.println("Price: unavailable");
		System.out.println("Product URL: " + productURL);
		System.out.println("Image URL: " + imageURL);
	}
	
	
}
