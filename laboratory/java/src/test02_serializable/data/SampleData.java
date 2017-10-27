package test02_serializable.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SampleData implements Serializable {

	private String sampleId;
	private String sampleName;

	public SampleData(String sampleId, String sampleName) {
		this.sampleId = sampleId;
		this.sampleName = sampleName;
	}

	public String getSampleId() {
		return sampleId;
	}

	public String getSampleName() {
		return sampleName;
	}

}
