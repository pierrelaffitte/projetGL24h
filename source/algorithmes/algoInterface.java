package algorithmes;

public interface algoInterface {

	public Object importer(String file);
	public Object fit(Object o);
	public Object[] split(Object o);
	public void evaluate(Object model, Object test);

}
