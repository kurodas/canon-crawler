package main;

import java.util.Comparator;

public class CameraComparator implements Comparator<Camera> {

	@Override
	public int compare(Camera camera1, Camera camera2) {
		//Return ordering of categories
		if(camera1.category.compareTo(camera2.category) != 0)
			return camera1.category.compareTo(camera2.category);
		//Else, return ordering of names
		else
			return camera1.name.compareTo(camera2.name);
	}

}
