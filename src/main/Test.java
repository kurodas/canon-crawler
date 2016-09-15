package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	static int limit = 2;

	public static void main(String[] args) throws IOException {
		String startingURL = "https://shop.usa.canon.com/shop/en/catalog/cameras";
		Document homePage = Jsoup.connect(startingURL).timeout(0).get();
		Set<String> URLSet = new HashSet<String>();
		Set<String> crawledURLs = new HashSet<String>();
		List<Camera> cameraList = new ArrayList<Camera>();
		getURLsFromDocument(homePage, URLSet);
		int counter = 0;
		while (!URLSet.isEmpty() && counter < limit) {
			System.out.println("Starting next set");
			Set<String> nextSet = new HashSet<String>();
			for (String url : URLSet) {
				if (!crawledURLs.contains(url)) {
					System.out.println(url);
					Document nextPage = Jsoup.connect(url).timeout(0).get();
					getURLsFromDocument(nextPage, nextSet);
					//Check if page is a product page
					if (nextPage.getElementById("WC_BreadCrumb_Link_3_1_1551_76303") != null) {
						Camera nextCamera = parseCameraDetails(nextPage, url);
						if(nextCamera!= null){
							cameraList.add(nextCamera);
							nextCamera.printDetails();
						}
					}
					crawledURLs.add(url);
				}
			}
			System.out.println(nextSet.size());
			URLSet = nextSet;
			counter++;
		}
		for (Camera camera : cameraList) {
			camera.printDetails();
		}
	}
//	public static void main(String[] args) throws IOException {
//		String startingURL = "https://shop.usa.canon.com/shop/en/catalog/eos-7d-mark-ii-body";
//		Document homePage = Jsoup.connect(startingURL).timeout(0).get();
//		parseCameraDetails(homePage);
//	}

	private static Set<String> getURLsFromDocument(Document webpage, Set<String> URLSet) {
		Elements links = webpage.getElementsByTag("a");
		// Set<String> URLSet = new HashSet<String>();
		for (Element link : links) {
			String linkURL = link.attr("href");
			if (linkURL.contains("/shop/en/catalog")) {
				// System.out.println(linkURL);
				if (!linkURL.contains("shop.usa.canon.com")) {
					String fullURL = "https://shop.usa.canon.com" + linkURL.trim();
					URLSet.add(fullURL);
				} else {
					URLSet.add(linkURL);
				}
			}
		}
		return URLSet;
	}

	private static Camera parseCameraDetails(Document cameraPage, String productURL) {
		Elements nameElement = cameraPage.getElementsByAttributeValue("itemprop", "name");
		String name = "";
		if(nameElement != null){
			name = nameElement.text();
		}		
		String category = cameraPage.getElementById("WC_BreadCrumb_Link_3_1_1551_76303").text();
		Category cameraCategory = null;
		switch (category.toUpperCase().trim()) {
		case "EOS DIGITAL SLR CAMERAS":
			cameraCategory = Category.EOS_DSLR;
			break;
		case "EOS M SERIES DIGITAL CAMERAS":
			cameraCategory = Category.EOS_M;
			break;
		case "EOS DIGITAL SLR CAMERA BUNDLES":
			cameraCategory = Category.EOS_DSLR_BUNDLES;
			break;
		case "POWERSHOT ADVANCED PERFORMANCE - G & S SERIES DIGITAL CAMERAS":
			cameraCategory = Category.POWERSHOT_ADVANCED_PERFORMANCE;
			break;
		case "POWERSHOT LONG ZOOM - SX SERIES DIGITAL CAMERAS":
			cameraCategory = Category.POWERSHOT_LONG_ZOOM;
			break;
		case " POWERSHOT NEW CONCEPT - N SERIES DIGITAL CAMERAS":
			cameraCategory = Category.POWERSHOT_NEW_CONCEPT;
			break;
		case "POWERSHOT PERFORMANCE & STYLE - ELPH DIGITAL CAMERAS":
			cameraCategory = Category.POWERSHOT_PERFORMANCE_STYLE;
			break;
		case "POWERSHOT WATERPROOF - D SERIES DIGITAL CAMERAS":
			cameraCategory = Category.POWERSHOT_WATERPROOF;
			break;
		case "REFURBISHED EOS DIGITAL SLR AND M SERIES DIGITAL CAMERAS":
			cameraCategory = Category.REFURBISHED_EOS;
			break;
		case "REFURBISHED POWERSHOT DIGITAL CAMERAS":
			cameraCategory = Category.REFURBISHED_POWERSHOT;
			break;
		case "CLEARANCE POWERSHOT DIGITAL CAMERAS":
			cameraCategory = Category.CLEARANCE_POWERSHOT;
			break;
		}
		if(cameraCategory == null){
			return null;
		}
		
		Elements priceElement = cameraPage.getElementsByClass("final_price");
		double price = 0.00;
		if (priceElement != null) {
			String priceString = priceElement.text().replaceAll("[,$]", "");
//			System.out.println(priceString);
			try{
				price = Math.round(Float.parseFloat(priceString) * 100.0) / 100.0;
			}
			catch(NumberFormatException e){
				price = -1;
			}
		}
		Element imageElement = cameraPage.getElementById("productMainImage");
		String imageURL = "";
		if(imageElement != null){
			imageURL = "https://shop.usa.canon.com" + imageElement.attr("src");
		}
		return new Camera(name,cameraCategory,price,imageURL, productURL);
	}
}
