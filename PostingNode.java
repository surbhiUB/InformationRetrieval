public class PostingNode {
	public PostingNode(String docId, String termFrequency) {
		this.docId = docId;
		this.termFrequency = termFrequency;
	}
	String docId;
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	String termFrequency;
	public String getTermFrequency() {
		return termFrequency;
	}
	public void setTermFrequency(String termFrequency) {
		this.termFrequency = termFrequency;
	}
}
