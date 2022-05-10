package simulator.model;

public interface Observable<T> {
	//en las interfaces los m�todos son public por defecto
	void addObserver(T o);
	void removeObserver(T o);
}
