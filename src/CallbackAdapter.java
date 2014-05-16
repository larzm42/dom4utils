public class CallbackAdapter implements Callback {
	public String found(String value) {
		return value;
	}
	public String notFound() {
		return "";
	}
}
