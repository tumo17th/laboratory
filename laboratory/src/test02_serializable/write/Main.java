package test02_serializable.write;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import test02_serializable.data.SampleData;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println("------- Program Start -------");
		SampleData data = new SampleData("spike", "Spike Spiegel");

		String fileName = "external" + File.separator + "test02" + File.separator + "data.txt";
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
		oos.writeObject(data);

		oos.close();
		System.out.println("------- Finish Normal -------");
	}

}
