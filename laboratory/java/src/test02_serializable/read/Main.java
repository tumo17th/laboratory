package test02_serializable.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import test02_serializable.data.SampleData;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		System.out.println("------- Program Start -------");

		String fileName = "external" + File.separator + "test02" + File.separator + "data.txt";
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
		SampleData data = (SampleData)ois.readObject();
		ois.close();

		System.out.println(data.getSampleId() + " : " + data.getSampleName());
		System.out.println("------- Finish Normal -------");
	}

}
